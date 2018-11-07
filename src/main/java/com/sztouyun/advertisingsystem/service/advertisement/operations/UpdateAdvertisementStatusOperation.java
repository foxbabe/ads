package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateAdvertisementStatusOperation implements IActionOperation<AdvertisementOperationLog> {
    @Autowired
    private AdvertisementRepository advertisementRepository;


    private Map<AdvertisementOperationEnum, AdvertisementStatusEnum[]> advertisementOperationEnumMapping = new HashMap<AdvertisementOperationEnum, AdvertisementStatusEnum[]>() {
        {
            put(AdvertisementOperationEnum.Submit, new AdvertisementStatusEnum[]{AdvertisementStatusEnum.PendingAuditing, AdvertisementStatusEnum.PendingCommit});
            put(AdvertisementOperationEnum.Auditing, new AdvertisementStatusEnum[]{AdvertisementStatusEnum.PendingDelivery, AdvertisementStatusEnum.PendingCommit});
            put(AdvertisementOperationEnum.Delivery, new AdvertisementStatusEnum[]{AdvertisementStatusEnum.Delivering, AdvertisementStatusEnum.PendingCommit});
            put(AdvertisementOperationEnum.Finish, new AdvertisementStatusEnum[]{AdvertisementStatusEnum.Finished, AdvertisementStatusEnum.TakeOff});
        }
    };


    @Override
    public void operateAction(AdvertisementOperationLog advertisementOperationLog) {
        Advertisement advertisement =advertisementOperationLog.getAdvertisement();
        if(advertisement ==null){
            advertisement =advertisementRepository.findOne(advertisementOperationLog.getAdvertisementId());
        }
        AdvertisementOperationEnum operationEnum = advertisementOperationLog.getAdvertisementOperationEnum();
        //更新广告状态
        AdvertisementStatusEnum advertisementStatusEnum = advertisementOperationEnumMapping.get(operationEnum)[advertisementOperationLog.isSuccessed() ? 0 : 1];
        advertisement.setAdvertisementStatus(advertisementStatusEnum.getValue());
        advertisementRepository.saveAndFlush(advertisement);
    }
}
