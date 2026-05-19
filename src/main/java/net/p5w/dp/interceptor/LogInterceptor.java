package net.p5w.dp.interceptor;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.util.IpUtil;
import net.p5w.dp.common.util.JsonUtil;
import net.p5w.dp.entity.ApiCallLog;
import net.p5w.dp.service.ApiCallLogService;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    // 保存 开始时间 + requestId
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final ThreadLocal<String> requestIdThread = new ThreadLocal<>();

    private final ApiCallLogService apiCallLogService;

    public LogInterceptor(ApiCallLogService apiCallLogService) {
        this.apiCallLogService = apiCallLogService;
    }

    // ===================== 1. 请求开始：插入日志 =====================
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTime.set(System.currentTimeMillis());

        String ip = IpUtil.getClientIp(request);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String requestId = UUID.randomUUID().toString().replace("-", "");

        request.setAttribute("requestId", requestId);

        // 保存 requestId，结束时用
        requestIdThread.set(requestId);

        log.info("【请求开始】时间：{}，IP：{}，地址：{}，方式：{}", new Date(), ip, uri, method);

        // 插入日志（只有基础信息，没有耗时）
        try {
            ApiCallLog log = new ApiCallLog();
            log.setRequestId(requestId);
            log.setIp(ip);
            log.setUrl(uri);
            log.setMethod(method);
            log.setParams(JsonUtil.toJson(request.getParameterMap()));
            log.setCreateTime(new Date());
            apiCallLogService.insert(log);
        } catch (Exception e) {
            log.error("日志插入失败", e);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    // ===================== 2. 请求结束：更新耗时 =====================
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long cost = System.currentTimeMillis() - startTime.get();
        String requestId = requestIdThread.get();

        log.info("【请求结束】时间：{}，耗时：{}ms", new Date(), cost);

        // 更新耗时到数据库
        try {
            ApiCallLog updateLog = new ApiCallLog();
            updateLog.setRequestId(requestId);
            updateLog.setCostTime(cost); // 耗时存入
            updateLog.setUpdateTime(new Date()); // 必须手动赋值
            apiCallLogService.updateByRequestId(updateLog);
        } catch (Exception e) {
            log.error("更新耗时失败", e);
        }

        // 清空线程变量
        startTime.remove();
        requestIdThread.remove();
    }
}