package net.p5w.dp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.Result;

/**
 * 公共接口控制器（无需鉴权）
 * <p>
 * 提供健康检查等基础接口，路径不在 /api/** 下，不受鉴权拦截器约束。
 * </p>
 */
@RestController
public class PublicController extends BaseController {

    /**
     * 服务健康检查
     * <p>
     * requestId 由 {@link net.p5w.dp.interceptor.LogInterceptor} 生成并注入 request 属性，
     * {@link net.p5w.dp.config.GlobalResponseHandler} 会自动将其写入响应体，
     * 此处无需手动设置。
     * </p>
     *
     * @param requestId 本次请求唯一标识
     * @return 服务状态描述
     */
    @GetMapping("/")
    public Result<String> index(@RequestAttribute("requestId") String requestId) {
        return success("p5w dp api running ok");
    }
}
