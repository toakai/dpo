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

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
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
        request.setAttribute("requestId", requestId);

        log.info("【请求开始】requestId={}, ip={}, uri={}, method={}", requestId, ip, uri, method);

        try {
            ApiCallLog log = new ApiCallLog();
            log.setRequestId(requestId);
            log.setIp(ip);
            log.setUrl(uri);
            log.setMethod(method);
            log.setCreateTime(new Date());
            apiCallLogService.insert(log);
        } catch (Exception e) {
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

        startTime.remove();
        requestIdThread.remove();
    }
}