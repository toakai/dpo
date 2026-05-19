package net.p5w.dp.service;

import net.p5w.dp.entity.UfContract;
public interface UfContractService{

    int insertSelective(UfContract record);

    UfContract selectByPrimaryKey(Integer id);

}
