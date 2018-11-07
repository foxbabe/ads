package com.sztouyun.advertisingsystem.utils.dataHandle;

/**
 * Created by fengwen on 22/08/2018.
 */
public abstract class AbstractDataHandle<IN extends DataInput,PO,R extends DataOutput> implements DataHandle<IN,PO,R>{
    public R execute(IN in){
        PO po=preProcess(in);
        validate(in,po);
        R r=process(in,po);
        after(in,po,r);
        return r;
    }
}
