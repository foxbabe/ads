package com.sztouyun.advertisingsystem.service.message.eventlistener;

import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.message.MessageCategoryEnum;
import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.service.message.event.AdvertisementOperationEvent;
import com.sztouyun.advertisingsystem.service.message.eventlistener.base.BaseMessageEventListener;
import com.sztouyun.advertisingsystem.viewmodel.message.advertisement.AdvertisementMessageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdvertisementOperationMessageEventListener extends BaseMessageEventListener<AdvertisementOperationEvent,AdvertisementOperationLog,AdvertisementMessageViewModel> {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;


    private Map<AdvertisementOperationEnum, MessageCategoryEnum[]> advertisementMessageEnumMapping = new HashMap<AdvertisementOperationEnum, MessageCategoryEnum[]>() {
        {
            put(AdvertisementOperationEnum.Submit, new MessageCategoryEnum[]{MessageCategoryEnum.SubmitAdvertisement,MessageCategoryEnum.SubmitAdvertisement});
            put(AdvertisementOperationEnum.Auditing, new MessageCategoryEnum[]{MessageCategoryEnum.FinishAuditingAdvertisement,MessageCategoryEnum.RejectAuditingAdvertisement});
            put(AdvertisementOperationEnum.Delivery, new MessageCategoryEnum[]{MessageCategoryEnum.DeliveryAdvertisement,MessageCategoryEnum.DeliveryAdvertisement});
            put(AdvertisementOperationEnum.Finish, new MessageCategoryEnum[]{MessageCategoryEnum.AutoTakeOffAdvertisementAndFinishContract,MessageCategoryEnum.ManualTakeOffAdvertisementAndFinishContract,MessageCategoryEnum.ManualTakeOffAdvertisement,MessageCategoryEnum.FinishAdvertisement});
        }
    };

    @Override
    protected String getObjectId(AdvertisementOperationLog advertisementOperationLog) {
        return advertisementOperationLog.getId();
    }

    @Override
    protected MessageTypeEnum getMessageType(AdvertisementOperationLog advertisementOperationLog) {
        return MessageTypeEnum.Advertisement;
    }

    @Override
    protected MessageCategoryEnum getMessageCategory(AdvertisementOperationLog advertisementOperationLog) {
        AdvertisementOperationEnum advertisementOperationEnum = advertisementOperationLog.getAdvertisementOperationEnum();
        MessageCategoryEnum[] messageCategoryEnums = advertisementMessageEnumMapping.get(advertisementOperationEnum);
        switch (advertisementOperationEnum){
            case Submit:
            case Auditing:
            case Delivery:
                return messageCategoryEnums[advertisementOperationLog.isSuccessed()?0:1];
            case Finish:
                return messageCategoryEnums[advertisementOperationLog.isSuccessed()?3:(advertisementOperationLog.isAutoTakeOff()?0:(advertisementOperationLog.isFinishContract()?1:2))];
        }
        return null;
    }

    @Override
    protected AdvertisementMessageViewModel getMessageViewModel(AdvertisementOperationLog advertisementOperationLog) {
        AdvertisementMessageViewModel advertisementMessageViewModel = new AdvertisementMessageViewModel();
        Advertisement advertisement = advertisementRepository.findOne(qAdvertisement.id.eq(advertisementOperationLog.getAdvertisementId()), new JoinDescriptor().innerJoin(qAdvertisement.contract));
        Contract contract = advertisement.getContract();
        advertisementMessageViewModel.setContractId(contract.getId());
        advertisementMessageViewModel.setContractName(contract.getContractName());
        advertisementMessageViewModel.setAdvertisementId(advertisement.getId());
        advertisementMessageViewModel.setAdvertisementName(advertisement.getAdvertisementName());
        advertisementMessageViewModel.setEffectivePeriod(advertisement.getAdvertisementPeriod()<contract.getContractAdvertisementPeriod());
        advertisementMessageViewModel.setAffectOtherAdvertisements(advertisementOperationLog.isAffectOtherAdvertisements());
        return advertisementMessageViewModel;
    }

    @Override
    protected List<String> getMessageReceiverIds(AdvertisementOperationLog advertisementOperationLog) {
        List<String> receiverIds = new ArrayList<>();
        Advertisement advertisement = advertisementRepository.findOne(qAdvertisement.id.eq(advertisementOperationLog.getAdvertisementId()), new JoinDescriptor().innerJoin(qAdvertisement.contract));
        Contract contract = advertisement.getContract();
        receiverIds.add(contract.getOwnerId());
        return receiverIds;
    }

    @Override
    protected boolean isCreateMessage(AdvertisementOperationLog advertisementOperationLog) {
        return !advertisementOperationLog.getChangeByContract();
    }

}
