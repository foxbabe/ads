package com.sztouyun.advertisingsystem.common.operation;

public class OperationInfo<TData,TResult>{
    private IOperation<TData,TResult> operation;
    private TData data;

    public OperationInfo(IOperation<TData,TResult> operation, TData data) {
        this.operation = operation;
        this.data = data;
    }

    public IOperation<TData,TResult> getOperation() {
        return operation;
    }

    public void setOperation(IOperation<TData,TResult>operation) {
        this.operation = operation;
    }

    public TData getData() {
        return data;
    }

    public void setData(TData data) {
        this.data = data;
    }
}
