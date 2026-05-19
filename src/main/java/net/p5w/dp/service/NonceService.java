package net.p5w.dp.service;

import net.p5w.dp.entity.Nonce;

public interface NonceService {

    int deleteByPrimaryKey(Long id);

    int insert(Nonce record);

    Nonce selectByPrimaryKey(Long id);

}
