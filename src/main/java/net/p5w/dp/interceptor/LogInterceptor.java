package net.p5w.dp.interceptor;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

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
 *   <li>异步将调用日志持久化到 api_call_log 表</li>
 * </ol>
 * 注意：ThreadLocal 在 afterCompletion 中必须 remove，防止线程池复用时数据污染。
 * </p>
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    /** 请求开始时间（毫秒），用于计算耗时 */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /** 当前请求的 requestId，afterCompletion 中更新日志耗时需要 */
    private final ThreadLocal<String> requestIdThread = new ThreadLocal<>();

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

        log.info("【请求开始】requestId={}, ip={}, uri={}, method={}", requestId, ip, uri, method);

        try {
            ApiCallLog callLog = new ApiCallLog();
            callLog.setRequestId(requestId);
            callLog.setIp(ip);
            callLog.setUrl(uri);
            callLog.setMethod(method);
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
            ApiCallLog updateLog = new ApiCallLog();
            updateLog.setRequestId(requestId);
            updateLog.setCostTime(cost);
            updateLog.setUpdateTime(new Date());
            apiCallLogService.updateByRequestId(updateLog);
        } catch (Exception e) {
            log.error("更新耗时日志失败", e);
        }

        // 必须清理 ThreadLocal，防止线程池复用时数据污染
        startTime.remove();
        requestIdThread.remove();
    }
}
