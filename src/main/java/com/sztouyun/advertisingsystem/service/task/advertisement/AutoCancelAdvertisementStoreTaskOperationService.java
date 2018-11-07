package com.sztouyun.advertisingsystem.service.task.advertisement;


import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.service.task.BaseTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import com.sztouyun.advertisingsystem.service.task.advertisement.operations.AutoCancelActivatedAdvertisementStoreTaskOperation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AutoCancelAdvertisementStoreTaskOperationService extends BaseTaskOperationService<AdvertisementStoreInfo> {
    @Override
    protected String getObjectId(AdvertisementStoreInfo data) {
        return data.getAdvertisement().getId();
    }

    @Override
    protected String getSubObjectId(AdvertisementStoreInfo data) {
        return data.getStoreInfo().getId();
    }

    @Override
    protected void onOperating(AdvertisementStoreInfo advertisementStoreInfo, IOperationCollection<AdvertisementStoreInfo, Boolean> operationCollection) {
        operationCollection.add(AutoCancelActivatedAdvertisementStoreTaskOperation.class);
    }

    @Override
    protected Function<Boolean, Boolean> getBreakFunc() {
        return (result)->false;
    }
}
