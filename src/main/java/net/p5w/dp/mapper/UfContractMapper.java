package net.p5w.dp.mapper;

import net.p5w.dp.common.annotation.DataSource;
import net.p5w.dp.entity.UfContract;

public interface UfContractMapper {

    @DataSource("ecology-data-source")
    int insertSelective(UfContract record);

    @DataSource("ecology-data-source")
    UfContract selectByPrimaryKey(Integer id);
}