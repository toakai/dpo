package net.p5w.dp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.Order;
import net.p5w.dp.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/list")
    public Result<List<Order>> list() {
        return Result.success(orderService.list());
    }

    @GetMapping("/{id}")
    public Result<Order> get(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Order order) {
        orderService.save(order);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return Result.success();
    }
}