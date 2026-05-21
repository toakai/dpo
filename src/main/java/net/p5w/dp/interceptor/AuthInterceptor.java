package net.p5w.dp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.exception.BizException;
import net.p5w.dp.common.result.ResultCode;
import net.p5w.dp.service.AuthClientService;

/**
 * 签名鉴权拦截器
 * <p>
 * 校验请求头中的 appKey / sign / timestamp / nonce 四要素，
 * 任一缺失或签名不合法时抛出 {@link BizException}，
 * 由 {@link net.p5w.dp.common.exception.GlobalExceptionHandler} 统一返回 401/403。
 * </p>
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthClientService authClientService;

    public AuthInterceptor(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String appKey    = request.getHeader("appKey");
        String sign      = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");
        String nonce     = request.getHeader("nonce");
        log.debug("鉴权参数：appKey={}, timestamp={}, nonce={}", appKey, timestamp, nonce);

        // 四要素必须同时存在
        if (appKey == null || sign == null || timestamp == null || nonce == null) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }

        // 校验签名
        boolean ok = authClientService.checkSign(appKey, sign, timestamp, nonce, null);
        log.debug("签名校验结果: {}", ok);
        if (!ok) {
            throw new BizException(ResultCode.FORBIDDEN);
        }

        return true;
    }
}
