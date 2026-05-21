package net.p5w.dp.service;

import java.util.List;

import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.User;
import net.p5w.dp.vo.UserVO;

/**
 * 用户Service接口
 */
public interface UserService {

    /**
     * 分页查询用户列表（完整实体）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<User> getUserPage(Integer pageNum, Integer pageSize);

    /**
     * 分页查询用户列表（VO对象，对外展示）
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<UserVO> getUserVoPage(Integer pageNum, Integer pageSize);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> list();

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 保存用户（新增/修改）
     *
     * @param user 用户信息
     */
    void save(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void delete(Long id);

    PageResult<UserVO> page(UserQuery query);

    PageResult<User> list(UserQuery query);
}