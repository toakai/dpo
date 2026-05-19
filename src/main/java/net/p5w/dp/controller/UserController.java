package net.p5w.dp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.common.result.Result;
import net.p5w.dp.entity.User;
import net.p5w.dp.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    // @GetMapping("/list")
    // public Result<List<User>> list() {
    //     return Result.success(userService.list());
    // }

    @GetMapping("/list")
    public Result<?> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size
    ) {
        // 调用分页 Service
        PageResult<User> pageResult = userService.getUserPage(current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}