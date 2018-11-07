package com.sztouyun.advertisingsystem.service.task;


import com.sztouyun.advertisingsystem.common.operation.BaseValidationOperationService;

public abstract class BaseTaskOperationService<TData extends BaseTaskData>  extends BaseValidationOperationService<TData> {
    protected abstract  String getObjectId(TData data);
    protected abstract  String getSubObjectId(TData data);
}
