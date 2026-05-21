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
import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.User;
import net.p5w.dp.service.UserService;
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
     * 分页查询用户列表（对外VO接口）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserQuery query) {
        logStart("用户VO分页", query);
        PageResult<UserVO> pageData = userService.page(query);
        logEnd("用户VO分页");
        return pageSuccess(pageData);
    }

    /**
     * 分页查询用户列表（对内完整实体接口）
     */
    @GetMapping("/list")
    public Result<PageResult<User>> list(UserQuery query) {
        logStart("用户全量分页", query);
        PageResult<User> pageData = userService.list(query);
        logEnd("用户全量分页");
        return pageSuccess(pageData);
    }

    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable Long id) {
        logStart("查询用户详情", id);
        User user = userService.getById(id);
        logEnd("查询用户详情");
        return success(user);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody User user) {
        logStart("保存用户", user);
        userService.save(user);
        logEnd("保存用户");
        return success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        logStart("删除用户", id);
        userService.delete(id);
        logEnd("删除用户");
        return success();
    }
}