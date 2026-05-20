package net.p5w.dp.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.result.ResultCode;

/**
 * 控制器公共父类
 * 所有业务Controller统一继承此类，复用通用能力
 */
@Slf4j
@RestController
public class BaseController {

    /**
     * 分页通用常量
     */
    protected static final Integer MIN_PAGE_NUM = 1;
    protected static final Integer MAX_PAGE_SIZE = 100;
    protected static final Integer DEFAULT_PAGE_SIZE = 10;

    // ==================== 统一响应返回 ====================

    /**
     * 成功无数据
     */
    protected <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 成功携带业务数据
     */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 分页专用成功返回
     */
    protected <T> Result<PageResult<T>> pageSuccess(PageResult<T> pageData) {
        return Result.success(pageData);
    }

    /**
     * 枚举统一失败返回
     */
    protected <T> Result<T> fail(ResultCode resultCode) {
        return Result.fail(resultCode.getCode(), resultCode.getMsg());
    }

    // ==================== 分页参数统一校验 ====================
    protected <T> Result<T> validPageParam(Long pageNum, Long pageSize) {
        if (pageNum < MIN_PAGE_NUM) {
            return fail(ResultCode.PAGE_NUM_ERROR);
        }
        if (pageSize < 1) {
            return fail(ResultCode.PAGE_SIZE_ERROR);
        }
        if (pageSize > MAX_PAGE_SIZE) {
            return fail(ResultCode.PAGE_SIZE_MAX_ERROR);
        }
        return null;
    }

    // ==================== 请求上下文获取 ====================

    /**
     * 获取当前Http请求对象
     */
    protected HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    /**
     * 获取客户端真实IP地址
     */
    protected String getClientIp() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return "未知IP";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 生成全局唯一请求ID
     */
    protected String createRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // ==================== 统一接口日志 ====================

    /**
     * 接口进入日志
     */
    protected void logApiStart(String apiName, Object... params) {
        log.info("[{}] 接口开始调用，访问IP：{}，请求参数：{}", apiName, getClientIp(), params);
    }

    /**
     * 接口结束日志
     */
    protected void logApiEnd(String apiName) {
        log.info("[{}] 接口调用执行完毕", apiName);
    }

    // ==================== 通用空值判断工具 ====================

    /**
     * 判断字符串是否为空
     */
    protected boolean strIsEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断对象非空
     */
    protected boolean objIsNotNull(Object obj) {
        return obj != null;
    }
}