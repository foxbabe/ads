package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.TimeSpan;
import com.sztouyun.advertisingsystem.mapper.StoreDailyStatisticMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreDailyStatistic;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.repository.adPosition.AdPositionRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.thirdpart.IThirdPart;
import com.sztouyun.advertisingsystem.thirdpart.StoreSourceImplFactory;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOrderRequest;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class StoreDailyStatisticScheduledTask extends BaseScheduledTask{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private StoreDailyStatisticMapper storeDailyStatisticMapper;
    @Autowired
    private AdPositionRepository adPositionRepository;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Scheduled(cron = "${store.daily.statistic.task.time}")
    public void getStoreDailyStatisticTask() {
        DateTime taskStartTime = DateTime.now();
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(StoreDailyStatisticScheduledTask.class.getName());
        String remark;
        try {
            Date lastSucceedDate = getLastSucceedDate(StoreDailyStatisticScheduledTask.class.getName());
            LocalDate executeDate = lastSucceedDate == null ? LocalDate.now().minusDays(1) : new LocalDate(lastSucceedDate);
            while (executeDate.isBefore(LocalDate.now())) {
                Date date = executeDate.toDate();
                saveStoreDailyStatistics(date,StoreSourceEnum.NEW_OMS);
                executeDate = executeDate.plusDays(1);
            }
            remark = "门店日统计数据获取成功";
        } catch (Exception e) {
            logger.error("门店日统计数据获取失败",e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "门店日统计数据获取失败";
            scheduledJob.setSuccessed(false);
        }
        String taskInfo ="门店日统计数据获取用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("门店日统计数据获取成功,"+ taskInfo);
        scheduledJob.setRemark(remark+taskInfo);
        scheduledJobRepository.save(scheduledJob);

    }

    public void saveStoreDailyStatistics(Date date,StoreSourceEnum storeSourceEnum) {
        int pageSize = 100, pageIndex = 0;
        while (true) {
            Pageable pageable = new MyPageRequest(pageIndex, pageSize);
            List<StoreInfo> storeInfoList = storeInfoRepository.findAll(q->
                    q.selectFrom(qStoreInfo)
                            .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(storeSourceEnum.getValue())))
                            .offset(pageable.getOffset()).limit(pageable.getPageSize()));
            if (storeInfoList == null || storeInfoList.isEmpty())
                break;
            try {
                saveStoreDailyStatistics(storeInfoList,date,StoreSourceEnum.NEW_OMS);
            } catch (Exception e) {
                logger.error("计算第"+pageIndex+"页数据发生异常",e);
            }
            pageIndex++;
        }
    }

    public void saveStoreDailyStatistics(List<StoreInfo> storeInfos, Date date,StoreSourceEnum storeSourceEnum) {
        if(storeInfos.isEmpty())
            return;
        try {
            IThirdPart thirdPartInterface = StoreSourceImplFactory.getInstance(storeSourceEnum);
            List<String> storeNos  =Linq4j.asEnumerable(storeInfos).select(s->s.getStoreNo()).toList();
            Map<String,Integer>  storeOrderCountMap = thirdPartInterface.getStoresDailyOrder(new StoreOrderRequest(date,storeNos));
            Map<String,TimeSpan> openingTimeMap =  thirdPartInterface.getStoreOpeningTimes(new StoreOpeningTimeRequest(date,storeNos));
            List<StoreDailyStatistic> resultList = new ArrayList<>();

            Map<Integer, Integer> adPositionMap = Linq4j.asEnumerable(adPositionRepository.findAll()).toMap(a -> a.getStoreType(), b -> b.getAdCount());
            for (StoreInfo storeInfo :storeInfos){
                StoreDailyStatistic storeDailyStatistic = new StoreDailyStatistic();
                storeDailyStatistic.setDate(date);
                storeDailyStatistic.setAvailable(storeInfo.isAvailable());
                storeDailyStatistic.setStoreId(storeInfo.getId());
                storeDailyStatistic.setOrderCount(storeOrderCountMap.getOrDefault(storeInfo.getStoreNo(),0));
                TimeSpan timeSpan = openingTimeMap.getOrDefault(storeInfo.getStoreNo(),new TimeSpan());
                storeDailyStatistic.setOpeningTimeBegin(timeSpan.getStartTime());
                storeDailyStatistic.setOpeningTimeEnd(timeSpan.getEndTime());
                storeDailyStatistic.setUsedAdPositionCount(storeInfo.getUsedCount());
                storeDailyStatistic.setTotalAdPositionCount(adPositionMap.getOrDefault(storeInfo.getStoreType(), 0));
                resultList.add(storeDailyStatistic);
            }
            storeDailyStatisticMapper.saveStoreDailyStatistics(resultList);
        } catch (Exception e) {
            logger.error("获取门店日统计数据失败, 获取时间为: " + DateUtils.getCurrentFormat(), e);
        }
    }
}
