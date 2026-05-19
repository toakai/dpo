package net.p5w.dp.mapper;

import java.util.List;

import net.p5w.dp.entity.Order;

public interface OrderMapper {
    int deleteById(Long id);

    int insert(Order record);

    Order getById(Long id);

    List<Order> list();

    int updateById(Order record);
}