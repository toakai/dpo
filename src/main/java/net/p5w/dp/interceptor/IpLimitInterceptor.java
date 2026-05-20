package net.p5w.dp.interceptor;

import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.limit.IpRateLimiter;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.util.IpUtil;

@Slf4j
public class IpLimitInterceptor implements HandlerInterceptor {

    private final IpRateLimiter ipRateLimiter;

    public IpLimitInterceptor(IpRateLimiter ipRateLimiter) {
        this.ipRateLimiter = ipRateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getClientIp(request);
        String uri = request.getRequestURI();

        log.info("限流拦截 → IP:{}, URI:{}", ip, uri);

        if (!ipRateLimiter.tryAcquire(ip)) {
            // 生成 requestId
            String requestId = UUID.randomUUID().toString().replace("-", "");

            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();

            Result<Object> result = Result.fail(requestId, 429, "访问频率过高，请稍后再试");

            out.write(JSON.toJSONString(result));

            out.flush();
            out.close();
            return false;
        }
        return true;
    }
}