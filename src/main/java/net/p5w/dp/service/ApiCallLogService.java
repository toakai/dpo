package net.p5w.dp.service;

import net.p5w.dp.entity.ApiCallLog;

public interface ApiCallLogService {

    int insert(ApiCallLog record);

    ApiCallLog selectByPrimaryKey(Long id);

    int updateByRequestId(ApiCallLog record);
}


