package com.ljj.mapper;

import com.ljj.entity.OperateLog;
import com.ljj.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

public interface OperateLogMapper {

    /**
     * 插入操作日志
     * @param log
     * @return
     */
    List<Permission> insertOperateLog(OperateLog log);

}
