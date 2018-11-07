package com.sztouyun.advertisingsystem.service.task;

public interface ICreateTaskOperationService<TData> {
    boolean createTask(TData data);
}
