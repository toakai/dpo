package net.p5w.dp.common.query;

import lombok.Data;

/**
 * 用户+订单嵌套查询条件
 * <p>
 * 继承 {@link PageQuery} 获得分页能力（page / size），
 * 在此基础上扩展用户业务筛选字段。
 * </p>
 */
@Data
public class UserOrderQuery extends PageQuery {

    /** 按用户名模糊搜索 */
    private String username;
}
