package net.p5w.dp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.exception.BizException;
import net.p5w.dp.common.result.ResultCode;
import net.p5w.dp.service.AuthClientService;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthClientService authClientService;

    public AuthInterceptor(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头参数
        String appKey = request.getHeader("appKey");
        String sign = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");
        String nonce = request.getHeader("nonce");
        log.info("appKey: {}, sign: {}, timestamp: {}, nonce: {}", appKey, sign, timestamp, nonce);

        // 必须传这4个参数
        if (appKey == null || sign == null || timestamp == null || nonce == null) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }

        // 校验签名（核心）
        boolean ok = authClientService.checkSign(appKey, sign, timestamp, nonce, null);
        System.out.println("ok: " + ok);
        if (!ok) {
            throw new BizException(ResultCode.FORBIDDEN);
        }

        return true;
    }
}