package net.p5w.dp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.Order;
import net.p5w.dp.mapper.OrderMapper;
import net.p5w.dp.service.OrderService;
import net.p5w.dp.vo.OrderVO;

/**
 * 订单 Service 实现类
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 分页查询订单（返回脱敏 VO）
     */
    @Override
    public PageResult<OrderVO> page(OrderQuery query) {
        log.info("分页查询订单：pageNum={}, pageSize={}", query.getPageNum(), query.getPageSize());

        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<Order> orderList = orderMapper.selectOrderList(query);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        List<OrderVO> voList = orderList.stream()
                .map(this::convertToOrderVO)
                .collect(Collectors.toList());

        return PageResult.build(pageInfo.getTotal(), pageInfo.getPageSize(), pageInfo.getPageNum(), voList);
    }

    /**
     * 新增订单
     * <p>创建时间和更新时间由业务层统一填充，不在 SQL 中使用数据库函数，便于测试和控制。</p>
     */
    @Override
    public void save(Order order) {
        Date now = new Date();
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderMapper.insertOrder(order);
        log.info("订单新增完成，orderNo={}", order.getOrderNo());
    }

    /**
     * 根据主键删除订单
     */
    @Override
    public void delete(Long id) {
        orderMapper.deleteById(id);
        log.info("订单删除完成，id={}", id);
    }

    /**
     * 根据主键查询订单详情
     */
    @Override
    public OrderVO getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            log.warn("订单不存在，id={}", id);
            return null;
        }
        return convertToOrderVO(order);
    }

    /**
     * Order 实体转 OrderVO（统一转换入口，便于后续扩展状态翻译、日期格式化等）
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        return vo;
    }
}
