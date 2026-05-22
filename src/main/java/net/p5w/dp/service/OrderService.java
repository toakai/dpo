package net.p5w.dp.service;

import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.Order;
import net.p5w.dp.vo.OrderVO;

/**
 * 订单业务接口
 */
public interface OrderService {

    /**
     * 订单分页查询
     *
     * @return 分页VO结果
     */
    PageResult<OrderVO> page(OrderQuery query);

    /**
     * 新增订单
     *
     * @param order 订单实体信息
     */
    void save(Order order);

    /**
     * 根据id删除订单
     *
     * @param id 订单主键id
     */
    void delete(Long id);

    /**
     * 根据id查询订单详情
     *
     * @param id 订单主键id
     * @return 订单VO详情
     */
    OrderVO getById(Long id);

}