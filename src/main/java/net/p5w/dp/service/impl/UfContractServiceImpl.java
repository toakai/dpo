package net.p5w.dp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.p5w.dp.entity.UfContract;
import net.p5w.dp.mapper.UfContractMapper;
import net.p5w.dp.service.UfContractService;

@Service
public class UfContractServiceImpl implements UfContractService {

    @Autowired
    private UfContractMapper ufContractMapper;

    @Override
    public int insertSelective(UfContract record) {
        return ufContractMapper.insertSelective(record);
    }

    @Override
    public UfContract selectByPrimaryKey(Integer id) {
        return ufContractMapper.selectByPrimaryKey(id);
    }

}
