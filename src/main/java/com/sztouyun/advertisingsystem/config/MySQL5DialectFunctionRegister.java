package com.sztouyun.advertisingsystem.config;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * Created by wenfeng on 2017/9/13.
 */
public class MySQL5DialectFunctionRegister extends org.hibernate.dialect.MySQL5Dialect {
    public MySQL5DialectFunctionRegister(){
        super();
        registerFunction("DateDiff", new SQLFunctionTemplate(StandardBasicTypes.DATE, "DateDiff(?1,?2)"));
        registerFunction("DATE_SUB", new SQLFunctionTemplate(StandardBasicTypes.DATE, "DATE_SUB(?1,INTERVAL ?2 DAY)"));
        registerFunction("DATE_ADD", new SQLFunctionTemplate(StandardBasicTypes.DATE, "DATE_ADD(?1,INTERVAL ?2 DAY)"));
    }
}
