package com.sztouyun.advertisingsystem.common.thread;

import lombok.experimental.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ThreadPool<T,TResult> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConcurrentLinkedQueue<T> queue;
    private final int threadCount;
    private final ExecutorService threadPool;
    private Function<T,TResult> task;
    private boolean isStopped = false;

    public ThreadPool(Collection<T> dataList){
        this(dataList,5);
    }

    public ThreadPool(Collection<T> dataList,int threadCount){
        threadCount = Math.min(threadCount,dataList.size());
        this.queue = new ConcurrentLinkedQueue<>(dataList);
        this.threadCount = threadCount;
        threadPool = Executors.newFixedThreadPool(threadCount);
    }

    public List<TResult> invokeAll(){
        List<TResult> resultList = new ArrayList<>();
        try{
            var futureList = threadPool.invokeAll(getInvokeAllTasks()) ;
            for (var future: futureList){
                resultList.addAll(future.get());
            }
        }catch ( Exception e){
            logger.error("线程池invokeAll任务异常",e);
        }
        stop();
        return resultList;
    }

    public TResult invokeAny(){
        return invokeAny(Objects::nonNull);
    }

    public TResult invokeAny(Function<TResult,Boolean> breakFunc){
        TResult result = null;
        try{
            result = threadPool.invokeAny(getInvokeAnyTasks(breakFunc)) ;
        }catch ( Exception e){
            logger.error("线程池invokeAny任务异常",e);
        }
        stop();
        return result;
    }

    private List<Callable<List<TResult>>> getInvokeAllTasks(){
        List<Callable<List<TResult>>> tasks= new ArrayList<>();
        for (int i=0;i<threadCount;i++) {
            tasks.add(() -> {
                List<TResult> list = new ArrayList<>();
                T data;
                do {
                    data = this.queue.poll();
                    if(data == null)
                        break;
                    try {
                        TResult result = task.apply(data);
                        if(result !=null){
                            list.add(result);
                        }
                    }catch (Exception e){
                        logger.error("线程池执行单个任务异常",e);
                    }
                } while (!isStopped);
                return list;
            });
        }
        return tasks;
    }

    private List<Callable<TResult>> getInvokeAnyTasks(Function<TResult,Boolean> breakFunc){
        List<Callable<TResult>> tasks= new ArrayList<>();
        for (int i=0;i<threadCount;i++) {
            tasks.add(() -> {
                T data;
                TResult result = null;
                do {
                    data = this.queue.poll();
                    if(data == null){
                        Thread.currentThread().wait();
                        continue;
                    }
                    try {
                        result = task.apply(data);
                    }catch (Exception e){
                        logger.error("线程池执行单个任务异常",e);
                    }
                } while (!isStopped && !breakFunc.apply(result));
                return result;
            });
        }
        return tasks;
    }

    public void stop(){
        isStopped = true;
        threadPool.shutdown();
    }

    public ThreadPool<T,TResult> setTask(Function<T, TResult> task) {
        this.task = task;
        return this;
    }

    public void addData(T data){
        this.queue.offer(data);
    }
}
