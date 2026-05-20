package net.p5w.dp.service;

import java.util.List;

import net.p5w.dp.common.result.PageResult;
import net.p5w.dp.entity.User;

public interface UserService {
    List<User> list();

    User getById(Long id);

    int update(User record);

    void delete(Long id);

    void save(User user);

    PageResult<User> getUserPage(Long page, Long size);
}