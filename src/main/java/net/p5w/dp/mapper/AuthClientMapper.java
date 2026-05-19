package net.p5w.dp.mapper;

import net.p5w.dp.entity.AuthClient;

public interface AuthClientMapper {
    AuthClient selectByPrimaryKey(Long id);

    int updateByPrimaryKey(AuthClient record);

    AuthClient getByAppKey(String appKey);
}