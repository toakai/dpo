package net.p5w.dp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.result.ResultCode;
import net.p5w.dp.common.util.IpUtil;

/**
 * 控制器基类
 * <p>
 * 所有 Controller 继承此类，复用以下公共能力：
 * <ul>
 *   <li>统一 success / fail 响应封装</li>
 *   <li>分页响应快捷方法</li>
 *   <li>获取客户端 IP 及 HttpServletRequest</li>
 *   <li>接口入/出日志</li>
 * </ul>
 * </p>
 */
@Slf4j
public class BaseController {

    /** 成功返回（无数据） */
    protected <T> Result<T> success() {
        return Result.success();
    }

    /** 成功返回（带数据） */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /** 分页成功返回 */
    protected <T> Result<PageResult<T>> pageSuccess(PageResult<T> pageData) {
        return Result.success(pageData);
    }

    /** 失败返回 */
    protected <T> Result<T> fail(ResultCode code) {
        return Result.fail(code.getCode(), code.getMsg());
    }

    /** 获取当前请求的 HttpServletRequest */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /** 获取客户端真实 IP（兼容反向代理） */
    protected String getClientIp() {
        return IpUtil.getClientIp(getRequest());
    }

    /**
     * 接口入口日志
     *
     * @param name 接口名称
     * @param ps   入参（可变参数）
     */
    protected void logStart(String name, Object... ps) {
        log.info("[{}] ip={} | params={}", name, getClientIp(), ps);
    }

    /** 接口完成日志 */
    protected void logEnd(String name) {
        log.info("[{}] 执行完成", name);
    }
}
