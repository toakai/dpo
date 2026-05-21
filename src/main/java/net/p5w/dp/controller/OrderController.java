package net.p5w.dp.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.common.query.PageQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.Order;
import net.p5w.dp.service.OrderService;
import net.p5w.dp.vo.OrderVO;

/**
 * 订单接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    /**
     * 订单分页列表接口
     *
     * @return 分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> pageList(OrderQuery query) {
        log.info("[调试] 最终接收的 pageSize: {}", query.getSize()); // 加这行
        logStart("订单分页查询", query);
        PageResult<OrderVO> pageResult = orderService.page(query);
        logEnd("订单分页查询");
        return pageSuccess(pageResult);
    }

    /**
     * 新增订单接口
     *
     * @param order 订单实体参数
     * @return 统一返回结果
     */
    @PostMapping("/save")
    public Result<Void> saveOrder(@RequestBody Order order) {
        log.info("前端发起新增订单请求");
        orderService.save(order);
        return success();
    }

    /**
     * 删除订单接口
     *
     * @param id 订单主键id
     * @return 统一返回结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> removeOrder(@PathVariable Long id) {
        log.info("前端发起删除订单请求，id={}", id);
        orderService.delete(id);
        return success();
    }

    /**
     * 订单详情查询接口
     *
     * @param id 订单主键id
     * @return 订单详情VO
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("前端请求订单详情接口，id={}", id);
        OrderVO orderVO = orderService.getById(id);
        return success(orderVO);
    }
}