package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.PartnerDailyStoreMonitorStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.PartnerMapper;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigGroupEnum;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerProfitConfigInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerProfitConfigInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.partner.UpdateDailyProfitAmountViewModel;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartnerStoreScheduledTask extends BaseScheduledTask{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private PartnerMapper partnerMapper;
    @Autowired
    private PartnerDailyStoreMonitorStatisticMapper partnerDailyStoreMonitorStatisticMapper;

    @Scheduled(cron = "${update.partner.daily.profit.jobs.cron.minute}")
    public void calculatePartnerStoreProfit() {
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(PartnerStoreScheduledTask.class.getName());
        DateTime taskStartTime = DateTime.now();
        String remark ="";
        try {
            PartnerProfitConfigInfoRequest request = new PartnerProfitConfigInfoRequest();
            request.setGroup(HistoricalParamConfigGroupEnum.PartnerProfitMode.getValue());
            request.setPageIndex(0);
            request.setPageSize(999);
            List<PartnerProfitConfigInfo> list = partnerMapper.getPartnerProfitConfigInfo(request);

            list.forEach(info -> {
                String partnerId = info.getObjectId();
                Double value = info.getValue() * 100;//把金额"元" 转为 "分"

                HistoricalParamConfigTypeEnum historicalParamConfigTypeEnum = EnumUtils.toEnum(info.getType(), HistoricalParamConfigTypeEnum.class);
                switch (historicalParamConfigTypeEnum) {
                    case PARTNER_PROFIT_ECPM: value = value / 1000; break; //计算公式 = 有效次数/1000 * 单价
                }
                UpdateDailyProfitAmountViewModel viewModel = new UpdateDailyProfitAmountViewModel(partnerId, value, LocalDate.now().minusDays(1).toDate());
                partnerDailyStoreMonitorStatisticMapper.updatePartnerDailyProfitAmount(viewModel);
            });
            remark = "合作方门店每日收益计算成功";
        } catch (Exception e) {
            logger.error("合作方门店每日收益计算失败",e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "合作方门店每日收益计算失败";
            scheduledJob.setSuccessed(false);
        }
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("合作方门店每日收益计算完成,"+ taskInfo);
        scheduledJob.setRemark(remark+taskInfo);
        scheduledJobRepository.save(scheduledJob);
    }
}
