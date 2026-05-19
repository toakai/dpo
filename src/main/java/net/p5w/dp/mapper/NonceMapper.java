package net.p5w.dp.mapper;

import net.p5w.dp.entity.Nonce;

public interface NonceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Nonce record);

    Nonce selectByPrimaryKey(Long id);

}