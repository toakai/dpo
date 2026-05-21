package net.p5w.dp.mapper;

import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.entity.Order;

import java.util.List;

/**
 * 订单数据访问层
 */
public interface OrderMapper {

    /**
     * 查询全部订单列表（分页使用）
     *
     * @return 订单实体集合
     */
    List<Order> selectOrderList();

    /**
     * 新增订单数据
     *
     * @param order 订单实体
     * @return 受影响行数
     */
    int insertOrder(Order order);

    /**
     * 根据主键删除订单
     *
     * @param id 主键id
     * @return 受影响行数
     */
    int deleteById(Long id);

    /**
     * 根据主键查询订单详情
     *
     * @param id 主键id
     * @return 订单实体
     */
    Order selectById(Long id);

    List<Order> selectOrderList(OrderQuery query);
}