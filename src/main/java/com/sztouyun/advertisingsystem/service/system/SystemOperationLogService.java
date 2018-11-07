package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.model.system.QSystemOperationLog;
import com.sztouyun.advertisingsystem.model.system.SystemOperationLog;
import com.sztouyun.advertisingsystem.repository.system.SystemOperationLogRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemOperationLogService extends BaseService {

    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;

    private final QSystemOperationLog qSystemOperationLog=QSystemOperationLog.systemOperationLog;

    public SystemOperationLog findOne(Integer operation) {
        return systemOperationLogRepository.findOne(s->s.selectFrom(qSystemOperationLog).where(qSystemOperationLog.operation.eq(operation)).orderBy(qSystemOperationLog.createdTime.desc()));
    }
}
