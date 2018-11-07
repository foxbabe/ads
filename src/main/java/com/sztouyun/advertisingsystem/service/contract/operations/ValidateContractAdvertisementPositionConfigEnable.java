package com.sztouyun.advertisingsystem.service.contract.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.SystemParamTypeEnum;
import com.sztouyun.advertisingsystem.model.system.QSystemParamConfig;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.repository.system.SystemParamConfigRepository;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ValidateContractAdvertisementPositionConfigEnable implements IActionOperation<Contract> {
    @Autowired
    private SystemParamConfigRepository systemParamConfigRepository;

    private static final QSystemParamConfig qSystemParamConfig = QSystemParamConfig.systemParamConfig;

    @Override
    public void operateAction(Contract contract) {
        List<String> unEnablePositionIds = systemParamConfigRepository.findAll(q -> q.select(qSystemParamConfig.id).from(qSystemParamConfig).where(qSystemParamConfig.type.eq(SystemParamTypeEnum.AdvertisementPositionType.getValue()).and(qSystemParamConfig.enabled.isFalse())));
        Map<String, ContractAdvertisementPositionConfig> positionConfigMap = Linq4j.asEnumerable(contract.getContractExtension().getContractAdvertisementPositionConfigs()).toMap(positionConfig -> positionConfig.getSystemParamAdvertisementPositionId(), positionConfig -> positionConfig);
        unEnablePositionIds.forEach(unEnablePositionId -> {
            if (positionConfigMap.keySet().contains(unEnablePositionId)) {
                ContractAdvertisementPositionConfig contractAdvertisementPositionConfig = positionConfigMap.getOrDefault(unEnablePositionId, null);
                if (contractAdvertisementPositionConfig != null) {
                    String terminalName = EnumUtils.toEnum(contractAdvertisementPositionConfig.getTerminalType(), TerminalTypeEnum.class).getDisplayName();
                    String positionTypeName = EnumUtils.toEnum(contractAdvertisementPositionConfig.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName();
                    throw new BusinessException(String.format("当前系统中不存在%s广告位，请在%s终端下，取消勾选%s广告位", positionTypeName, terminalName, positionTypeName));
                }
            }
        });
    }
}
