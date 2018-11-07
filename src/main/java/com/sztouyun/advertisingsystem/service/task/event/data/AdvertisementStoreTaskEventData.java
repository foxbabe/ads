package com.sztouyun.advertisingsystem.service.task.event.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import lombok.Data;

/**
 * Created by wenfeng on 2018/4/8.
 */
@Data
public class AdvertisementStoreTaskEventData extends BaseTaskEventData {
    private Advertisement advertisement;
    private StoreInfo storeInfo;
}
