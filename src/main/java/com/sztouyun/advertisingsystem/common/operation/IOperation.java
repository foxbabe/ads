package com.sztouyun.advertisingsystem.common.operation;

public interface IOperation<TData,TResult>{
    TResult operate(TData data);
}
