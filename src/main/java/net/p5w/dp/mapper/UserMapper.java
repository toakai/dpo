package net.p5w.dp.mapper;

import net.p5w.dp.common.query.UserQuery;
import net.p5w.dp.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {

    /**
     * 删除用户（根据ID）
     */
    int deleteById(Long id);

    /**
     * 新增用户
     */
    int insert(User record);

    /**
     * 根据ID查询用户
     */
    User getById(Long id);

    /**
     * 根据ID更新用户（传对象）
     */
    int updateById(User user);

    /**
     * 查询全部用户（PageHelper 分页专用）
     */
    List<User> list();

    List<User> selectUserList(UserQuery query);
}