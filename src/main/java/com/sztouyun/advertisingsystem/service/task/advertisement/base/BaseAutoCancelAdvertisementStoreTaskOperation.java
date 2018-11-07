package com.sztouyun.advertisingsystem.service.task.advertisement.base;

import com.sztouyun.advertisingsystem.common.operation.IValidationOperation;
import com.sztouyun.advertisingsystem.mapper.TaskMapper;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.task.CancelAdvertisementTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseAutoCancelAdvertisementStoreTaskOperation<TData extends AdvertisementStoreInfo> implements IValidationOperation<TData> {
    public abstract Boolean validate(TData data);

    public abstract String getRemark(TData data);

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Boolean operate(TData data) {
        if(!validate(data))
            return false;

        CancelAdvertisementTaskInfo info = new CancelAdvertisementTaskInfo();
        info.setRemark(getRemark(data));
        info.setAdvertisementId(data.getAdvertisement().getId());
        info.setStoreId(data.getStoreInfo().getId());
        taskMapper.cancelTaskByAdvertisement(info);
        return true;
    }
}
