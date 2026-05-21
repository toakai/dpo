package net.p5w.dp.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.Order;
import net.p5w.dp.mapper.OrderMapper;
import net.p5w.dp.service.OrderService;
import net.p5w.dp.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单业务实现类
 *
 * @author p5w
 * @date 2026-05-21
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 订单分页查询
     */
    @Override
    public PageResult<OrderVO> page(OrderQuery query) {
        PageHelper.startPage(query.getPage(), query.getSize());
        List<Order> orderList = orderMapper.selectOrderList(query);

        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        List<OrderVO> voList = orderList.stream().map(o -> {
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(o, vo);
            return vo;
        }).collect(Collectors.toList());

        return PageResult.build(pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), voList);
    }

    /**
     * 新增订单
     */
    @Override
    public void save(Order order) {
        log.info("开始新增订单业务处理");
        Date now = new Date();
        // 业务层统一填充创建、修改时间，不在SQL写函数
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderMapper.insertOrder(order);
        log.info("订单新增完成");
    }

    /**
     * 删除订单
     */
    @Override
    public void delete(Long id) {
        log.info("开始执行订单删除，订单id={}", id);
        orderMapper.deleteById(id);
        log.info("订单删除完成");
    }

    /**
     * 查询订单详情
     */
    @Override
    public OrderVO getById(Long id) {
        log.info("根据id查询订单详情，id={}", id);
        Order order = orderMapper.selectById(id);
        if (order == null) {
            log.warn("未查询到对应订单数据，id={}", id);
            return null;
        }
        return convertToOrderVO(order);
    }

    /**
     * 统一实体转VO私有方法，全局复用
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        // 如需状态转文字、日期格式化等统一在此扩展
        return vo;
    }
}