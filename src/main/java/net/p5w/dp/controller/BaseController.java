package net.p5w.dp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;
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
@RestController
public class BaseController {

    // 默认页码
    protected static final Integer DEFAULT_PAGE_NUM = 1;
    // 默认每页条数
    protected static final Integer DEFAULT_PAGE_SIZE = 10;
    // 最大允许每页条数
    protected static final Integer MAX_PAGE_SIZE = 100;

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
     * 构建页码：为空则赋默认值1
     */
    protected Integer buildPageNum(Integer pageNum) {
        return pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
    }

    /**
     * 构建每页条数：为空则赋默认值10
     */
    protected Integer buildPageSize(Integer pageSize) {
        return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
    }

    /**
     * 分页参数合法性校验
     */
    protected <T> Result<T> checkPage(Integer pageNum, Integer pageSize) {
        if (pageNum < 1) {
            return fail(ResultCode.PAGE_NUM_ERROR);
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            return fail(ResultCode.PAGE_SIZE_MAX_ERROR);
        }
        return null;
    }

    /**
     * 获取HttpServletRequest对象
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

    /**
     * 获取客户端真实IP
     */
    protected String getClientIp() {
        HttpServletRequest req = getRequest();
        if (req == null) {
            return "127.0.0.1";
        }
        return IpUtil.getClientIp(req);
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