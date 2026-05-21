package net.p5w.dp.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import net.p5w.dp.common.query.PageQuery;

/**
 * 分页参数自动解析器
 * 自动封装 page / size 请求参数为 PageQuery
 */
public class PageQueryArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageQuery.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        PageQuery pageQuery = new PageQuery();
        String pageStr = webRequest.getParameter("page");
        String sizeStr = webRequest.getParameter("size");

        if (pageStr != null) {
            pageQuery.setPage(Integer.parseInt(pageStr));
        }
        if (sizeStr != null) {
            pageQuery.setSize(Integer.parseInt(sizeStr));
        }
        return pageQuery;
    }
}