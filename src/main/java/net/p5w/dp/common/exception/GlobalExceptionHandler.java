package net.p5w.dp.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.result.ResultCode;

@Slf4j  // 加日志
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // ======================== 1. 分页 / 表单参数绑定异常（setter 校验） ========================
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public Result<Void> handleBindException(org.springframework.validation.BindException e) {
        String defaultMessage = e.getFieldError().getDefaultMessage();

        // 提取我们自己抛的真实错误信息
        String realMsg = defaultMessage;
        if (defaultMessage.contains("IllegalArgumentException")) {
            realMsg = defaultMessage.split("java.lang.IllegalArgumentException: ")[1].trim();
        }

        log.error("参数绑定异常：{}", realMsg); // 规范日志
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), realMsg);
    }

    // ======================== 2. 普通业务参数异常 ========================
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        log.error("业务参数异常：{}", e.getMessage()); // 规范日志
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    // ======================== 3. 全局兜底异常 ========================
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("服务器未知异常", e); // 必须打完整堆栈！
        return Result.fail(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMsg());
    }
}