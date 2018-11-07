package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.job.StoreSyncLog;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.job.StoreInfoServiceJob;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StoreInfoScheduledTask  extends BaseScheduledTask{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StoreInfoServiceJob storeInfoServiceJob;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Value("${store.info.jobs.initdate}")
    private String initDate;
    @Value("${store.new.info.jobs.initdate}")
    private String newStoreInitDate;


    @Scheduled(cron = "${store.info.jobs.cron.sys}")
    public void synchronizeStoreInfo() {
        logger.info("门店数据同步day【开始】，当前时间：" + DateUtils.getCurrentFormat());
        StoreSyncLog syncStoreLog = storeInfoServiceJob.findNewestLogBySuccess();
        if(syncStoreLog == null){
            recalculateStoreType(initDate);
        }else {
            storeInfoServiceJob.synchronizeStoreInfo(syncStoreLog.getEndDate(),DateUtils.getPureDate(new Date()));
        }
        //同步完成立刻计算门店类型
        calcStoreType();
        logger.info("门店数据同步day【结束】，当前时间：" + DateUtils.getCurrentFormat());
    }

    @Scheduled(cron = "${store.info.jobs.cron.calc}")
    public void calcStoreType() {
        logger.info("重算门店类型，当前时间：" + DateUtils.getCurrentFormat());
        storeInfoServiceJob.calcStoreType();
        logger.info("重算门店类型，当前时间：" + DateUtils.getCurrentFormat());
        scheduledJobRepository.save(new ScheduledJob("重算门店类型"));
    }

    public void  recalculateStoreType(String startDateString){
        Date startDate = DateUtils.strToDate(startDateString, "yyyy-MM-dd");
        storeInfoServiceJob.synchronizeStoreInfo(startDate,DateUtils.getPureDate(new Date()));
        calcStoreType();
    }

    public void  syncNewStoreInfo(){
        Date startDate = DateUtils.strToDate(newStoreInitDate, "yyyy-MM-dd");
        storeInfoServiceJob.synchronizeStoreInfo(startDate,new Date());
        calcStoreType();
    }

    public void manualSyncStorePortraitInfo(){
        Date startDate = DateUtils.strToDate(initDate, "yyyy-MM-dd");
        String jobName="SyncStorePortrait";
        storeInfoServiceJob.syncStorePortraitInfo(startDate,DateUtils.getPureDate(new Date()),jobName,200);
    }

    @Scheduled(cron = "${store.portrait.daily.sync.task.time}")
    public void autoSyncStorePortraitInfo(){
        logger.info("门店画像数据同步day【开始】，当前时间：" + DateUtils.getCurrentFormat());
        String jobName="SyncStorePortrait";
        StoreSyncLog syncStoreLog = storeInfoServiceJob.findNewestLogBySuccess(jobName);
        if(syncStoreLog==null){
            Date startDate = DateUtils.strToDate(initDate, "yyyy-MM-dd");
            storeInfoServiceJob.syncStorePortraitInfo(startDate,DateUtils.getPureDate(new Date()),jobName,200);
        }else{
            storeInfoServiceJob.syncStorePortraitInfo(syncStoreLog.getEndDate(),DateUtils.getPureDate(new Date()),jobName,200);
        }
        logger.info("门店画像数据同步day【结束】，当前时间：" + DateUtils.getCurrentFormat());
    }

    @Scheduled(cron = "${store.nearBy.sync.cron.minute}")
    public void syncStoreNearByInfo(){
        storeInfoServiceJob.syncStoreNearByInfo();
    }

}