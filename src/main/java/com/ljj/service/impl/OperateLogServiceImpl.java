package com.ljj.service.impl;

import com.ljj.mapper.OperateLogMapper;
import com.ljj.entity.OperateLog;
import com.ljj.entity.Permission;
import com.ljj.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("operateLogService")

public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public List<Permission> insertOperateLog(OperateLog log) {
        return operateLogMapper.insertOperateLog(log);
    }
}
