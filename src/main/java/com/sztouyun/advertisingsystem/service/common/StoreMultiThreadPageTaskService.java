package com.sztouyun.advertisingsystem.service.common;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.thread.MultiThreadPageTaskService;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.utils.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 门店多线程分页任务处理
 */
@Component
public class StoreMultiThreadPageTaskService extends MultiThreadPageTaskService
{
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    public void runPageStoreIdTask(BooleanBuilder filter, Consumer<List<String>> task, int threadCount, int pageSize){
        runPageStoreIdTask(filter, FunctionUtil.toFunction(task),threadCount,pageSize);
    }

    public void runPageStoreInfoTask(BooleanBuilder filter, Consumer<List<StoreInfo>> task, int threadCount, int pageSize){
        runPageStoreInfoTask(filter, FunctionUtil.toFunction(task),threadCount,pageSize);
    }

     public <TResult> List<TResult> runPageStoreIdTask(BooleanBuilder filter, Function<List<String>,TResult> task,int threadCount,int pageSize){
         Function<Integer,List<String>> dataTask = pageIndex-> storeInfoRepository.findAll(q->
                 q.select(qStoreInfo.id).from(qStoreInfo).where(filter).orderBy(qStoreInfo.id.desc()).offset(pageSize*pageIndex).limit(pageSize));
         return runPageTask(filter,task,dataTask,threadCount,pageSize);
     }

    public <TResult> List<TResult> runPageStoreInfoTask(BooleanBuilder filter, Function<List<StoreInfo>,TResult> task, int threadCount, int pageSize){
        Function<Integer,List<StoreInfo>> dataTask = pageIndex-> storeInfoRepository.findAll(q->
                q.selectFrom(qStoreInfo).where(filter).orderBy(qStoreInfo.id.desc()).offset(pageSize*pageIndex).limit(pageSize));
        return runPageTask(filter,task,dataTask,threadCount,pageSize);
    }

    public <TData,TResult> List<TResult> runPageTask(BooleanBuilder filter, Function<List<TData>,TResult> task,Function<Integer,List<TData>> getPageListTask,int threadCount,int pageSize){
        long total =storeInfoRepository.count(filter);
        return super.runPageTask(task,getPageListTask,total,threadCount,pageSize);
    }
}
