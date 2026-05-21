package net.p5w.dp.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单视图返回对象
 *
 * @author p5w
 * @date 2026-05-21
 */
@Data
public class OrderVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态：1正常 2取消 3完成
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;
}