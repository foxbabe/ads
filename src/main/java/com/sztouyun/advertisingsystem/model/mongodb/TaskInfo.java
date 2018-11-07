package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;

@Data
public class TaskInfo {
    public TaskInfo() {
    }

    public TaskInfo(String taskId, String content) {
        this.taskId = taskId;
        this.content = content;
    }
    private String taskId;
    private String content;
}
