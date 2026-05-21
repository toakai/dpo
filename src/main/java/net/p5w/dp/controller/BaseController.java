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
 * 基础控制器
 * 所有控制器继承此类，实现公共方法复用
 */
@Slf4j
public class BaseController {

    /**
     * 成功返回（无数据）
     */
    protected <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 成功返回（带数据）
     */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 分页成功返回
     */
    protected <T> Result<PageResult<T>> pageSuccess(PageResult<T> pageData) {
        return Result.success(pageData);
    }

    /**
     * 失败返回
     */
    protected <T> Result<T> fail(ResultCode code) {
        return Result.fail(code.getCode(), code.getMsg());
    }

    /**
     * 获取HttpServletRequest对象
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /**
     * 获取客户端真实IP
     */
    protected String getClientIp() {
        return IpUtil.getClientIp(getRequest());
    }

    /**
     * 接口开始日志
     */
    protected void logStart(String name, Object... ps) {
        log.info("[{}] IP:{} | params:{}", name, getClientIp(), ps);
    }

    /**
     * 接口结束日志
     */
    protected void logEnd(String name) {
        log.info("[{}] 执行完成", name);
    }

}