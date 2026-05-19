package net.p5w.dp.mapper;

import net.p5w.dp.entity.ApiCallLog;

public interface ApiCallLogMapper {
    int insert(ApiCallLog record);

    ApiCallLog selectByPrimaryKey(Long id);

    int updateByRequestId(ApiCallLog record);
}