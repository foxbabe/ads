package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.mapper.TaskMapper;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.task.CancelAdvertisementTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelAdvertisementTaskOperation implements IActionOperation<Advertisement> {
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public void operateAction(Advertisement advertisement) {
        String statusName = EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(), AdvertisementStatusEnum.class);
        String remark = "广告名称为“"+advertisement.getAdvertisementName()+"”的广告，广告状态从投放中变更为"+statusName+"，当前广告下，所有未完成的任务，均自动取消";

        CancelAdvertisementTaskInfo info = new CancelAdvertisementTaskInfo();
        info.setAdvertisementId(advertisement.getId());
        info.setRemark(remark);
        taskMapper.cancelTaskByAdvertisement(info);
    }
}