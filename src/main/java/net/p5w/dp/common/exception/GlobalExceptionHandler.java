package net.p5w.dp.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.p5w.dp.common.result.Result;
import net.p5w.dp.common.result.ResultCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Result<?> bizException(BizException e) {
        return Result.fail(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> exception(Exception e) {
        e.printStackTrace();
        log.error("服务器异常：{}", e.getMessage());
        return Result.fail(ResultCode.SERVER_ERROR);
    }
}