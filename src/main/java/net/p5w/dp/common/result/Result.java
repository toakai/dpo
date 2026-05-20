package net.p5w.dp.common.result;

import lombok.Data;

@Data
public class Result<T> {

    private String requestId;
    private Integer code;
    private String msg;
    T data;

    // ===================== 成功 =====================
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMsg(ResultCode.SUCCESS.getMsg());
        r.setData(data);
        return r;
    }

    // ===================== 失败（带 requestId） =====================
    public static <T> Result<T> fail(String requestId, int code, String msg) {
        Result<T> r = new Result<>();
        r.setRequestId(requestId);
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    // ===================== 失败（不带 requestId） =====================
    public static <T> Result<T> fail(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> Result<T> fail(ResultCode code) {
        Result<T> r = new Result<>();
        r.setCode(code.getCode());
        r.setMsg(code.getMsg());
        return r;
    }
}