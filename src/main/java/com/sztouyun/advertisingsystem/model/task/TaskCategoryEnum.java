package com.sztouyun.advertisingsystem.model.task;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.service.task.ICreateTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.CreateAdvertisementNotActivatedTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.CreateAdvertisementNotDisplayTaskOperationService;

public enum TaskCategoryEnum implements EnumMessage<Integer> {
    AdvertisementNotPlay(1,"未上架",TaskTypeEnum.Advertisement,TaskTypeEnum.Store, 20, CreateAdvertisementNotDisplayTaskOperationService.class),
    AdvertisementNotActivated(2,"未激活",TaskTypeEnum.Advertisement,TaskTypeEnum.Store, 1, CreateAdvertisementNotActivatedTaskOperationService.class);

    private Integer value;
    private String displayName;
    private TaskTypeEnum taskType;
    private TaskTypeEnum subTaskType;
    private int priority =1;
    private Class<? extends ICreateTaskOperationService> taskOperationService;

    TaskCategoryEnum(Integer value, String displayName, TaskTypeEnum taskType,Class<? extends ICreateTaskOperationService> taskOperationService) {
        this.value = value;
        this.displayName = displayName;
        this.taskType = taskType;
        this.taskOperationService = taskOperationService;
    }

    TaskCategoryEnum(Integer value, String displayName, TaskTypeEnum taskType, TaskTypeEnum subTaskType, int priority, Class<? extends ICreateTaskOperationService> taskOperationService) {
        this.value = value;
        this.displayName = displayName;
        this.taskType = taskType;
        this.subTaskType = subTaskType;
        this.taskOperationService = taskOperationService;
        this.priority = priority;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public TaskTypeEnum getSubTaskType() {
        return subTaskType;
    }

    public Class<? extends ICreateTaskOperationService> getTaskOperationService() {
        return taskOperationService;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
