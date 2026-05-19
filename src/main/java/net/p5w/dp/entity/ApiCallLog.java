package net.p5w.dp.entity;

import java.util.Date;

import lombok.Data;

/**
 * 接口调用日志表
 */
@Data
public class ApiCallLog {
    /**
     * 主键
     */
    private Long id;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 耗时(ms)
     */
    private Long costTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}