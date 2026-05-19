package net.p5w.dp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.p5w.dp.entity.ApiCallLog;
import net.p5w.dp.mapper.ApiCallLogMapper;
import net.p5w.dp.service.ApiCallLogService;

@Service
public class ApiCallLogServiceImpl implements ApiCallLogService {

    @Autowired
    private ApiCallLogMapper apiCallLogMapper;

    @Override
    public int insert(ApiCallLog record) {
        return apiCallLogMapper.insert(record);
    }

    @Override
    public ApiCallLog selectByPrimaryKey(Long id) {
        return apiCallLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByRequestId(ApiCallLog record) {
        return apiCallLogMapper.updateByRequestId(record);
    }
}
