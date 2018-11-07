package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import org.apache.calcite.linq4j.Linq4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class EnumUtils {
    private final static ConcurrentMap<String, Map<?,EnumMessage>> enumMessageMap = new ConcurrentHashMap<>();

    public static <T extends EnumMessage<S>,S> T toEnum(S value, Class<T> enumClass)  {
        if(value == null)
            return null;
        Map<?,EnumMessage>  enumValueMap = getEnumValueMap(enumClass);
        return (T)enumValueMap.get(value);
    }

    public static <T extends EnumMessage> T displayNameToEnum(String displayName, Class<T> enumClass)  {
        if(displayName == null)
            return null;

        Map<?,EnumMessage>  enumValueMap = getEnumValueMap(enumClass);
        for (EnumMessage enumMessage:enumValueMap.values()) {
            if(enumMessage.getDisplayName().equals(displayName))
                return (T)enumMessage;
        }
        return null;
    }

    public static <T extends EnumMessage<S>,S> String getDisplayName(S value, Class<T> enumClass)  {
        if(value == null)
            return "";
        T instance = toEnum(value,enumClass);
        if(instance == null)
            return "";
        return instance.getDisplayName();
    }

    public static <T extends EnumMessage<S>,S>  Map<S,String> toValueMap(Class<T> enumClass)  {
        Map<?,EnumMessage>  enumValueMap = getEnumValueMap(enumClass);
        Map<S,String> map=new HashMap<>();
        enumValueMap.forEach((value,enumMessage)-> map.put((S)value,enumMessage.getDisplayName()));
        return map;
    }

    public static <T extends EnumMessage<S>,S>  boolean isValidValue(S value, Class<T> enumClass)  {
        Map<?,EnumMessage>  enumValueMap = getEnumValueMap(enumClass);
        return enumValueMap.containsKey(value);
    }

    public static <T> List<T> getAllItems(Class<T> enumClass){
        Map<?,EnumMessage> enumValueMap = getEnumValueMap(enumClass);
        List<T> enumValues  = new ArrayList<>();
        enumValueMap.forEach((key,enumMessage)-> enumValues.add((T)enumMessage));
        return enumValues;
    }

    private static <T> Map<?,EnumMessage> getEnumValueMap(Class<T> enumClass){
        String name = enumClass.getName();
        Map<?,EnumMessage> enumValueMap=  enumMessageMap.get(name);
        if(enumValueMap != null)
            return enumValueMap;

        EnumMessage[] enumItems;
        try {
            Method method = enumClass.getMethod("values");
            enumItems=(EnumMessage[]) method.invoke(null, null);
        }catch (Exception ex){
            enumItems=new EnumMessage[0];
        }

        enumValueMap = Linq4j.asEnumerable(enumItems).toMap(e->e.getValue(),e->e);
        enumMessageMap.putIfAbsent(name,enumValueMap);
        return enumValueMap;
    }
}
