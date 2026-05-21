package net.p5w.dp.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.limit.IpRateLimiter;
import net.p5w.dp.interceptor.AuthInterceptor;
import net.p5w.dp.interceptor.IpLimitInterceptor;
import net.p5w.dp.interceptor.LogInterceptor;
import net.p5w.dp.service.ApiCallLogService;
import net.p5w.dp.service.AuthClientService;

/**
 * Web MVC 配置
 * <p>
 * 拦截器注册顺序（按执行顺序排列）：
 * <ol>
 *   <li>{@link LogInterceptor}：全路径，生成 requestId 并记录请求日志</li>
 *   <li>{@link IpLimitInterceptor}：/api/**，IP 滑动窗口限流</li>
 *   <li>{@link AuthInterceptor}：/api/**，签名鉴权</li>
 * </ol>
 * </p>
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthClientService authClientService;

    @Resource
    private ApiCallLogService apiCallLogService;

    @Resource
    private IpRateLimiter ipRateLimiter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 日志拦截器（所有请求）
        registry.addInterceptor(new LogInterceptor(apiCallLogService))
                .addPathPatterns("/**");

        // 2. IP 限流拦截器（API 接口）
        registry.addInterceptor(new IpLimitInterceptor(ipRateLimiter))
                .addPathPatterns("/api/**");

        // 3. 签名鉴权拦截器（API 接口）
        registry.addInterceptor(new AuthInterceptor(authClientService))
                .addPathPatterns("/api/**");

        log.info("拦截器注册完成：日志（/**）、IP 限流（/api/**）、鉴权（/api/**）");
    }
}
