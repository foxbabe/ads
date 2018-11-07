package com.sztouyun.advertisingsystem.service.task.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IValidationOperation;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.TaskDurationInfo;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

//验证与上次的任务的时间间隔
@Service
public class ValidateLastTaskDurationOperation implements IValidationOperation<TaskDurationInfo> {
    @Override
    public Boolean operate(TaskDurationInfo taskDurationInfo) {
        if(taskDurationInfo.getLastTask() == null || taskDurationInfo.getLastTask().getEndTime() ==null)
            return true;

        return taskDurationInfo.getDate().after(new DateTime(taskDurationInfo.getLastTask().getEndTime()).plusDays(taskDurationInfo.getDays()).toDate());
    }
}
