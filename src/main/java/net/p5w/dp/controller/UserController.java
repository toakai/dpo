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
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.User;
import net.p5w.dp.service.UserService;

/**
 * 用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户分页列表查询
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return 分页用户数据
     */
    @GetMapping("/list")
    public Result<PageResult<User>> getUserPageList(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        // 统一分页参数校验
        Result<PageResult<User>> checkResult = validPageParam(pageNum, pageSize);
        if (checkResult != null) {
            return checkResult;
        }
        logApiStart("用户分页列表查询", pageNum, pageSize);
        PageResult<User> pageData = userService.getUserPage(pageNum, pageSize);
        logApiEnd("用户分页列表查询");
        return pageSuccess(pageData);
    }

    /**
     * 根据主键查询用户详情
     *
     * @param id 用户主键ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserDetail(@PathVariable Long id) {
        logApiStart("查询用户详情", id);
        User user = userService.getById(id);
        logApiEnd("查询用户详情");
        return success(user);
    }

    /**
     * 新增/编辑用户信息
     *
     * @param user 用户实体
     * @return 操作结果
     */
    @PostMapping("/save")
    public Result<Void> saveOrUpdateUser(@RequestBody User user) {
        logApiStart("新增或编辑用户", user);
        userService.save(user);
        logApiEnd("新增或编辑用户");
        return success();
    }

    /**
     * 根据ID删除用户
     *
     * @param id 用户主键ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> removeUser(@PathVariable Long id) {
        logApiStart("删除用户数据", id);
        userService.delete(id);
        logApiEnd("删除用户数据");
        return success();
    }
}