package com.sztouyun.advertisingsystem.common.operation;

import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

public abstract class BaseOperationService<TData,TResult> extends BaseService implements IOperation<TData,TResult>{
    protected abstract void onOperating(TData data,IOperationCollection<TData,TResult> operationCollection);

    protected void onOperated(TData data,IOperationCollection<TData,TResult> operationCollection){

    }

    protected TResult getResult(List<TResult> resultList){
        return null;
    }

    @Override
    @Transactional
    public final TResult operate(TData data) {
        return operate(data,null);
    }

    @Transactional
    public final TResult operate(TData data,Class<? extends IOperation>... ignoredOperationClasses) {
        IOperationCollection<TData,TResult> operationCollection = new OperationCollection<>(getBreakFunc());
        onOperating(data,operationCollection);
        onOperated(data,operationCollection);
        if(ignoredOperationClasses != null){
            for (Class<? extends IOperation> operation:ignoredOperationClasses){
                operationCollection.remove(operation);
            }
        }
        return getResult(operationCollection.operate(data));
    }

    protected Function<TResult,Boolean> getBreakFunc(){
        return (result)->false;
    }
}
