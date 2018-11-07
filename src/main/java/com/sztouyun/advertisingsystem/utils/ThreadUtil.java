package com.sztouyun.advertisingsystem.utils;

/**
 * Created by wenfeng on 2017/9/14.
 */
public class ThreadUtil {
    public static  void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }
}
