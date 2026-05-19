package net.p5w.dp.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.p5w.dp.interceptor.AuthInterceptor;
import net.p5w.dp.interceptor.LimitInterceptor;
import net.p5w.dp.interceptor.LogInterceptor;
import net.p5w.dp.service.ApiCallLogService;
import net.p5w.dp.service.AuthClientService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthClientService authClientService;

    @Resource
    private ApiCallLogService apiCallLogService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器（必须传 service）
        registry.addInterceptor(new LogInterceptor(apiCallLogService)).addPathPatterns("/**");

        registry.addInterceptor(new LimitInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(new AuthInterceptor(authClientService)).addPathPatterns("/api/**");
    }
}