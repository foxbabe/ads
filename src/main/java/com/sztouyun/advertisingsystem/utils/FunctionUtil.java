package com.sztouyun.advertisingsystem.utils;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by RiberLi on 2018/8/26 0026.
 */
public class FunctionUtil {
    public static <TData> Function<TData,Void> toFunction(Consumer<TData> task){
        return data->{
            task.accept(data);
            return null;
        };
    }
}
