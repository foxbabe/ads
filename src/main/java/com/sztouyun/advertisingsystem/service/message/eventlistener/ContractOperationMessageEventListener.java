package com.sztouyun.advertisingsystem.service.message.eventlistener;

import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.model.message.MessageCategoryEnum;
import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.service.message.event.ContractOperationEvent;
import com.sztouyun.advertisingsystem.service.message.eventlistener.base.BaseMessageEventListener;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.message.advertisement.AdvertisementInfo;
import com.sztouyun.advertisingsystem.viewmodel.message.contract.ContractMessageViewModel;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContractOperationMessageEventListener extends BaseMessageEventListener<ContractOperationEvent,ContractOperationLog,ContractMessageViewModel> {

    private Map<ContractOperationEnum, MessageCategoryEnum[]> contractCategoryEnumMapping = new HashMap<ContractOperationEnum, MessageCategoryEnum[]>() {
        {
            put(ContractOperationEnum.Submit, new MessageCategoryEnum[]{MessageCategoryEnum.SubmitContract, MessageCategoryEnum.SubmitContract});
            put(ContractOperationEnum.Auditing, new MessageCategoryEnum[]{MessageCategoryEnum.AuditContractSucceed, MessageCategoryEnum.AuditContractFailed});
            put(ContractOperationEnum.Sign, new MessageCategoryEnum[]{MessageCategoryEnum.SignContractSucceed, MessageCategoryEnum.SignContractFailed});
            put(ContractOperationEnum.SubmitTermination, new MessageCategoryEnum[]{MessageCategoryEnum.SubmitTerminatedAuditContract, MessageCategoryEnum.SubmitTerminatedAuditContract});
            put(ContractOperationEnum.TerminationAuditing, new MessageCategoryEnum[]{MessageCategoryEnum.TerminatedAuditContractSucceed, MessageCategoryEnum.TerminatedAuditContractFailed});
            put(ContractOperationEnum.Finish, new MessageCategoryEnum[]{MessageCategoryEnum.FinishedContract, MessageCategoryEnum.TerminateContract});
        }
    };

    @Override
    protected String getObjectId(ContractOperationLog contractOperationLog) {
        return contractOperationLog.getId();
    }

    @Override
    protected MessageTypeEnum getMessageType(ContractOperationLog contractOperationLog) {
        return MessageTypeEnum.Contract;
    }

    @Override
    protected MessageCategoryEnum getMessageCategory(ContractOperationLog contractOperationLog) {
        if(!contractCategoryEnumMapping.containsKey(contractOperationLog.getContractOperationEnum()))
            return null;
        MessageCategoryEnum[] messageCategoryEnums = contractCategoryEnumMapping.get(contractOperationLog.getContractOperationEnum());
        return messageCategoryEnums[contractOperationLog.isSuccessed()? 0 :1];
    }

    @Override
    protected ContractMessageViewModel getMessageViewModel(ContractOperationLog contractOperationLog) {
        ContractMessageViewModel contractMessageViewModel = (ContractMessageViewModel)getMessageCategory(contractOperationLog).getMessageViewModel();
        contractMessageViewModel.setContractId(contractOperationLog.getContractId());
        contractMessageViewModel.setContractName(contractOperationLog.getContract().getContractName());
        contractMessageViewModel.setConnectedAdvertisements(Linq4j.asEnumerable(contractOperationLog.getUnfinishedAdvertisements()).where(a-> !a.getId().equals(contractOperationLog.getAdvertisementId())).select(a->{
            AdvertisementInfo advertisementInfo =new AdvertisementInfo();
            advertisementInfo.setAdvertisementId(a.getId());
            advertisementInfo.setAdvertisementName(a.getAdvertisementName());
            return advertisementInfo;
        }).toList());
        return contractMessageViewModel;
    }

    @Override
    protected List<String> getMessageReceiverIds(ContractOperationLog contractOperationLog) {
        List<String> receiverIds= new ArrayList<>();
        receiverIds.add(contractOperationLog.getContract().getOwnerId());
        return receiverIds;
    }

    @Override
    protected boolean isCreateMessage(ContractOperationLog contractOperationLog) {
        switch (EnumUtils.toEnum(contractOperationLog.getOperation(), ContractOperationEnum.class)) {
            case BeginExecute: return false;
            case StageFinish: return false;
            case Finish:
                if(contractOperationLog.isSuccessed()&&(contractOperationLog.getUnfinishedAdvertisements()==null||Linq4j.asEnumerable(contractOperationLog.getUnfinishedAdvertisements()).toList().size()==0)) {
                    return false;
                }
            default: return true;
        }
    }
}
