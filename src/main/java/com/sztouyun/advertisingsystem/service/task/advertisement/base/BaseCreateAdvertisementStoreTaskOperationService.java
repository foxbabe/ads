package com.sztouyun.advertisingsystem.service.task.advertisement.base;


import com.sztouyun.advertisingsystem.model.task.Task;
import com.sztouyun.advertisingsystem.service.task.BaseCreateTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import com.sztouyun.advertisingsystem.service.task.event.AdvertisementStoreTaskEvent;
import com.sztouyun.advertisingsystem.service.task.event.data.AdvertisementStoreTaskEventData;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public abstract class BaseCreateAdvertisementStoreTaskOperationService extends BaseCreateTaskOperationService<AdvertisementStoreInfo>{
    @Override
    protected String getObjectId(AdvertisementStoreInfo data) {
        return data.getAdvertisement().getId();
    }

    @Override
    protected String getSubObjectId(AdvertisementStoreInfo data) {
        return data.getStoreInfo().getId();
    }

    @Override
    protected  void afterCreateTask(Task task,AdvertisementStoreInfo data){
        AdvertisementStoreTaskEventData taskEventData= ApiBeanUtils.copyProperties(task,AdvertisementStoreTaskEventData.class);
        taskEventData.setStoreInfo(data.getStoreInfo());
        taskEventData.setAdvertisement(data.getAdvertisement());
        taskEventData.setCreatedTime(data.getDate());
        taskEventData.setExpectedCompletionTime(task.getExpectedCompletionTime());
        taskEventData.setExpectedCompletionUnit(task.getExpectedCompletionUnit());
        taskEventData.setRemark(task.getRemark());
        publishEvent(new AdvertisementStoreTaskEvent(taskEventData));
    }

    protected String getAreaName(String areaId, Map<String,String> areaMap) {
        if (StringUtils.isEmpty(areaId) || !areaMap.keySet().contains(areaId))
            return "";
        return areaMap.get(areaId);
    }
}
