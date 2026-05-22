package net.p5w.dp.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.OrderQuery;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.Order;
import net.p5w.dp.service.OrderService;
import net.p5w.dp.vo.OrderVO;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    /**
     * 订单分页查询
     * <p>请求示例：GET /api/order/page?page=2&size=3</p>
     *
     * @param query 分页及筛选条件
     * @return 分页数据
     */
    @GetMapping("/page")
    public Result<PageInfo<OrderVO>> pageList(OrderQuery query) {
        logStart("订单分页查询", query);
        PageInfo<OrderVO> pageInfo = orderService.page(query);
        logEnd("订单分页查询");
        return pageSuccess(pageInfo);
    }

    /**
     * 新增订单
     *
     * @param order 订单实体（JSON 请求体）
     * @return 统一返回结果
     */
    @PostMapping("/save")
    public Result<Void> saveOrder(@RequestBody Order order) {
        logStart("新增订单", order);
        orderService.save(order);
        logEnd("新增订单");
        return success();
    }

    /**
     * 删除订单
     *
     * @param id 订单主键
     * @return 统一返回结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> removeOrder(@PathVariable Long id) {
        logStart("删除订单", id);
        orderService.delete(id);
        logEnd("删除订单");
        return success();
    }

    /**
     * 查询订单详情
     *
     * @param id 订单主键
     * @return 订单详情 VO
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        logStart("订单详情查询", id);
        OrderVO orderVO = orderService.getById(id);
        logEnd("订单详情查询");
        return success(orderVO);
    }
}
