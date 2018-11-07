package com.sztouyun.advertisingsystem.common.operation;

import java.util.List;

public interface IOperationCollection<TData,TResult>{
    <TOperation extends IOperation<TData,TResult>> OperationCollection add(Class<TOperation> operationClass);
    <TOperationData,TOperation extends IOperation<TOperationData,TResult>> OperationCollection add(Class<TOperation> operationClass,TOperationData operationData);
    <TOperation extends IOperation> OperationCollection remove(Class<TOperation> operationClass);
    void clear();
    List<TResult> operate(TData data);
}
