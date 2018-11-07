package com.sztouyun.advertisingsystem.scheduled;

import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.TimeSpan;
import com.sztouyun.advertisingsystem.mapper.*;
import com.sztouyun.advertisingsystem.model.adProfitShare.*;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.monitor.AdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.model.monitor.QAdvertisementDailyDeliveryMonitorStatistic;
import com.sztouyun.advertisingsystem.model.store.*;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.repository.adProfitShare.AdvertisementProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.StoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementDailyDeliveryMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreDailyStatisticRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.profitshare.ProfitShareCalculationOperationService;
import com.sztouyun.advertisingsystem.service.profitshare.StoreProfitShareValidationOperationService;
import com.sztouyun.advertisingsystem.service.profitshare.operations.Info.StoreValidationRuleInfo;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.ProfitShareCalculationData;
import com.sztouyun.advertisingsystem.service.profitshare.operations.data.StoreValidationRuleData;
import com.sztouyun.advertisingsystem.service.rule.RuleTypeEnum;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.thirdpart.IThirdPart;
import com.sztouyun.advertisingsystem.thirdpart.StoreSourceImplFactory;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoInActivityAdvertisementInfo;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOrderRequest;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvertisementProfitShareScheduledTask extends BaseScheduledTask {
    @Autowired
    private StoreProfitStatisticRepository storeProfitStatisticRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private HistoricalParamConfigService advertisementProfitShareConfigService;
    @Autowired
    private ProfitShareCalculationOperationService profitShareCalculationOperationService;
    @Autowired
    private AdvertisementProfitStatisticRepository advertisementProfitStatisticRepository;
    @Autowired
    private AdvertisementProfitStatisticMapper advertisementProfitStatisticMapper;
    @Autowired
    private StoreProfitStatisticMapper storeProfitStatisticMapper;
    @Autowired
    private StoreProfitShareValidationOperationService storeProfitShareValidationOperationService;
    @Autowired
    private PeriodStoreProfitStatisticMapper periodStoreProfitStatisticMapper;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private AdvertisementDailyDeliveryMonitorStatisticRepository advertisementDailyDeliveryMonitorStatisticRepository;
    @Autowired
    private StoreDailyStatisticRepository storeDailyStatisticRepository;
    @Autowired
    private StoreDailyStatisticMapper storeDailyStatisticMapper;

    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;
    private final QStoreProfitStatistic qStoreProfitStatistic = QStoreProfitStatistic.storeProfitStatistic;
    private final QAdvertisementDailyDeliveryMonitorStatistic qAdvertisementDailyDeliveryMonitorStatistic = QAdvertisementDailyDeliveryMonitorStatistic.advertisementDailyDeliveryMonitorStatistic;
    private final QStoreDailyStatistic qStoreDailyStatistic = QStoreDailyStatistic.storeDailyStatistic;



    @Scheduled(cron = "${store.profit.share.jobs.cron.minute}")
    public void calculateAdvertisementProfitShare() {
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementProfitShareScheduledTask.class.getName());
        String remark ="";
        int pageSize = 200, pageIndex = 0;
        DateTime taskStartTime = DateTime.now();
        try {
            Date lastSucceedDate = getLastSucceedDate(AdvertisementProfitShareScheduledTask.class.getName());
            LocalDate executeDate = lastSucceedDate == null ? LocalDate.now().minusDays(1) : new LocalDate(lastSucceedDate);
            while (executeDate.isBefore(LocalDate.now())) {
                Date date = executeDate.toDate();
                while (true) {
                    Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                    List<StoreInfo> storeInfoList = storeInfoRepository.findAll(q->
                            q.select(Projections.bean(StoreInfo.class, qStoreInfo.id, qStoreInfo.storeNo)).from(qStoreInfo)
                            .where(qStoreInfo.deleted.isFalse())
                            .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                    if (storeInfoList == null || storeInfoList.isEmpty())
                        break;

                    try{
                        calculateAdvertisementDailyProfitShare(date,storeInfoList);
                    }catch (Exception e){
                        logger.error("计算第"+pageIndex+"页数据发生异常",e);
                    }

                    pageIndex++;
                }
                executeDate = executeDate.plusDays(1);
		        pageIndex = 0;
            }
            remark = "门店收益计算定时任务成功";
        } catch (Exception e) {
            logger.error("门店收益计算定时任务失败",e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "门店收益计算定时任务失败";
            scheduledJob.setPageNum(pageIndex);
            scheduledJob.setSuccessed(false);
        }
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("门店收益计算定时任务成功,"+ taskInfo);
        scheduledJob.setRemark(remark+taskInfo);
        scheduledJobRepository.save(scheduledJob);
    }
    private void calculateAdvertisementDailyProfitShare(Date date, List<StoreInfo> storeInfoList){
        List<String> storeIds = Linq4j.asEnumerable(storeInfoList).select(a -> a.getId()).toList();

        Map<String, List<String>> activityAdvertisementInfo = getActivityAdvertisementInfo(date, storeIds);
        Map<String, StoreDailyStatistic> storeDailyStatisticMap = getStoreDailyStatisticWithStoreIdsOnDate(storeIds, date);

        for (StoreInfo storeInfo : storeInfoList) {
            List<String> activeAdvertisementIds = activityAdvertisementInfo.getOrDefault(storeInfo.getId(), new ArrayList<>());
            StoreDailyStatistic storeDailyStatistic = storeDailyStatisticMap.get(storeInfo.getId());
            TimeSpan timeSpan = storeDailyStatistic == null ? new TimeSpan() : new TimeSpan(storeDailyStatistic.getOpeningTimeBegin(), storeDailyStatistic.getOpeningTimeEnd());
            Double dailyOrderCount = storeDailyStatistic == null ? 0D : storeDailyStatistic.getOrderCount();
            try{
                generateStoreProfitStatistic(storeInfo, date, timeSpan,dailyOrderCount, activeAdvertisementIds);
            }catch (Exception ex){
                logger.error("生成门店收益错误："+storeInfo.getId(),ex);
            }
        }
    }

    public void generateStoreProfitStatistic(StoreInfo storeInfo, Date date, TimeSpan timeSpan,Double dailyOrderCount,List<String> activeAdvertisementIds) {
        DateTime taskStartTime = DateTime.now();
        StoreProfitStatistic storeProfitStatistic = new StoreProfitStatistic();
        StoreValidationRuleData ruleData =new StoreValidationRuleData(storeProfitStatistic.getId(),storeInfo,date);
        Double openingTime = timeSpan == null ? 0D :(timeSpan.getEndTime() -timeSpan.getStartTime())/3600000.0;
        ruleData.addRuleFactValue(RuleTypeEnum.ValidateStoreOpeningTime,openingTime);
        ruleData.addRuleFactValue(RuleTypeEnum.ValidateStoreOrder,dailyOrderCount);
        Boolean isQualifiedStore =  storeProfitShareValidationOperationService.operate(ruleData);
        storeProfitStatistic.setStoreId(storeInfo.getId());
        storeProfitStatistic.setStoreInfo(storeInfo);
        storeProfitStatistic.setProfitDate(date);
        storeProfitStatistic.setIsQualified(isQualifiedStore);
        storeProfitStatistic.setStoreProfitStatisticExtension(generateStoreProfitShareExtensionInfo(storeProfitStatistic, timeSpan,  ruleData.getRuleInfoList()));
        generateAdvertisementProfitStatistics(storeProfitStatistic, activeAdvertisementIds);
        storeProfitStatisticRepository.save(storeProfitStatistic);
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("门店收益计算定时任务成功,用时"+ taskInfo+",门店ID:"+storeInfo.getId());
    }

    private void generateAdvertisementProfitStatistics(StoreProfitStatistic storeProfitStatistic, List<String> activeAdvertisementIds) {
        advertisementMapper.getAdvertisementsByStore(new AdvertisementActivityInStoreRequest(storeProfitStatistic.getStoreId(), storeProfitStatistic.getProfitDate(), new LocalDate(storeProfitStatistic.getProfitDate()).plusDays(1).toDate())).forEach(advertisement -> {
            AdvertisementProfitStatistic advertisementProfitStatistic = new AdvertisementProfitStatistic();
            advertisementProfitStatistic.setStoreProfitStatisticId(storeProfitStatistic.getId());
            advertisementProfitStatistic.setStoreId(storeProfitStatistic.getStoreId());
            advertisementProfitStatistic.setContractId(advertisement.getContractId());
            advertisementProfitStatistic.setAdvertisementId(advertisement.getId());
            advertisementProfitStatistic.setProfitDate(storeProfitStatistic.getProfitDate());
            advertisementProfitStatistic.setActived(activeAdvertisementIds.contains(advertisement.getId()));
            advertisementProfitStatistic.setEnableProfitShare(advertisement.isEnableProfitShare());
            advertisementProfitStatistic.setIsQualified(storeProfitStatistic.getIsQualified() && advertisement.isEnableProfitShare() && advertisementProfitStatistic.isActived());
            storeProfitStatistic.setTotalAdvertisementCount(storeProfitStatistic.getTotalAdvertisementCount() + 1);
            if(advertisementProfitStatistic.getIsQualified()) {
                storeProfitStatistic.setEffectiveAdvertisementCount(storeProfitStatistic.getEffectiveAdvertisementCount() + 1);//门店有效广告数量递增
                //计算广告当天收益
                Double advertisementShareAmount = profitShareCalculationOperationService.operate(new ProfitShareCalculationData(
                        advertisementProfitStatistic.getId(), storeProfitStatistic.getProfitDate(), storeProfitStatistic.getStoreInfo(), advertisement
                ));
                advertisementProfitStatistic.setShareAmount(advertisementShareAmount);
                storeProfitStatistic.setShareAmount(storeProfitStatistic.getShareAmount() + advertisementShareAmount);//将当前广告收益累加到门店收益中
            }
            advertisementProfitStatisticRepository.save(advertisementProfitStatistic);
        });
    }



    private StoreProfitStatisticExtension generateStoreProfitShareExtensionInfo(StoreProfitStatistic storeProfitStatistic,TimeSpan timeSpan, List<StoreValidationRuleInfo> storeValidationRuleInfos) {
        StoreProfitStatisticExtension storeProfitStatisticExtension = new StoreProfitStatisticExtension();
        storeValidationRuleInfos.forEach(storeValidationRuleInfo -> {
            switch (EnumUtils.toEnum(storeValidationRuleInfo.getRuleType(), RuleTypeEnum.class)) {
                case ValidateStoreOrder://验证订单
                    storeProfitStatistic.setOrderStandardIs(storeValidationRuleInfo.getResult());
                    storeProfitStatisticExtension.setOrderNum(storeValidationRuleInfo.getFact());
                    storeProfitStatisticExtension.setOrderStandard(storeValidationRuleInfo.getStandard());
                    storeProfitStatisticExtension.setOrderUnit(storeValidationRuleInfo.getUnit());
                    storeProfitStatisticExtension.setOrderComparisonType(storeValidationRuleInfo.getComparisonType());
                    break;
                case ValidateStoreOpeningTime://验证每天开机时间
                    storeProfitStatistic.setOpeningTimeStandardIs(storeValidationRuleInfo.getResult());
                    storeProfitStatisticExtension.setOpeningTime(storeValidationRuleInfo.getFact());
                    storeProfitStatisticExtension.setOpeningTimeStandard(storeValidationRuleInfo.getStandard());
                    storeProfitStatisticExtension.setOpeningTimeUnit(storeValidationRuleInfo.getUnit());
                    storeProfitStatisticExtension.setOpeningTimeComparisonType(storeValidationRuleInfo.getComparisonType());

                    if(timeSpan.getStartTime() != null && timeSpan.getStartTime() != 0) {
                        storeProfitStatisticExtension.setOpeningTimeBegin(new Date(timeSpan.getStartTime()));
                    }
                    if (timeSpan.getEndTime() != null && timeSpan.getEndTime() != 0) {
                        storeProfitStatisticExtension.setOpeningTimeEnd(new Date(timeSpan.getEndTime()));
                    }
                    break;
            }
        });
        storeProfitStatisticExtension.setStoreProfitStatistic(storeProfitStatistic);
        return storeProfitStatisticExtension;
    }

    public void manualCalculateProfitShare(ManualInvokeProfitShareViewModel viewModel) {
        AuthenticationService.setAdminLogin();
        LocalDate startLocalDate = new LocalDate(viewModel.getStartDate());
        LocalDate endLocalDate = new LocalDate(viewModel.getEndDate());
        while (startLocalDate.isBefore(endLocalDate) || startLocalDate.equals(endLocalDate)) {
            Date date = startLocalDate.toDate();

            Map<String, List<String>> activityAdvertisementInfo = null;
            if (CollectionUtils.isEmpty(viewModel.getActiveAdvertisements())) {
                activityAdvertisementInfo = getActivityAdvertisementInfo(date, viewModel.getStoreIds());
            }
            List<String> storeProfitStatisticIds = storeProfitStatisticRepository.findAll(q -> q.select(qStoreProfitStatistic.id).from(qStoreProfitStatistic).where(qStoreProfitStatistic.profitDate.eq(date).and(qStoreProfitStatistic.storeId.in(viewModel.getStoreIds()))));
            if (!CollectionUtils.isEmpty(storeProfitStatisticIds)) {
                storeProfitStatisticMapper.deleteStoreProfitStatistic(storeProfitStatisticIds);
            }

            Iterable<StoreInfo> storeInfos = storeInfoRepository.findAll(q ->
                    q.select(Projections.bean(StoreInfo.class, qStoreInfo.id, qStoreInfo.storeNo)).from(qStoreInfo)
                            .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.id.in(viewModel.getStoreIds()))));

            Map<String, StoreDailyStatistic> storeDailyStatisticMap = getStoreDailyStatisticWithStoreIdsOnDate(Linq4j.asEnumerable(storeInfos).select(item -> item.getId()).toList(), date);
            for (StoreInfo storeInfo : storeInfos) {
                if (CollectionUtils.isEmpty(viewModel.getActiveAdvertisements())) {
                    viewModel.setActiveAdvertisements(activityAdvertisementInfo.getOrDefault(storeInfo.getId(), new ArrayList<>()));
                }
                StoreDailyStatistic storeDailyStatistic = storeDailyStatisticMap.get(storeInfo.getId());
                TimeSpan timeSpan = storeDailyStatistic == null ? new TimeSpan() : new TimeSpan(storeDailyStatistic.getOpeningTimeBegin(), storeDailyStatistic.getOpeningTimeEnd());
                Double dailyOrderCount = storeDailyStatistic == null ? 0D : storeDailyStatistic.getOrderCount();
                generateStoreProfitStatistic(storeInfo, date, timeSpan, dailyOrderCount, viewModel.getActiveAdvertisements());
            }
            startLocalDate = startLocalDate.plusDays(1);
        }
    }

    public void calculateProfitByAdvertisement(CalculateProfitByAdvertisementViewModel viewModel) {
        ScheduledJob scheduledJob = new ScheduledJob("根据广告计算门店收益");
        String remark;
        int pageSize = 100, pageIndex = 0;
        try {
            while (true) {
                DateTime taskStartTime = DateTime.now();
                CalculateProfitByAdvertisementPageRequest request = ApiBeanUtils.copyProperties(viewModel, CalculateProfitByAdvertisementPageRequest.class);
                request.setPageSize(pageSize);
                request.setPageIndex(pageIndex);
                List<String> storeIdList = storeInfoMapper.getStoreIdByAdvertisement(request);
                if (storeIdList == null || storeIdList.isEmpty())
                    break;

                ManualInvokeProfitShareViewModel manualInvokeProfitShareViewModel = ApiBeanUtils.copyProperties(viewModel, ManualInvokeProfitShareViewModel.class);
                manualInvokeProfitShareViewModel.setStoreIds(storeIdList);
                manualCalculateProfitShare(manualInvokeProfitShareViewModel);
                logger.info("页数:"+pageIndex+"手动计算门店收益计算任务成功,用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒");
                pageIndex++;
            }
            remark = "根据广告计算门店收益成功";
        } catch (Exception e) {
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setPageNum(pageIndex);
            remark = "根据广告计算门店收益失败";
            logger.error(remark,e);
            scheduledJob.setPageNum(pageIndex);
            scheduledJob.setSuccessed(false);
        }
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);
    }

    @Scheduled(cron = "${period.store.profit.share.jobs.cron.minute}")
    public void calculatePeriodStoreProfitStatisticTask() {
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(PeriodStoreProfitStatistic.class.getName());
        String remark ="";
        int pageSize = 500, pageIndex = 0;
        try {
            DateTime startDateTime = LocalDate.now().minusMonths(1).toDateTimeAtStartOfDay();
            Date startDate = startDateTime.dayOfMonth().withMinimumValue().toDate();

            DateTime endDateTime = LocalDate.now().toDateTimeAtStartOfDay();
            Date endDate = endDateTime.dayOfMonth().withMinimumValue().toDate();

            while (true) {
                Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                List<String> storeIds = storeInfoRepository.findAll(q ->
                        q.select(qStoreInfo.id).from(qStoreInfo)
                                .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue())))
                                .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                if (storeIds == null || storeIds.isEmpty())
                    break;
                calculatePeriodStoreProfitStatistic(startDate, endDate, storeIds);
                pageIndex++;
            }
            remark = "月收益流水定时任务执行成功";
        } catch (Exception e) {
            logger.error("月收益流水定时任务执行失败", e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setPageNum(pageIndex);
            remark = "月收益流水定时任务执行失败";
        }
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);
    }

    public void calculatePeriodStoreProfitStatistic(Date startDate, Date endDate, List<String> storeIds) {
        HistoricalParamConfig advertisementProfitShareConfig = advertisementProfitShareConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.FAULT_TOLERANCE, startDate);
        if (advertisementProfitShareConfig == null) {
            throw new RuntimeException("查询不到具体的容错率配置");
        }
        periodStoreProfitStatisticMapper.addPeriodStoreProfitShareStatistic(new PeriodStoreProfitShareCalculateViewModel(startDate, endDate, (advertisementProfitShareConfig.getValue()/ 100D), storeIds));
        storeProfitStatisticMapper.updatePeriodStoreProfitId(new PeriodStoreProfitShareCalculateViewModel(startDate, endDate, storeIds));
        advertisementProfitStatisticMapper.updateAdvertisementProfitShareAmount(new PeriodStoreProfitShareCalculateViewModel(startDate, endDate, storeIds));
        advertisementProfitStatisticMapper.updateAdvertisementStoreProfitShareAmount(new PeriodStoreProfitShareCalculateViewModel(startDate, endDate, storeIds));
    }

    public void reCalculateActiveAdvertisement(String advertisementId, Date startDate, Date endDate) {
        LocalDate startLocalDate = new LocalDate(startDate);
        LocalDate endLocalDate = new LocalDate(endDate);
        ScheduledJob scheduledJob = new ScheduledJob("重算广告收益的广告是否激活");
        try {
            while (startLocalDate.isBefore(endLocalDate) || startLocalDate.equals(endLocalDate)) {
                int pageIndex = 0;
                int pageSize = 500;
                while (true) {
                    Page<String> page = getStoreIdsByActivityAdvertisement(new StoreInfoInActivityAdvertisementInfo(advertisementId, startLocalDate.toDate(), pageIndex, pageSize));
                    if(page.getTotalElements() == 0) {
                        break;
                    }
                    advertisementProfitStatisticMapper.updateAdvertisementActive(new AdvertisementActiveUpdateRequest(advertisementId, page.getContent(), startLocalDate.toDate()));
                    ++pageIndex;
                }
                pageIndex = 0;
                startLocalDate = startLocalDate.plusDays(1);
            }
            scheduledJob.setRemark("重算广告收益的广告是否激活成功");
        } catch (Exception e) {
            logger.error("重算广告收益的广告是否激活失败", e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setRemark("重算广告收益的广告是否激活失败");
            scheduledJob.setSuccessed(Boolean.FALSE);
        }
        scheduledJobRepository.save(scheduledJob);
        logger.info("重算广告收益的广告是否激活成功");
    }

    public Map<String, StoreDailyStatistic> getStoreDailyStatisticWithStoreIdsOnDate(List<String> storeIds, Date date) {
        List<StoreDailyStatistic> storeDailyStatistics = storeDailyStatisticRepository.findAll(q -> q.selectFrom(qStoreDailyStatistic)
                .where(qStoreDailyStatistic.storeId.in(storeIds).and(qStoreDailyStatistic.date.eq(date))));
        return Linq4j.asEnumerable(storeDailyStatistics).toMap(a -> a.getStoreId(), a -> a);
    }

    public Map<String, List<String>> getActivityAdvertisementInfo(Date date, List<String> storeIds) {
        List<AdvertisementDailyDeliveryMonitorStatistic> advertisementDailyDeliveryMonitorStatistics = advertisementDailyDeliveryMonitorStatisticRepository
                .findAll(q -> q.selectFrom(qAdvertisementDailyDeliveryMonitorStatistic)
                .where(qAdvertisementDailyDeliveryMonitorStatistic.date.eq(date).and(qAdvertisementDailyDeliveryMonitorStatistic.storeId.in(storeIds)
                .and(qAdvertisementDailyDeliveryMonitorStatistic.displayTimes.gt(0))))
        );
        Enumerable<AdvertisementDailyDeliveryMonitorStatistic> advertisementEnumerable = Linq4j.asEnumerable(advertisementDailyDeliveryMonitorStatistics);
        return advertisementEnumerable.toMap(a -> a.getStoreId(), b -> advertisementEnumerable.where(c -> c.getStoreId().equals(b.getStoreId())).select(d -> d.getAdvertisementId()).distinct().toList());
    }

    public Page<String> getStoreIdsByActivityAdvertisement(StoreInfoInActivityAdvertisementInfo info) {
        Pageable pageable = new MyPageRequest(info.getPageIndex(), info.getPageSize());
        return advertisementDailyDeliveryMonitorStatisticRepository.findAll(q -> q.select(qAdvertisementDailyDeliveryMonitorStatistic.storeId).from(qAdvertisementDailyDeliveryMonitorStatistic)
                .where(qAdvertisementDailyDeliveryMonitorStatistic.advertisementId.eq(info.getAdvertisementId()).and(qAdvertisementDailyDeliveryMonitorStatistic.date.eq(info.getDate())))
                .groupBy(qAdvertisementDailyDeliveryMonitorStatistic.storeId), pageable);
    }

    @Scheduled(cron = "${store.profit.daily.order.count.jobs.cron.minute}")
    public void recalculateStoreDailyOrderCountInProfit() {
        AuthenticationService.setAdminLogin();
        DateTime taskStartTime = DateTime.now();
        ScheduledJob mainScheduledJob = new ScheduledJob(AdvertisementProfitShareScheduledTask.class.getName()+"每月重算门店日交易订单与更新分成");
        ScheduledJob supplementScheduledJob = new ScheduledJob(AdvertisementProfitShareScheduledTask.class.getName()+"门店订单补录");
        ScheduledJob profitScheduledJob = new ScheduledJob(AdvertisementProfitShareScheduledTask.class.getName()+"根据补录订单重算门店分成");
        String mainRemark = "主任务: 每月重算门店日交易订单与更新分成成功";
        LocalDate startLocalDate = new LocalDate().minusMonths(1).dayOfMonth().withMinimumValue();
        LocalDate endLocalDate = new LocalDate().minusMonths(1).dayOfMonth().withMaximumValue();
        try {
            int supplementPageSize = 200, supplementPageIndex = 0;
            try {//门店订单补录
                LocalDate executeDate = startLocalDate;
                while (executeDate.isBefore(endLocalDate) || executeDate.equals(endLocalDate)) {
                    while (true) {
                        Pageable pageable = new MyPageRequest(supplementPageIndex, supplementPageSize);
                        List<StoreInfo> storeInfos = storeInfoRepository.findAll(q ->
                                q.select(Projections.bean(StoreInfo.class, qStoreInfo.id, qStoreInfo.storeNo)).from(qStoreInfo)
                                        .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue())))
                                        .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                        if (storeInfos == null || storeInfos.isEmpty())
                            break;
                        try {
                            IThirdPart thirdPartInterface = StoreSourceImplFactory.getInstance(StoreSourceEnum.NEW_OMS);
                            List<String> storeNos  =Linq4j.asEnumerable(storeInfos).select(s->s.getStoreNo()).toList();
                            var date = executeDate.toDate();
                            Map<String,Integer>  storeOrderCountMap = thirdPartInterface.getStoresDailyOrder(new StoreOrderRequest(date,storeNos));
                            List<StoreDailyStatistic> resultList = new ArrayList<>();
                            for (StoreInfo storeInfo :storeInfos){
                                StoreDailyStatistic storeDailyStatistic = new StoreDailyStatistic();
                                storeDailyStatistic.setDate(date);
                                storeDailyStatistic.setStoreId(storeInfo.getId());
                                storeDailyStatistic.setOrderCount(storeOrderCountMap.getOrDefault(storeInfo.getStoreNo(),0));
                                if(storeDailyStatistic.getOrderCount() >0){
                                    resultList.add(storeDailyStatistic);
                                }
                            }
                            storeDailyStatisticMapper.saveStoreDailyStatistics(resultList);
                        } catch (Exception e) {
                            logger.error("获取门店日统计数据失败, 获取时间为: " + DateUtils.getCurrentFormat(), e);
                        }
                        supplementPageIndex++;
                    }
                    executeDate = executeDate.plusDays(1);
                    supplementPageIndex = 0;
                }
            } catch (Exception e) {
                supplementScheduledJob.setRemark("子任务: 补录订单计算失败");
                supplementScheduledJob.setPageNum(supplementPageIndex);
                logger.error("子任务: 补录订单计算失败",e);
                scheduledJobRepository.save(supplementScheduledJob);
                throw new RuntimeException("子任务: 补录订单计算失败", e);
            }
            supplementScheduledJob.setRemark("子任务: 补录订单计算成功");
            scheduledJobRepository.save(supplementScheduledJob);

            int profitPageSize = 500, profitPageIndex = 0;
            try {//根据补录订单, 重算门店分成和广告分成
                while (true) {
                    Pageable pageable = new MyPageRequest(profitPageIndex, profitPageSize);
                    List<String> storeIds = storeInfoRepository.findAll(q ->
                            q.select(qStoreInfo.id).from(qStoreInfo)
                                    .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue())))
                                    .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                    if (storeIds == null || storeIds.isEmpty())
                        break;
                    try {
                        recalculateProfitInfoBySupplementOrder(startLocalDate.toDate(), endLocalDate.toDate(), storeIds);
                    } catch (Exception e) {
                        logger.error("根据补录订单,重算门店分成和广告分成失败, 页数为: " +profitPageIndex+", 获取时间为: " + DateUtils.getCurrentFormat(), e);
                    }
                    profitPageIndex++;
                }
            } catch (Exception e) {
                profitScheduledJob.setRemark("子任务: 根据补录订单, 重算门店分成和广告分成失败");
                profitScheduledJob.setPageNum(profitPageIndex);
                logger.error("子任务: 根据补录订单, 重算门店分成和广告分成失败",e);
                scheduledJobRepository.save(profitScheduledJob);
                throw new RuntimeException("子任务: 根据补录订单, 重算门店分成和广告分成失败", e);
            }
            supplementScheduledJob.setRemark("子任务: 根据补录订单, 重算门店分成和广告分成成功");
            scheduledJobRepository.save(profitScheduledJob);
        } catch (Exception e) {
            logger.error("主任务: 每月重算门店日交易订单与更新分成失败", e);
            mainRemark = "主任务: 每月重算门店日交易订单与更新分成失败";
            mainScheduledJob.setSuccessed(false);
        }
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        mainScheduledJob.setRemark(mainRemark + taskInfo);
        scheduledJobRepository.save(mainScheduledJob);
    }

    @Transactional
    public void recalculateProfitInfoBySupplementOrder(Date startDate, Date endDate, List<String> storeIds) {
        HistoricalParamConfig dailyOrderConfig = advertisementProfitShareConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.DAILY_TRADING_ORDER_QUANTITY, endDate);
        SupplementProfitInfo supplementProfitInfo = new SupplementProfitInfo(startDate, endDate, dailyOrderConfig.getValue().intValue(), dailyOrderConfig.getComparisonType(), new LocalDate().toDate(), storeIds);
        //更新分成数据
        storeProfitStatisticMapper.supplementStoreProfitQualifiedInfo(supplementProfitInfo);
        advertisementProfitStatisticMapper.supplementAdvertisementProfitQualifiedInfo(supplementProfitInfo);
        storeProfitStatisticMapper.supplementStoreProfitAmountInfo(supplementProfitInfo);
        storeProfitStatisticMapper.supplementStoreProfitExtensionOrderInfo(supplementProfitInfo);
        storeProfitStatisticMapper.supplementStoreProfitOrderStandardIs(supplementProfitInfo);
    }
}