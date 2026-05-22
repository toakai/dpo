package net.p5w.dp.interceptor;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.util.IpUtil;
import net.p5w.dp.entity.ApiCallLog;
import net.p5w.dp.service.ApiCallLogService;

/**
 * 请求日志拦截器
 * <p>
 * 职责：
 * <ol>
 *   <li>为每次请求生成唯一 requestId，写入 request 属性，供 {@link net.p5w.dp.config.GlobalResponseHandler} 自动填充到响应体</li>
 *   <li>记录请求开始/结束日志及耗时</li>
 *   <li>将调用日志（含请求参数和响应结果）持久化到 p5w_api_call_log 表</li>
 * </ol>
 * 注意：
 * <ul>
 *   <li>ThreadLocal 在 afterCompletion 中必须 remove，防止线程池复用时数据污染</li>
 *   <li>响应体捕获依赖 Filter 注册的 {@link ContentCachingResponseWrapper}，见 {@link net.p5w.dp.config.WebConfig}</li>
 * </ul>
 * </p>
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    /** 请求开始时间（毫秒），用于计算耗时 */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /** 当前请求的 requestId，afterCompletion 中更新日志需要 */
    private final ThreadLocal<String> requestIdThread = new ThreadLocal<>();

    /** params/result 记录的最大长度，防止超大请求/响应撑爆数据库 TEXT 字段 */
    private static final int MAX_LOG_LENGTH = 2000;

    private final ApiCallLogService apiCallLogService;

    public LogInterceptor(ApiCallLogService apiCallLogService) {
        this.apiCallLogService = apiCallLogService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        startTime.set(start);

        String ip = IpUtil.getClientIp(request);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String requestId = UUID.randomUUID().toString().replace("-", "");
        requestIdThread.set(requestId);
        // 将 requestId 写入 request 属性，GlobalResponseHandler 会自动注入响应体
        request.setAttribute("requestId", requestId);

        // 读取请求参数
        String params = getRequestParams(request);

        log.info("【请求开始】requestId={}, ip={}, uri={}, method={}", requestId, ip, uri, method);

        try {
            ApiCallLog callLog = new ApiCallLog();
            callLog.setRequestId(requestId);
            callLog.setIp(ip);
            callLog.setUrl(uri);
            callLog.setMethod(method);
            callLog.setParams(truncate(params));
            callLog.setCreateTime(new Date());
            apiCallLogService.insert(callLog);
        } catch (Exception e) {
            // 日志入库失败不影响主流程
            log.error("日志入库失败", e);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long cost = System.currentTimeMillis() - startTime.get();
        String requestId = requestIdThread.get();
        String ip = IpUtil.getClientIp(request);
        String uri = request.getRequestURI();

        log.info("【请求结束】requestId={}, ip={}, uri={}, 耗时={}ms", requestId, ip, uri, cost);

        try {
            // 读取响应体（依赖 ContentCachingResponseWrapper）
            String result = getResponseResult(response);

            ApiCallLog updateLog = new ApiCallLog();
            updateLog.setRequestId(requestId);
            updateLog.setCostTime(cost);
            updateLog.setResult(truncate(result));
            updateLog.setUpdateTime(new Date());
            apiCallLogService.updateByRequestId(updateLog);
        } catch (Exception e) {
            log.error("更新耗时日志失败", e);
        }

        // 必须清理 ThreadLocal，防止线程池复用时数据污染
        startTime.remove();
        requestIdThread.remove();
    }

    /**
     * 读取请求参数
     * <p>
     * GET 请求：从 queryString 解析
     * POST/PUT 请求：优先从 queryString 解析，如果有 JSON body 则追加
     * </p>
     */
    private String getRequestParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        // 从 query string 读取参数
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=");
                String[] values = entry.getValue();
                if (values != null && values.length > 0) {
                    sb.append(values[0]);
                }
            }
        }

        // 如果是 JSON body 请求，尝试读取 body
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            String queryString = request.getQueryString();
            // query string 为空时才读 body，避免重复
            if (queryString == null || queryString.isEmpty()) {
                try {
                    String body = readRequestBody(request);
                    if (body != null && !body.isEmpty()) {
                        sb.append(body);
                    }
                } catch (IOException e) {
                    log.warn("读取请求body失败: {}", e.getMessage());
                }
            }
        }

        return sb.toString();
    }

    /**
     * 读取请求 body 内容
     */
    private String readRequestBody(HttpServletRequest request) throws IOException {
        // 使用可重复读取的 request wrapper（如果 Filter 已包装）
        // 普通情况下直接读取（注意：stream 只能读一次，如果在 Filter 中已读取则此处为空）
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[1024];
        int len;
        try (java.io.InputStream is = request.getInputStream()) {
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "UTF-8"));
                // 防止超大 body
                if (sb.length() > MAX_LOG_LENGTH) {
                    sb.append("...(truncated)");
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 从 ContentCachingResponseWrapper 读取响应体
     * <p>
     * ContentCachingResponseWrapper 会在内容写入后缓存响应数据，
     * 但仅在 response 提交（flush/complete）后才能通过 getContentAsByteArray() 获取完整内容。
     * afterCompletion 执行时 response 已提交，可以安全读取。
     * </p>
     */
    private String getResponseResult(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, java.nio.charset.StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    /**
     * 截断过长内容，防止超出数据库 TEXT 字段限制
     */
    private String truncate(String str) {
        if (str == null) {
            return null;
        }
        return str.length() > MAX_LOG_LENGTH ? str.substring(0, MAX_LOG_LENGTH) : str;
    }
}
