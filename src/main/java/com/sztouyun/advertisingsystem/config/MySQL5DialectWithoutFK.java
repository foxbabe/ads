package com.sztouyun.advertisingsystem.config;

/**
 * Created by Riber on 2017/8/7 0007.
 */
public class MySQL5DialectWithoutFK extends MySQL5DialectFunctionRegister {
    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        return "" ;
    }
}
