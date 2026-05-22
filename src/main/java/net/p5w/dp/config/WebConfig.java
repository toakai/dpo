package net.p5w.dp.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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
 * Filter 注册：
 * <ol>
 *   <li>{@link ContentCachingFilter}：全路径，包装 request/response 以支持 body 的重复读取和缓存</li>
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

    /**
     * 注册 ContentCachingFilter，使 LogInterceptor 能够读取请求体和响应体内容
     * <p>
     * 优先级设置为 Ordered.HIGHEST_PRECEDENCE，确保在所有 Filter 之前执行，
     * 这样后续的 Interceptor 和 Controller 都能从 wrapper 中正常读取 body。
     * </p>
     */
    @Bean
    public FilterRegistrationBean<ContentCachingFilter> contentCachingFilterRegistration() {
        FilterRegistrationBean<ContentCachingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ContentCachingFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(-100);
        registration.setName("contentCachingFilter");
        return registration;
    }

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

    /**
     * 请求/响应内容缓存 Filter
     * <p>
     * 使用 Spring 提供的 {@link ContentCachingRequestWrapper} 和 {@link ContentCachingResponseWrapper}
     * 包装原始 request/response，使得 body 内容可以被多次读取。
     * </p>
     * 注意：
     * <ul>
     *   <li>ContentCachingRequestWrapper 在第一次读取 body 时进行缓存，之后可通过 getContentAsByteArray() 获取</li>
     *   <li>ContentCachingResponseWrapper 需要在 Filter 链结束后调用 copyBodyToResponse() 将缓存内容写回客户端</li>
     * </ul>
     */
    static class ContentCachingFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            // 只包装 HTTP 请求
            if (request instanceof HttpServletRequest && response instanceof javax.servlet.http.HttpServletResponse) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                // 对 JSON 类型的请求包装 request，使 body 可重复读取
                if (isJsonRequest(httpRequest)) {
                    request = new ContentCachingRequestWrapper(httpRequest);
                }
                // 包装 response 以缓存响应体
                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
                        (javax.servlet.http.HttpServletResponse) response);

                chain.doFilter(request, responseWrapper);

                // 将缓存内容写回原始 response，否则客户端收不到响应
                responseWrapper.copyBodyToResponse();
            } else {
                chain.doFilter(request, response);
            }
        }

        private boolean isJsonRequest(HttpServletRequest request) {
            String contentType = request.getContentType();
            return contentType != null && contentType.contains("application/json");
        }
    }
}
