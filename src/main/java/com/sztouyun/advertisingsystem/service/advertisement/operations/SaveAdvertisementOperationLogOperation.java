package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveAdvertisementOperationLogOperation implements IActionOperation<AdvertisementOperationLog> {
    @Autowired
    private AdvertisementOperationLogService advertisementOperationLogService;

    @Override
    public void operateAction(AdvertisementOperationLog advertisementOperationLog) {
        advertisementOperationLogService.saveAdvertisementOperationLog(advertisementOperationLog);
    }
}
