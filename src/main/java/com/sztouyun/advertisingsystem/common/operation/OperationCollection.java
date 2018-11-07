package com.sztouyun.advertisingsystem.common.operation;


import com.sztouyun.advertisingsystem.utils.SpringUtil;
import org.apache.calcite.linq4j.Linq4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OperationCollection<TData,TResult> implements IOperationCollection<TData,TResult> {
    private List<OperationInfo> operationInfos = new ArrayList<>();
    private Function<TResult,Boolean> breakFunc;

    public OperationCollection(Function<TResult,Boolean> breakFunc) {
        this.breakFunc = breakFunc;
    }

    @Override
    public <TOperation extends IOperation<TData, TResult>> OperationCollection add(Class<TOperation> operationClass) {
        TOperation operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos.add(new OperationInfo<>(operation,null));
        }
        return this;
    }

    @Override
    public <TOperationData, TOperation extends IOperation<TOperationData, TResult>> OperationCollection add(Class<TOperation> operationClass, TOperationData operationData) {
        TOperation operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos.add(new OperationInfo<>(operation ,operationData));
        }
        return this;
    }

    @Override
    public <TOperation extends IOperation> OperationCollection remove(Class<TOperation> operationClass) {
        IOperation operation = SpringUtil.getBean(operationClass);
        if(operation !=null){
            operationInfos = Linq4j.asEnumerable(operationInfos).where(info->!info.getOperation().equals(operation)).toList();
        }
        return this;
    }

    @Override
    public void clear() {
        operationInfos = new ArrayList<>();
    }

    @Override
    public List<TResult> operate(TData data) {
        List<TResult> resultList = new ArrayList<>();
        for (OperationInfo operationInfo : operationInfos){
            TResult result = execute(data,operationInfo);
            resultList.add(result);
            if(breakFunc.apply(result))
                break;
        }
        return resultList;
    }

    private TResult execute(TData data,OperationInfo operationInfo) {
        return (TResult) operationInfo.getOperation().operate(operationInfo.getData()==null ? data : operationInfo.getData());
    }
}


