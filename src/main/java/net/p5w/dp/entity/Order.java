package net.p5w.dp.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 */
@Data
public class Order {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 状态：1正常 2取消 3完成
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}