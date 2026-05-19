package net.p5w.dp.entity;

import java.util.Date;

import lombok.Data;

/**
 * 鉴权客户端表
 */
@Data
public class AuthClient {
    /**
     * 主键
     */
    private Long id;

    /**
     * 应用标识
     */
    private String appKey;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 状态：1启用 0禁用
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

}