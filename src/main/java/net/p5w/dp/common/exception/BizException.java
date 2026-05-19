package net.p5w.dp.common.exception;

import net.p5w.dp.common.result.ResultCode;

public class BizException extends RuntimeException {
    private ResultCode code;

    public BizException(ResultCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public ResultCode getCode() {
        return code;
    }
}