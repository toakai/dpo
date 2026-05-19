package net.p5w.dp.service.impl;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import net.p5w.dp.mapper.NonceMapper;
import net.p5w.dp.entity.Nonce;
import net.p5w.dp.service.NonceService;

@Service
public class NonceServiceImpl implements NonceService {

    @Autowired
    private NonceMapper nonceMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return nonceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Nonce record) {
        return nonceMapper.insert(record);
    }

    @Override
    public Nonce selectByPrimaryKey(Long id) {
        return nonceMapper.selectByPrimaryKey(id);
    }
    
}
