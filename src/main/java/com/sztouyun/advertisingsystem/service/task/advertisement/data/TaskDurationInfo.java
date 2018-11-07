package com.sztouyun.advertisingsystem.service.task.advertisement.data;

import com.sztouyun.advertisingsystem.model.task.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDurationInfo {
    private Task lastTask;
    private Date date;
    private int days;
}
