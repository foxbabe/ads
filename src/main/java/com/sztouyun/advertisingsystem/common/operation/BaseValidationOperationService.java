package com.sztouyun.advertisingsystem.common.operation;

import java.util.List;
import java.util.function.Function;

public abstract class BaseValidationOperationService<TData>  extends BaseOperationService<TData,Boolean>{
    @Override
    protected Function<Boolean, Boolean> getBreakFunc() {
        return (result)->!result;
    }

    @Override
    protected Boolean getResult(List<Boolean> resultList) {
        for (Boolean result :resultList){
            if(!result)
                return false;
        }
        return true;
    }
}
