package com.sztouyun.advertisingsystem.service.material;

import com.sztouyun.advertisingsystem.model.material.PartnerMaterialOperationEnum;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialOperationLog;
import com.sztouyun.advertisingsystem.model.material.QPartnerMaterialOperationLog;
import com.sztouyun.advertisingsystem.repository.material.PartnerMaterialOperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wenfeng on 2018/1/31.
 */
@Service
public class PartnerMaterialOperationLogService {
    @Autowired
    private PartnerMaterialOperationLogRepository partnerMaterialOperationLogRepository;
    private final static QPartnerMaterialOperationLog qPartnerMaterialOperationLog=QPartnerMaterialOperationLog.partnerMaterialOperationLog;
    public PartnerMaterialOperationLog findAuditLog(String partnerMaterialId){
        return partnerMaterialOperationLogRepository.findOne(qPartnerMaterialOperationLog.partnerMaterialId.eq(partnerMaterialId).and(qPartnerMaterialOperationLog.operation.eq(PartnerMaterialOperationEnum.Auditing.getValue())));
    }
}
