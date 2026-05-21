package net.p5w.dp.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.UserOrderQuery;
import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.User;
import net.p5w.dp.service.UserService;
import net.p5w.dp.vo.UserOrderVO;
import net.p5w.dp.vo.UserVO;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户列表（返回脱敏 VO，对外）
     * <p>请求示例：GET /api/user/page?page=1&size=10</p>
     *
     * @param query 分页及筛选条件
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserQuery query) {
        logStart("用户分页查询（VO）", query);
        PageResult<UserVO> pageData = userService.page(query);
        logEnd("用户分页查询（VO）");
        return pageSuccess(pageData);
    }

    /**
     * 分页查询用户列表（返回完整实体，内部管理）
     * <p>请求示例：GET /api/user/list?page=1&size=10</p>
     *
     * @param query 分页及筛选条件
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<User>> list(UserQuery query) {
        logStart("用户分页查询（全量）", query);
        PageResult<User> pageData = userService.list(query);
        logEnd("用户分页查询（全量）");
        return pageSuccess(pageData);
    }

    /**
     * 分页查询用户+嵌套订单列表（一对多）
     * <p>请求示例：GET /api/user/pageWithOrders?page=1&size=10&username=张</p>
     *
     * @param query 分页及筛选条件
     * @return 分页结果，每个用户携带其订单列表
     */
    @GetMapping("/pageWithOrders")
    public Result<PageResult<UserOrderVO>> pageWithOrders(UserOrderQuery query) {
        logStart("用户+订单分页查询", query);
        PageResult<UserOrderVO> pageData = userService.pageWithOrders(query);
        logEnd("用户+订单分页查询");
        return pageSuccess(pageData);
    }

    /**
     * 查询用户详情
     *
     * @param id 用户主键
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable Long id) {
        logStart("用户详情查询", id);
        User user = userService.getById(id);
        logEnd("用户详情查询");
        return success(user);
    }

    /**
     * 保存用户（有 id 则更新，无 id 则新增）
     *
     * @param user 用户信息（JSON 请求体）
     * @return 统一返回结果
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody User user) {
        logStart("保存用户", user);
        userService.save(user);
        logEnd("保存用户");
        return success();
    }

    /**
     * 根据 ID 删除用户
     *
     * @param id 用户主键
     * @return 统一返回结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        logStart("删除用户", id);
        userService.delete(id);
        logEnd("删除用户");
        return success();
    }
}
