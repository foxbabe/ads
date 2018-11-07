package com.sztouyun.advertisingsystem.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ObjectMapperUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object object){
        try {
            return mapper.writeValueAsString(object);
        }catch (Exception e){
            return "";
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz,String pattern) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(pattern));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(json, new TypeReference<T>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonToObject(String json,Class<?> collectionClass, Class<?>... elementClasses) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
