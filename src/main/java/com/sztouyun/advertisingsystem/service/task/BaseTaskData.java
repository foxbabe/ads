package com.sztouyun.advertisingsystem.service.task;

import com.sztouyun.advertisingsystem.model.task.Task;
import lombok.Data;

import java.util.Date;

@Data
public class BaseTaskData {
    public BaseTaskData(Date date) {
        this.date = date;
    }
    private Date date;

    private Task lastTask;
}
