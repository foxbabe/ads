package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.job.AdvertisementProfitJobService;
import lombok.experimental.var;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 广告收益计算定时任务
 */
@Component
public class AdvertisementProfitScheduledTask extends BaseScheduledTask {
    @Autowired
    private AdvertisementProfitJobService advertisementProfitJobService;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    @Scheduled(cron = "${advertisement.store.profit.share.jobs.cron.minute}")
    public void calculateAdvertisementProfitShare() {
        AuthenticationService.setAdminLogin();
        DateTime taskStartTime = DateTime.now();
        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementProfitScheduledTask.class.getName());
        try{
            var date = LocalDate.now().plusDays(-1).toDate();
            advertisementProfitJobService.calculateAdvertisementDailyProfit(date);
        }catch (Exception ex){
            scheduledJob.setRemark("广告收益计算定时任务");
            scheduledJob.setSuccessed(false);
            logger.error("广告收益计算定时任务",ex);
        }
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("广告收益计算定时任务,"+ taskInfo);
        scheduledJob.setRemark(taskInfo);
        scheduledJobRepository.save(scheduledJob);
    }
}
