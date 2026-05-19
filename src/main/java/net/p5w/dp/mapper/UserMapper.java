package net.p5w.dp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.p5w.dp.entity.User;

public interface UserMapper {

    int deleteById(Long id);

    int insert(User record);

    User getById(Long id);

    int updateById(Long id);

    List<User> list();

    List<User> selectUserPage(@Param("offset") long offset, @Param("size") long size);

    long selectUserCount();
}