package net.p5w.dp.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.exception.BizException;
import net.p5w.dp.common.result.ResultCode;
import net.p5w.dp.common.util.IpUtil;

@Slf4j
public class LimitInterceptor implements HandlerInterceptor {

    private static final int MAX_LIMIT = 10;

    private static final Map<String, Integer> IP_COUNT = new ConcurrentHashMap<>();

    static {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    IP_COUNT.clear();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = IpUtil.getClientIp(request);
        int count = IP_COUNT.getOrDefault(ip, 0);

        if (count >= MAX_LIMIT) {
            log.warn("IP: {} 请求次数过多", ip);
            throw new BizException(ResultCode.FORBIDDEN);
        }

        IP_COUNT.put(ip, count + 1);
        return true;
    }
}