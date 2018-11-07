package com.sztouyun.advertisingsystem.utils.dataHandle;

/**
 * Created by fengwen on 22/08/2018.
 */
public interface DataHandle <IN,PO,R>{
     PO preProcess(IN in);
     void validate(IN in,PO vi);
     R process(IN in,PO hi);
     void after(IN in,PO po,R r);

     R execute(IN in);

}
