package net.p5w.dp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.User;
import net.p5w.dp.mapper.UserMapper;
import net.p5w.dp.service.UserService;
import net.p5w.dp.vo.UserVO;

/**
 * 用户Service实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 分页查询用户（完整实体）
     */
    @Override
    public PageResult<User> getUserPage(Integer pageNum, Integer pageSize) {
        log.info("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);
        log.warn("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);
        log.error("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);
        log.debug("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);
        log.trace("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);

        // ===========================
        // 必须紧挨着查询，中间不能有任何代码
        // ===========================
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.list(); // 必须紧跟在 startPage 下一行！

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        return PageResult.build(
                pageInfo.getTotal(),
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getList()
        );
    }

    /**
     * 分页查询用户（VO对象）
     */
    @Override
    public PageResult<UserVO> getUserVoPage(Integer pageNum, Integer pageSize) {
        log.info("开始分页：pageNum={}, pageSize={}", pageNum, pageSize);

        // ===========================
        // 【终极正确】紧挨着查询！
        // ===========================
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.list(); // 必须紧跟！

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        List<UserVO> voList = userList.stream().map(user -> {
            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setRealName(user.getRealName());
            vo.setPhone(user.getPhone());
            vo.setCreateTime(user.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return PageResult.build(
                pageInfo.getTotal(),
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                voList
        );
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
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
    public int update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}