package com.sztouyun.advertisingsystem.service.task.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IValidationOperation;
import com.sztouyun.advertisingsystem.model.task.Task;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.service.task.TaskService;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.DisplayDurationInfo;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateDisplayDurationOperation implements IValidationOperation<DisplayDurationInfo> {

    @Autowired
    private TaskService taskService;

    @Override
    public Boolean operate(DisplayDurationInfo displayDurationInfo) {
        LocalDateTime startDateTime = new LocalDateTime(displayDurationInfo.getStartDate());

        Task lastTask = taskService.getLastTask(displayDurationInfo.getAdvertisement().getId(), displayDurationInfo.getStoreInfo().getId());
        if(lastTask != null && lastTask.getTaskStatus().equals(TaskStatusEnum.Finished.getValue())) {
            startDateTime = new LocalDateTime(lastTask.getEndTime()).plusDays(1);
        }

        int days = new Period(startDateTime, new LocalDateTime(displayDurationInfo.getEndDate()), PeriodType.days()).getDays();
        return days >= displayDurationInfo.getNotDisplayDurationConfig();
    }
}
