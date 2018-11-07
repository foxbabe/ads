package com.sztouyun.advertisingsystem.service.message.event;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;

public class AdvertisementOperationEvent extends BaseEvent<AdvertisementOperationLog> {

    public AdvertisementOperationEvent(AdvertisementOperationLog advertisementOperationLog) {
        super(advertisementOperationLog);
    }
}
