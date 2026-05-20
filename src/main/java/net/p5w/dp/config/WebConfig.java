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
        log.info("开始注册拦截器...");

        // 日志拦截器
        registry.addInterceptor(new LogInterceptor(apiCallLogService))
                .addPathPatterns("/**");

        // IP限流拦截器
        registry.addInterceptor(new IpLimitInterceptor(ipRateLimiter))
                .addPathPatterns("/api/**");

        // 鉴权拦截器
        registry.addInterceptor(new AuthInterceptor(authClientService))
                .addPathPatterns("/api/**");

        log.info("拦截器注册完成：日志、限流、鉴权");
    }
}