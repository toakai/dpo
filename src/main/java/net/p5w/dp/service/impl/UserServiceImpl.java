package net.p5w.dp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.User;
import net.p5w.dp.mapper.UserMapper;
import net.p5w.dp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }

    @Override
    public int update(User record) {
        return userMapper.updateById(record.getId());
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    public void save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user.getId());
        }
    }

    @Override
    public PageResult<User> getUserPage(long current, long size) {
        // 1. 计算起始行
        long offset = (current - 1) * size;

        // 2. 查询分页列表
        List<User> userList = userMapper.selectUserPage(offset, size);

        // 3. 查询总条数
        long total = userMapper.selectUserCount();

        // 4. 封装分页结果
        return PageResult.build(total, size, current, userList);
    }

}

