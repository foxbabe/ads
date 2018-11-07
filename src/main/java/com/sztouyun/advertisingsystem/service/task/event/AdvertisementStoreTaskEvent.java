package com.sztouyun.advertisingsystem.service.task.event;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.service.task.event.data.AdvertisementStoreTaskEventData;

/**
 * Created by wenfeng on 2018/4/3.
 */
public class AdvertisementStoreTaskEvent extends BaseEvent<AdvertisementStoreTaskEventData> {
    public AdvertisementStoreTaskEvent(AdvertisementStoreTaskEventData taskEventData) {
        super(taskEventData);
    }
}
