package com.sztouyun.advertisingsystem.utils;

import java.lang.reflect.ParameterizedType;

public class ReflectUtil {

    /**
     * 获取某个类型泛型参数的对应的实例
     * @param clazz 类型
     * @param index 第几个泛型参数
     * @param <T>   泛型参数类型
     * @return 泛型参数的实例
     */
    public static <T> T newGenericInstance(Class<?> clazz,int index){
        try {
            Class<T> genericClazz = getGenericClass(clazz,index);
            return genericClazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取某个类型泛型参数的对应Class类型
     * @param clazz 类型
     * @param index 第几个泛型参数
     * @param <T>   泛型参数类型
     * @return 泛型参数的Class类型
     */
    public static <T> Class<T> getGenericClass(Class<?> clazz, int index){
        try {
            ParameterizedType pt = (ParameterizedType) clazz.getGenericSuperclass();
            return  (Class<T>) pt.getActualTypeArguments()[index];
        } catch (Exception e) {
            return null;
        }
    }

    public static Object newInstance(Class<?> clazz){
        try{
            return clazz.newInstance();
        }catch (Exception e){
            return null;
        }
    }
}
