package net.p5w.dp.mapper;

import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.entity.Order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 订单数据访问层
 */
public interface OrderMapper {

    /**
     * 按条件分页查询订单列表（配合 PageHelper 使用）
     *
     * @param query 分页及筛选条件
     * @return 订单实体集合
     */
    List<Order> selectOrderList(OrderQuery query);

    /**
     * 根据用户 ID 列表批量查询订单
     *
     * @param userIds 用户 ID 列表
     * @return 订单实体集合
     */
    List<Order> selectByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 新增订单
     *
     * @param order 订单实体
     * @return 受影响行数
     */
    int insertOrder(Order order);

    /**
     * 根据主键删除订单
     *
     * @param id 订单主键
     * @return 受影响行数
     */
    int deleteById(Long id);

    /**
     * 根据主键查询订单详情
     *
     * @param id 订单主键
     * @return 订单实体，不存在则返回 null
     */
    Order selectById(Long id);
}
