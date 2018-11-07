package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.repository.system.SystemOperationLogRepository;
import com.sztouyun.advertisingsystem.model.system.SystemParamTypeEnum;
import com.sztouyun.advertisingsystem.model.system.QSystemParamConfig;
import com.sztouyun.advertisingsystem.model.system.SystemParamConfig;
import com.sztouyun.advertisingsystem.repository.system.SystemParamConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.system.TerminalAndAdvertisementPositionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SystemParamConfigService extends BaseService{

    @Autowired
    private SystemParamConfigRepository systemParamConfigRepository;
    @Autowired
    private SystemOperationLogRepository systemOperationLogRepository;

    private final QSystemParamConfig qSystemParamConfig = QSystemParamConfig.systemParamConfig;

    public List<TerminalAndAdvertisementPositionInfo> getTerminalAndAdvertisementPositionInfos() {
        return ApiBeanUtils.convertToTreeList(getTerminalTypeAndAdvertisementPositionType(), l->{
            TerminalAndAdvertisementPositionInfo terminalAndAdvertisementPositionInfo = ApiBeanUtils.copyProperties(l, TerminalAndAdvertisementPositionInfo.class);
            terminalAndAdvertisementPositionInfo.setChecked(l.getEnabled());
            return  terminalAndAdvertisementPositionInfo;
        },SystemParamConfig.ROOT_PARENT_ID);
    }

    public List<SystemParamConfig> getTerminalTypeAndAdvertisementPositionType() {
        List<SystemParamConfig> systemParamConfigs = getSystemParamConfigs();
        systemParamConfigs.parallelStream().forEach(systemParamConfig -> systemParamConfig.setName(systemParamConfig.getParamValueEnum().getDisplayName()));
        return systemParamConfigs;
    }

    private List<SystemParamConfig> getSystemParamConfigs() {
        return systemParamConfigRepository.findAll(s -> s.selectFrom(qSystemParamConfig).where(qSystemParamConfig.type.in(SystemParamTypeEnum.TerminalType.getValue(), SystemParamTypeEnum.AdvertisementPositionType.getValue(), SystemParamTypeEnum.MaterialLinkType.getValue())).orderBy(qSystemParamConfig.id.asc()));
    }

    @Transactional
    public void updateSystemParamConfigService(List<String> ids) {
        ids.forEach(id->{
            if(systemParamConfigRepository.findOne(id)==null)
                throw new BusinessException("请求无效！");
        });
        getSystemParamConfigs().parallelStream().forEach(systemParamConfig->{
            systemParamConfig.setEnabled(ids.contains(systemParamConfig.getId()));
            systemParamConfig.setUpdatedTime(new Date());
        });
        systemOperationLogRepository.save(new SystemOperationLog(SystemOperationEnum.TerminalAndAdvertisementPosition.getValue()));
    }
}
