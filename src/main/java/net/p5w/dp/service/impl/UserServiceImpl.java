package net.p5w.dp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.common.query.UserOrderQuery;
import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.entity.Order;
import net.p5w.dp.entity.User;
import net.p5w.dp.mapper.OrderMapper;
import net.p5w.dp.mapper.UserMapper;
import net.p5w.dp.service.UserService;
import net.p5w.dp.vo.OrderVO;
import net.p5w.dp.vo.UserOrderVO;
import net.p5w.dp.vo.UserVO;

/**
 * 用户 Service 实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderMapper orderMapper;

    /**
     * 分页查询用户（返回脱敏 VO，对外接口使用）
     */
    @Override
    public PageInfo<UserVO> page(UserQuery query) {
        log.info("分页查询用户VO：pageNum={}, pageSize={}", query.getPageNum(), query.getPageSize());

        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<User> userList = userMapper.selectUserList(query);
        PageInfo<User> entityPageInfo = new PageInfo<>(userList);

        List<UserVO> voList = userList.stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(u, vo);
            return vo;
        }).collect(Collectors.toList());

        return copyPageInfo(entityPageInfo, voList);
    }

    /**
     * 分页查询用户+嵌套订单列表（一对多）
     * <p>
     * 采用两步查询法避免 N+1 问题：
     * <ol>
     *   <li>PageHelper 分页查用户列表</li>
     *   <li>收集当前页用户 ID，批量查询订单</li>
     *   <li>按 userId 分组，组装到 UserOrderVO</li>
     * </ol>
     * </p>
     */
    @Override
    public PageInfo<UserOrderVO> pageWithOrders(UserOrderQuery query) {
        log.info("分页查询用户+订单：pageNum={}, pageSize={}, username={}",
                query.getPageNum(), query.getPageSize(), query.getUsername());

        // 第一步：分页查询用户
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        UserQuery userQuery = new UserQuery();
        userQuery.setUsername(query.getUsername());
        List<User> userList = userMapper.selectUserList(userQuery);
        PageInfo<User> entityPageInfo = new PageInfo<>(userList);

        // 第二步：批量查询当前页用户的订单
        List<Long> userIds = userList.stream().map(User::getId).collect(Collectors.toList());
        final Map<Long, List<OrderVO>> orderMap;
        if (!CollectionUtils.isEmpty(userIds)) {
            List<Order> orderList = orderMapper.selectByUserIds(userIds);
            orderMap = orderList.stream()
                    .collect(Collectors.groupingBy(
                            Order::getUserId,
                            Collectors.mapping(this::convertToOrderVO, Collectors.toList())
                    ));
        } else {
            orderMap = Collections.emptyMap();
        }

        // 第三步：组装 VO
        List<UserOrderVO> voList = userList.stream().map(user -> {
            UserOrderVO vo = new UserOrderVO();
            BeanUtils.copyProperties(user, vo);
            vo.setOrders(orderMap.getOrDefault(user.getId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());

        return copyPageInfo(entityPageInfo, voList);
    }

    /**
     * 将 PageInfo 的分页元数据复制到新的 PageInfo（用于 Entity → VO 转换后保持分页信息）
     */
    private <S, T> PageInfo<T> copyPageInfo(PageInfo<S> source, List<T> list) {
        PageInfo<T> target = new PageInfo<>(list);
        target.setTotal(source.getTotal());
        target.setPages(source.getPages());
        target.setPageNum(source.getPageNum());
        target.setPageSize(source.getPageSize());
        return target;
    }

    /**
     * Order 实体转 OrderVO
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        return vo;
    }

    /**
     * 分页查询用户（返回完整实体，内部管理接口使用）
     */
    @Override
    public PageInfo<User> list(UserQuery query) {
        log.info("分页查询用户：pageNum={}, pageSize={}", query.getPageNum(), query.getPageSize());

        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<User> userList = userMapper.selectUserList(query);
        return new PageInfo<>(userList);
    }

    /**
     * 查询全部用户（不分页）
     */
    @Override
    public List<User> list() {
        return userMapper.list();
    }

    /**
     * 根据 ID 查询用户
     */
    @Override
    public User getById(Long id) {
        return userMapper.getById(id);
    }

    /**
     * 保存用户：有 id 则更新，无 id 则新增
     */
    @Override
    public void save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }
    }

    /**
     * 根据 ID 删除用户
     */
    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
