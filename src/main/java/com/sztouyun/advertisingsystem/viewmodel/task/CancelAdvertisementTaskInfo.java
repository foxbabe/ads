package com.sztouyun.advertisingsystem.viewmodel.task;

import com.sztouyun.advertisingsystem.model.task.TaskResultEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import lombok.Data;

@Data
public class CancelAdvertisementTaskInfo {
    private String remark;

    private String advertisementId;

    private Integer taskResult = TaskResultEnum.Resolved.getValue();

    private Integer taskStatus = TaskStatusEnum.Cancel.getValue();

    private String storeId;
}
