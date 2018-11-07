package com.sztouyun.advertisingsystem.common.operation;

/**
 * Created by RiberLi on 2018/1/8 0008.
 */
public interface IActionOperation<TData> extends IOperation<TData,Void> {
    @Override
    default Void operate(TData data){
        operateAction(data);
        return null;
    }

    void operateAction(TData data);
}
