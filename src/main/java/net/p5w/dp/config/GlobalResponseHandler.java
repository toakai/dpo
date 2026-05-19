package net.p5w.dp.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import net.p5w.dp.common.result.Result;

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理 JSON 响应
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 如果返回结果不是 Result，直接返回
        if (!(body instanceof Result)) {
            return body;
        }

        // 获取 request 对象
        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();

        // 从 request 中取出拦截器生成的 requestId
        String requestId = (String) req.getAttribute("requestId");

        // 自动设置到返回结果里
        Result<?> result = (Result<?>) body;
        result.setRequestId(requestId);

        return result;
    }
}
