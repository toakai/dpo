package net.p5w.dp.service;

import java.util.List;

import net.p5w.dp.entity.Order;

public interface OrderService {

    List<Order> list();

    Order getById(Long id);

    void save(Order order);

    void delete(Long id);
}

