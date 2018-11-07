package com.sztouyun.advertisingsystem.service.store;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.mongodb.StoreDeviceHeartbeat;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.job.StoreInfoServiceJob;
import com.sztouyun.advertisingsystem.utils.mongo.MongodbUtil;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartResult;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartViewModel;
import com.sztouyun.advertisingsystem.viewmodel.storeDevice.OpeningTimeTrendRequest;
import com.sztouyun.advertisingsystem.viewmodel.storeDevice.StoreDeviceOpeningDurationInfo;
import com.sztouyun.advertisingsystem.viewmodel.storeDevice.StoreDeviceOpeningDurationInfoResult;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by szty on 2018/8/8.
 */
@Service
public class StoreDeviceService extends BaseService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreInfoServiceJob storeInfoServiceJob;

    private static Map<String,Object[]> deviceOpeningDurationMap =new LinkedHashMap(){{
        put("0 - 4h",new Integer[]{0,4});
        put("4 - 6h",new Integer[]{4,6});
        put("6 - 8h",new Integer[]{6,8});
        put("8 - 10h",new Integer[]{8,10});
        put("10 - 12h",new Integer[]{10,12});
        put("12h以上",new Integer[]{12,24});
    }};

    public List<StoreDeviceOpeningDurationInfo> getStoreDeviceOpeningDurationInfo(Date date){
        Iterator<StoreDeviceOpeningDurationInfoResult> infoResultIterator= getStoreDeviceOpeningDurationInfoResult(date);
        Integer totalStoreCount=storeService.getStoreCount(date);
        Integer openingStoreCount=0;
        List<StoreDeviceOpeningDurationInfo> resultList=new ArrayList<>();
        Map<String,StoreDeviceOpeningDurationInfo> resultMap=new HashMap<>();
        while (infoResultIterator.hasNext()){
            StoreDeviceOpeningDurationInfo item=infoResultIterator.next().getValue();
            item.setTotalStoreCount(totalStoreCount);
            openingStoreCount+=item.getStoreCount();
            resultMap.put(item.getDurationType(),item);
        }
        Iterator<Map.Entry<String,Object[]>> iter = deviceOpeningDurationMap.entrySet().iterator();
        while (iter.hasNext()){
            String durationType=iter.next().getKey();
            StoreDeviceOpeningDurationInfo temp=null;
            if(resultMap.containsKey(durationType)){
                temp=resultMap.get(durationType);
                temp.setTotalStoreCount(totalStoreCount);
            }else{
                temp=new StoreDeviceOpeningDurationInfo(durationType,totalStoreCount);
            }
            if(durationType.equals("0 - 4h")){
                temp.updateStoreCount(totalStoreCount-openingStoreCount);
            }
            resultList.add(temp);
        }
        return resultList;
    }

    public List<StoreDeviceOpeningDurationInfo> getDailyOpeningTimeTrend(OpeningTimeTrendRequest request){
        Map<String,Integer[]> dailyOpeningTimeTrendMap=getOpeningTimeTrendDurationMap(request.getInterval());
        Iterator<StoreDeviceOpeningDurationInfoResult> infoResultIterator= getDailyOpeningTimeTrendResult(request,dailyOpeningTimeTrendMap);
        Integer totalStoreCount=storeService.getStoreCount(request.getDate());
        Map<String,StoreDeviceOpeningDurationInfo> resultMap=new HashMap<>();
        List<StoreDeviceOpeningDurationInfo> resultList=new ArrayList<>();
        while (infoResultIterator.hasNext()){
            StoreDeviceOpeningDurationInfo item=infoResultIterator.next().getValue();
            item.setTotalStoreCount(totalStoreCount);
            resultMap.put( item.getDurationType(),item);
        }

        Iterator<Map.Entry<String,Integer[]>> iter = dailyOpeningTimeTrendMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry<String,Integer[]> entry=iter.next();
            String key=entry.getKey();
            Integer[] value=entry.getValue();
            StoreDeviceOpeningDurationInfo temp=null;
            if(resultMap.containsKey(key)){
                temp=resultMap.get(key);
                temp.setDurationType(String.format(Constant.HourPeroidPattern,value));
            }else{
                temp=new StoreDeviceOpeningDurationInfo(String.format(Constant.HourPeroidPattern,value),totalStoreCount);
            }
            resultList.add(temp);
        }
        return resultList;
    }

    private Iterator<StoreDeviceOpeningDurationInfoResult> getStoreDeviceOpeningDurationInfoResult(Date date){
        Long datetime=new LocalDate(date).toDate().getTime();
        if(datetime.equals(Long.valueOf(LocalDate.now().toDate().getTime()))){
            storeInfoServiceJob.calcStoreOpenCloseTime(date,date);
        }
        MapReduceOptions mro=new MapReduceOptions();
        mro.outputTypeInline();
        mro.finalizeFunction("classpath:script/storeOpeningDuration/finalize.js");
        mro.scopeVariables(new HashMap(){{
            put("durationMap", deviceOpeningDurationMap);
        }});
        return mongoTemplate.mapReduce(
                new Query(Criteria.where("_id.createdDate").is(datetime)),
                "storeDeviceDailyStatisticResult",
                "classpath:script/storeOpeningDuration/mapFunc.js",
                "classpath:script/storeOpeningDuration/reduceFunc.js",
                mro,
                StoreDeviceOpeningDurationInfoResult.class
        ).iterator();
    }

    private Iterator<StoreDeviceOpeningDurationInfoResult> getDailyOpeningTimeTrendResult(OpeningTimeTrendRequest request,Map<String,Integer[]> inervalMap){
        Long datetime=new LocalDate(request.getDate()).toDate().getTime();
        if(datetime.equals(Long.valueOf(LocalDate.now().toDate().getTime()))){
            storeInfoServiceJob.calcStoreOpenCloseTime(request.getDate(),request.getDate());
        }
        MapReduceOptions mro=new MapReduceOptions();
        mro.outputTypeInline();
        mro.finalizeFunction("classpath:script/storeOpeningTimeTrend/finalize.js");
        mro.scopeVariables(new HashMap(){{
            put("intervalMap",inervalMap);
        }});
        return mongoTemplate.mapReduce(
                new Query(Criteria.where("_id.createdDate").is(datetime)),
                "storeDeviceDailyStatisticResult",
                "classpath:script/storeOpeningTimeTrend/mapFunc.js",
                "classpath:script/storeOpeningTimeTrend/reduceFunc.js",
                mro,
                StoreDeviceOpeningDurationInfoResult.class
        ).iterator();
    }

    private Map<String,Integer[]> getOpeningTimeTrendDurationMap(Integer interval){
        return new LinkedHashMap(){{
            for(int i=0;i<24;i+=interval){
                if(i==0){
                    put("0", i+interval>24?new Integer[]{i,24}:new Integer[]{i,i+interval});
                }else{
                    put(String.valueOf(i), i+interval>24?new Integer[]{i,24}:new Integer[]{i,i+interval});
                }

            }
        }};
    }

    public long getHasNetworkStoreTotal(Date date) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("createdDate").is(date.getTime())),
                group("createdDate", "storeId"),
                group("createdDate").count().as("count"),
                project("count"));
        return MongodbUtil.getCount(mongoTemplate, aggregation, StoreDeviceHeartbeat.class, Long.class);
    }

    public List<StoreNetworkPeriodIntervalChartViewModel> storeNetworkPeriodIntervalChart(StoreNetworkPeriodIntervalChartRequest request) {
        List<StoreNetworkPeriodIntervalChartViewModel> list = new ArrayList<>();
        Map<String,Integer[]> openingTimeTrendDurationMap = getOpeningTimeTrendDurationMap(request.getInterval());
        Map<String, StoreNetworkPeriodIntervalChartInfo> map = getStoreNetworkPeriodIntervalChartInfoMap(request,openingTimeTrendDurationMap);
        Integer totalStoreCount=storeService.getStoreCount(request.getDate());
        Iterator<Map.Entry<String, Integer[]>> iterator = getOpeningTimeTrendDurationMap(request.getInterval()).entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer[]> entry = iterator.next();
            StoreNetworkPeriodIntervalChartInfo storeNetworkPeriodIntervalChartInfo = map.get(entry.getKey());
            StoreNetworkPeriodIntervalChartViewModel viewModel= new StoreNetworkPeriodIntervalChartViewModel();
                viewModel.setInterval(String.format(Constant.HourPeroidPattern,entry.getValue()));
                viewModel.setStoreCount(totalStoreCount);
            if(storeNetworkPeriodIntervalChartInfo!=null){
                viewModel.setStoreCount4G(storeNetworkPeriodIntervalChartInfo.getStoreCount4G());
                viewModel.setStoreCountWIFI(storeNetworkPeriodIntervalChartInfo.getStoreCountWIFI());
                viewModel.setNetworkStoreCount(storeNetworkPeriodIntervalChartInfo.getNetworkStoreCount());
            }
            list.add(viewModel);
        }
        return  list;
    }

    private Map<String, StoreNetworkPeriodIntervalChartInfo> getStoreNetworkPeriodIntervalChartInfoMap(StoreNetworkPeriodIntervalChartRequest request,Map<String,Integer[]> intervalMap) {
        MapReduceOptions mro=new MapReduceOptions();
        mro.outputTypeInline();
        mro.finalizeFunction("classpath:script/storeNetworkPeriodInterval/finalize.js");
        mro.scopeVariables(new HashMap(){{
            put("intervalMap", intervalMap);
        }});
        Iterator<StoreNetworkPeriodIntervalChartResult> iterator = mongoTemplate.mapReduce(
                new Query(Criteria.where("createdDate").is(request.getDate().getTime())),
                "storeDeviceHeartbeat",
                "classpath:script/storeNetworkPeriodInterval/mapFunc.js",
                "classpath:script/storeNetworkPeriodInterval/reduceFunc.js",
                mro,
                StoreNetworkPeriodIntervalChartResult.class
        ).iterator();
        Map<String,StoreNetworkPeriodIntervalChartInfo> map = new HashMap<>();
        while (iterator.hasNext()){
            StoreNetworkPeriodIntervalChartResult result = iterator.next();
            map.put(String.valueOf(result.getValue().getInterval()),result.getValue());
        }
        return map;
    }

}

