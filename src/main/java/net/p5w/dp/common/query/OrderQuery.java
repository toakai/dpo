package net.p5w.dp.common.query;

import lombok.Data;

/**
 * 订单分页查询条件
 * <p>
 * 继承 {@link PageQuery} 获得分页能力（page / size），
 * 在此基础上扩展订单业务筛选字段。
 * </p>
 */
@Data
public class OrderQuery extends PageQuery {

    /** 按用户 ID 筛选 */
    private Long userId;

    /** 按订单号精确筛选 */
    private String orderNo;

    /** 按订单状态筛选：1-正常 2-取消 3-完成 */
    private Byte status;

}
