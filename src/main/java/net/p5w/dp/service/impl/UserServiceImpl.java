package net.p5w.dp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.User;
import net.p5w.dp.mapper.UserMapper;
import net.p5w.dp.service.UserService;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
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
        return userMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }
    }

    @Override
    public PageResult<User> getUserPage(Long page, Long size) {
        log.info("用户分页查询 page={}, size={}", page, size);

        PageHelper.startPage(page.intValue(), size.intValue());
        List<User> list = userMapper.list();
        PageInfo<User> pageInfo = new PageInfo<>(list);

        // 使用我们自己的 PageResult（完整分页信息）
        return PageResult.build(
                pageInfo.getTotal(),
                (long) pageInfo.getPageSize(),
                (long) pageInfo.getPageNum(),
                pageInfo.getList()
        );
    }
}