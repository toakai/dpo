package net.p5w.dp.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.p5w.dp.entity.Nonce;
import net.p5w.dp.mapper.NonceMapper;
import net.p5w.dp.service.NonceService;

@Service
public class NonceServiceImpl implements NonceService {

    @Resource
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

    @Override
    public int deleteByCreateTimeBefore(Date threshold) {
        return nonceMapper.deleteByCreateTimeBefore(threshold);
    }
}
