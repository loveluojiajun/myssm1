package com.ljj.service;

import com.ljj.entity.OperateLog;
import com.ljj.entity.Permission;
import com.ljj.entity.User;

import java.util.List;

public interface OperateLogService {

    /**
     * 插入操作日志
     * @param log
     * @return
     */
    List<Permission> insertOperateLog(OperateLog log);
}
