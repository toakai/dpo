package net.p5w.dp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.p5w.dp.entity.Order;
import net.p5w.dp.mapper.OrderMapper;
import net.p5w.dp.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> list() {
        return orderMapper.list();
    }

    @Override
    public Order getById(Long id) {
        return orderMapper.getById(id);
    }

    @Override
    public void save(Order order) {
        if (order.getId() == null) {
            orderMapper.insert(order);
        } else {
            orderMapper.updateById(order);
        }
    }

    @Override
    public void delete(Long id) {
        orderMapper.deleteById(id);
    }
}

