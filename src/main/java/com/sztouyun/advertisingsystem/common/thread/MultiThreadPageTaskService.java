package com.sztouyun.advertisingsystem.common.thread;


import com.sztouyun.advertisingsystem.utils.FunctionUtil;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 多线程分页任务处理
 */
@Component
public class MultiThreadPageTaskService
{
    public <TData> void runPageTask(Consumer<List<TData>> task, Function<Integer,List<TData>> getPageListTask, long total, int threadCount, int pageSize){
        this.runPageTask(FunctionUtil.toFunction(task),getPageListTask,total,threadCount,pageSize);
    }

    public <TData,TResult> List<TResult> runPageTask(Function<List<TData>,TResult> task,Function<Integer,List<TData>> getPageListTask,long total,int threadCount,int pageSize){
        List<Integer> pageIndexList =getPageIndexList(total,pageSize);
        //一个线程则循环处理
        if(threadCount == 1)
            return Linq4j.asEnumerable(pageIndexList).select(pageIndex->task.apply(getPageListTask.apply(pageIndex))).toList();

        ThreadPool<Integer,TResult> threadPool = new ThreadPool<>(pageIndexList, threadCount);
        threadPool.setTask(pageIndex->{
            var dataList = getPageListTask.apply(pageIndex);
            return task.apply(dataList);
        });
        return threadPool.invokeAll();
    }

    private List<Integer> getPageIndexList(long total, int pageSize){
        List<Integer> pageIndexList = new ArrayList<>();
        int pageIndex = 0;
        while (pageIndex*pageSize <total){
            pageIndexList.add(pageIndex);
            pageIndex++;
        }
        return pageIndexList;
    }
}
