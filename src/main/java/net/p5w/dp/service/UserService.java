package net.p5w.dp.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import net.p5w.dp.common.query.UserOrderQuery;
import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.entity.User;
import net.p5w.dp.vo.UserOrderVO;
import net.p5w.dp.vo.UserVO;

/**
 * 用户 Service 接口
 */
public interface UserService {

    /**
     * 分页查询用户列表（返回脱敏 VO，对外展示）
     *
     * @param query 分页及筛选条件
     * @return 分页结果
     */
    PageInfo<UserVO> page(UserQuery query);

    /**
     * 分页查询用户+嵌套订单列表（一对多）
     *
     * @param query 分页及筛选条件
     * @return 分页结果，每个用户携带其订单列表
     */
    PageInfo<UserOrderVO> pageWithOrders(UserOrderQuery query);

    /**
     * 分页查询用户列表（返回完整实体，内部使用）
     *
     * @param query 分页及筛选条件
     * @return 分页结果
     */
    PageInfo<User> list(UserQuery query);

    /**
     * 查询所有用户（不分页）
     *
     * @return 用户列表
     */
    List<User> list();

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户主键
     * @return 用户信息，不存在则返回 null
     */
    User getById(Long id);

    /**
     * 保存用户（有 id 则更新，无 id 则新增）
     *
     * @param user 用户信息
     */
    void save(User user);

    /**
     * 根据 ID 删除用户
     *
     * @param id 用户主键
     */
    void delete(Long id);
}
