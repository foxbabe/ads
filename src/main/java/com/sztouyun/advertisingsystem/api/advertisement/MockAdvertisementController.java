package com.sztouyun.advertisingsystem.api.advertisement;


import com.sztouyun.advertisingsystem.ResourceDownloadThread;
import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.TimeSpan;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PeriodStoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.StoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.TaskMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QPeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplate;
import com.sztouyun.advertisingsystem.model.contract.ContractTemplateTypeEnum;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.mongodb.profit.StoreDailyProfit;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.repository.adProfitShare.PeriodStoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.StoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.repository.order.OrderInfoRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.scheduled.AdvertisementDeliveryLogScheduledTask;
import com.sztouyun.advertisingsystem.scheduled.AdvertisementProfitShareScheduledTask;
import com.sztouyun.advertisingsystem.scheduled.AdvertisementTaskScheduledTask;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.adProfitShare.StoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.contract.ContractTemplateService;
import com.sztouyun.advertisingsystem.service.job.AdvertisementProfitJobService;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.service.task.TaskService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.CalculateProfitByAdvertisementViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.mock.ModifyAdvertisementStartDeliveryTimeRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.task.MockCompleteTaskRequest;
import com.sztouyun.advertisingsystem.viewmodel.mock.profitShare.MockPeriodStoreProfitShareViewModel;
import com.sztouyun.advertisingsystem.viewmodel.mock.profitShare.MockStoreProfitShareViewModel;
import com.sztouyun.advertisingsystem.viewmodel.mock.task.MockAdvertisementTaskViewModel;
import com.sztouyun.advertisingsystem.viewmodel.order.OrderMockOperationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Profile({"local","dev","test","stage"})
@Api("模拟广告接口")
@RestController
@RequestMapping("/api/mock/advertisement")
public class MockAdvertisementController  extends BaseApiController {
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private StoreProfitStatisticRepository storeProfitStatisticRepository;
    @Autowired
    private AdvertisementDeliveryLogScheduledTask advertisementDeliveryLogScheduledTask;
    @Autowired
    private AdvertisementProfitShareScheduledTask advertisementProfitShareScheduledTask;
    @Autowired
    private StoreProfitStatisticService storeProfitStatisticService;
    @Autowired
    private PeriodStoreProfitStatisticMapper periodStoreProfitStatisticMapper;
    @Autowired
    private PeriodStoreProfitStatisticRepository periodStoreProfitStatisticRepository;
    @Autowired
    private StoreProfitStatisticMapper storeProfitStatisticMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private AdvertisementTaskScheduledTask advertisementTaskScheduledTask;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ResourceDownloadThread resourceDownloadThread;
    @Autowired
    private ContractTemplateService contractTemplateService;
    @Autowired
    private PreviewConfig previewConfig;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final QStoreProfitStatistic qStoreProfitStatistic = QStoreProfitStatistic.storeProfitStatistic;
    private final QPeriodStoreProfitStatistic qPeriodStoreProfitStatistic = QPeriodStoreProfitStatistic.periodStoreProfitStatistic;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "修改执行中广告开始执行时间", notes = "创建者：李川")
    @RequestMapping(value = "/modifyStartDeliveryTime", method = RequestMethod.POST)
    public InvokeResult modifyAdvertisementStartTime(@Validated @RequestBody ModifyAdvertisementStartDeliveryTimeRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        Advertisement advertisement =advertisementRepository.findOne(request.getAdvertisementId());
        if(advertisement == null)
            return InvokeResult.Fail("广告不存在");
        if(!advertisement.getAdvertisementStatusEnum().equals(AdvertisementStatusEnum.Delivering))
            return InvokeResult.Fail("广告状态必须为投放中");
        Date effectiveStartTime =DateUtils.getDateAccurateToMinute(request.getStartDeliveryTime());
        advertisement.setEffectiveStartTime(effectiveStartTime);
        Contract contract =advertisement.getContract();
        Date expectedDueDay = DateUtils.addDays(effectiveStartTime,contract.getContractAdvertisementPeriod()-contract.getUsedContractPeriod());
        advertisement.setExpectedDueDay(expectedDueDay);
        advertisementRepository.save(advertisement);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "触发监控定时任务重算", notes = "创建者：王伟权")
    @GetMapping(value = "/modifyMonitorInfo")
    public InvokeResult modifyMonitorInfo() {
        AuthenticationService.setAdminLogin();
        long beginTime = advertisementDeliveryLogScheduledTask.getScheduleBeginTime();
        long endTime = LocalDateTime.now().toDate().getTime();

        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementDeliveryLogScheduledTask.class.getName() + "手动调用");
        StringBuffer remark=new StringBuffer();
        try {
            advertisementDeliveryLogScheduledTask.updateDeliveryMonitorTask(beginTime, endTime);
            remark.append("手动触发监控日志计算定时任务成功");
        } catch (Exception e) {
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            scheduledJob.setSuccessed(false);
            remark.append("#####手动触发监控日志计算定时任务失败#####");
            logger.error(e.getMessage(),e);
        }
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);

        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟门店收益计算", notes = "创建人：王伟权")
    @RequestMapping(value="reCalculateStoreProfitShare",method = RequestMethod.POST)
    public InvokeResult reCalculateStoreProfitShare(@Validated @RequestBody MockStoreProfitShareViewModel viewModel, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);

        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementProfitShareScheduledTask.class.getName() + "手动调用");
        String remark ="";

        LocalDate startDate = new LocalDate(viewModel.getStartDate());
        LocalDate endDate = new LocalDate(viewModel.getEndDate());

        try {
            while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
                Date date = startDate.toDate();

                List<StoreInfo> storeInfos = storeInfoRepository.findAll(viewModel.getStoreIds());
                if (storeInfos.size() != viewModel.getStoreIds().size()) {
                    return InvokeResult.Fail("门店数量不匹配");
                }
                for (StoreInfo storeInfo : storeInfos) {
                    String storeProfitStatisticId = storeProfitStatisticRepository.findOne(q -> q.select(qStoreProfitStatistic.id).from(qStoreProfitStatistic).where(qStoreProfitStatistic.storeId.eq(storeInfo.getId()).and(qStoreProfitStatistic.profitDate.eq(date))));
                    if (!org.springframework.util.StringUtils.isEmpty(storeProfitStatisticId)) {
                        storeProfitStatisticService.deleteStoreProfitStatistic(Arrays.asList(storeProfitStatisticId));
                    }

                    Date bootShutDownDate = startDate.toLocalDateTime(LocalTime.MIDNIGHT).plusMinutes((int)(viewModel.getBootTime()*60D)).toDate();//门店当天最后关机时间
                    advertisementProfitShareScheduledTask.generateStoreProfitStatistic(storeInfo, date, new TimeSpan(date.getTime(), bootShutDownDate.getTime()), viewModel.getOrderCount(), viewModel.getActiveAdvertisementIds());

                }
                startDate = startDate.plusDays(1);
            }
            remark = "手动调用门店收益计算定时任务成功";
        } catch (Exception e) {
            e.printStackTrace();
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "手动门店收益计算定时任务失败";
            scheduledJob.setSuccessed(false);
        }
        scheduledJob.setRemark(remark);
        scheduledJobRepository.save(scheduledJob);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟门店月收益计算", notes = "创建人：王伟权")
    @RequestMapping(value="reCalculatePeriodStoreProfitShare",method = RequestMethod.POST)
    public InvokeResult reCalculatePeriodStoreProfitShare(@Validated @RequestBody MockPeriodStoreProfitShareViewModel viewModel, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        ScheduledJob scheduledJob = new ScheduledJob(PeriodStoreProfitStatistic.class.getName() + "手动调用");
        String remark ="";
        try {
            viewModel.getMongth().forEach(month -> {
                Date startDate = new LocalDate(viewModel.getYear(), month, 1).toDateTimeAtStartOfDay().dayOfMonth().withMinimumValue().toDate();
                Date endDate = new LocalDate(viewModel.getYear(), month, 1).toDateTimeAtStartOfDay().dayOfMonth().withMinimumValue().plusMonths(1).toDate();
                List<String> periodStoreProfitStatisticIds = periodStoreProfitStatisticRepository.findAll(q -> q.select(qPeriodStoreProfitStatistic.id).from(qPeriodStoreProfitStatistic).where(qPeriodStoreProfitStatistic.settledMonth.eq(startDate)));
                if (!CollectionUtils.isEmpty(periodStoreProfitStatisticIds)) {
                    storeProfitStatisticMapper.clearPeriodStoreProfitId(periodStoreProfitStatisticIds);
                    periodStoreProfitStatisticMapper.deletePeriodStoreProfitShareStatistic(periodStoreProfitStatisticIds);
                }

                int pageSize = 500, pageIndex = 0;
                while (true) {
                    Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                    List<String> storeIds = storeInfoRepository.findAll(q ->
                            q.select(qStoreInfo.id).from(qStoreInfo)
                                    .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue())))
                                    .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                    if (storeIds == null || storeIds.isEmpty())
                        break;
                    advertisementProfitShareScheduledTask.calculatePeriodStoreProfitStatistic(startDate, endDate, storeIds);
                    pageIndex++;
                }
            });
            remark = "手动计算月流水收益成功";
        } catch (Exception e) {
            e.printStackTrace();
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "手动计算月流水收益失败";
            scheduledJob.setSuccessed(false);
        }
        scheduledJob.setRemark(remark);
        scheduledJobRepository.save(scheduledJob);
        return InvokeResult.SuccessResult();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟订单自动【投放】操作", notes = "创建人：毛向军")
    @RequestMapping(value="/deliveryOrder",method = RequestMethod.POST)
    public InvokeResult deliveryOrder(@Validated @RequestBody OrderMockOperationRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        OrderInfo orderInfo = orderService.getOrder(request.getId());
        if(!OrderStatusEnum.PendingDelivery.equals(orderInfo.getOrderStatusEnum())) {
            throw new BusinessException("当前订单状态不能做投放操作");
        }
        orderInfo.setStartTime(request.getDate());
        orderInfoRepository.save(orderInfo);
        return InvokeResult.SuccessResult();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟订单自动【完成】操作", notes = "创建人：毛向军")
    @RequestMapping(value="/finishOrder",method = RequestMethod.POST)
    public InvokeResult finishOrder(@Validated @RequestBody OrderMockOperationRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        OrderInfo orderInfo = orderService.getOrder(request.getId());
        if(!OrderStatusEnum.Delivering.equals(orderInfo.getOrderStatusEnum())) {
            throw new BusinessException("当前订单状态不能做完成操作");
        }
        orderInfo.setEndTime(request.getDate());
        orderInfoRepository.save(orderInfo);
        return InvokeResult.SuccessResult();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟订单自动【取消】操作", notes = "创建人：毛向军")
    @RequestMapping(value="/cancelOrder",method = RequestMethod.POST)
    public InvokeResult cancelOrder(@Validated @RequestBody OrderMockOperationRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        OrderInfo orderInfo = orderService.getOrder(request.getId());
        if(!OrderStatusEnum.PendingPublishing.equals(orderInfo.getOrderStatusEnum())) {
            throw new BusinessException("当前订单状态不能做自动取消操作");
        }
        orderInfoRepository.updateCreateTime(request.getDate(),request.getId());
        return InvokeResult.SuccessResult();
    }


    @Transactional
    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟生成广告任务", notes = "创建人：王伟权")
    @RequestMapping(value="/createAdvertisementTask",method = RequestMethod.POST)
    public InvokeResult reCreateAdvertisementTaskScheduledTask(@Validated @RequestBody MockAdvertisementTaskViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        LocalDateTime startDateTime = new LocalDateTime(viewModel.getStartTime());
        LocalDateTime endDateTime = new LocalDateTime(viewModel.getEndTime());

        if(startDateTime.isAfter(endDateTime)) {
            return InvokeResult.Fail("开始时间必须小于结束时间");
        }
        Advertisement advertisement = advertisementRepository.findOne(qAdvertisement.id.eq(viewModel.getAdvertisementId()).and(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())));
        if (advertisement == null) {
            return InvokeResult.Fail("请选择正确的广告");
        }

        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementTaskScheduledTask.class.getName() + "手动调用");
        String remark ="";
        try {
            taskMapper.deleteAdvertisementTaskByTime(viewModel);
            while (startDateTime.isBefore(endDateTime) || startDateTime.equals(endDateTime)) {
                advertisementTaskScheduledTask.calculateAdvertisementTask(advertisement, startDateTime.toDate());
                startDateTime = startDateTime.plusDays(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "手动生成广告任务失败";
            scheduledJob.setSuccessed(false);
        }
        scheduledJob.setRemark(remark);
        scheduledJobRepository.save(scheduledJob);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "完成任务")
    @PostMapping(value = "/task/complete")
    public InvokeResult completeAdvertisementStoreTask(@Validated @RequestBody MockCompleteTaskRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        taskService.completeTask(request);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "根据补录订单重算日收益", notes = "创建人：王伟权")
    @RequestMapping(value="/recalculateProfitByOrder",method = RequestMethod.POST)
    public InvokeResult recalculateProfitByOrder(@RequestParam int year, @RequestParam int month) {
        LocalDate startLocalDate = new LocalDate(year, month, 1);
        LocalDate endLocalDate = startLocalDate.plusMonths(1).minusDays(1);
        try {//根据补录订单, 重算门店分成和广告分成
            int pageSize = 500, pageIndex = 0;
            while (true) {
                Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                List<String> storeIds = storeInfoRepository.findAll(q ->
                        q.select(qStoreInfo.id).from(qStoreInfo)
                                .where(qStoreInfo.deleted.isFalse().and(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue())))
                                .offset(pageable.getOffset()).limit(pageable.getPageSize()));
                if (storeIds == null || storeIds.isEmpty())
                    break;
                advertisementProfitShareScheduledTask.recalculateProfitInfoBySupplementOrder(startLocalDate.toDate(), endLocalDate.toDate(), storeIds);
                pageIndex++;
            }
        } catch (Exception e) {
            logger.error("根据订单补录,重算门店分成和广告分成失败");
            return InvokeResult.Fail("根据订单补录,重算门店分成和广告分成失败");
        }
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "手动下载资源文件",notes = "文丰")
    @RequestMapping(value="/resourceDownLoad",method = RequestMethod.GET)
    public InvokeResult resourceDownLoad(@RequestParam(required = false)  String templateId,@RequestParam(required = false) Boolean override){
        if(org.springframework.util.StringUtils.isEmpty(templateId)){
            new Thread(resourceDownloadThread).start();

        }else{
            ContractTemplate contractTemplate= contractTemplateService.getContractTemplate(templateId);
            if(contractTemplate==null)
                throw new BusinessException("模板ID无效");
            ContractTemplateTypeEnum contractTemplateTypeEnum= EnumUtils.toEnum(contractTemplate.getTemplateType(),ContractTemplateTypeEnum.class);
            String fullFmtPath=previewConfig.getFullFmtPath(templateId,contractTemplateTypeEnum.getExt());
            resourceDownloadThread.downloadResource(contractTemplate.getUrl(),fullFmtPath,override);
        }
        return InvokeResult.SuccessResult();
    }



    @Autowired
    private AdvertisementProfitJobService advertisementProfitJobService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟计算广告下的门店日收益",notes = "创建人：李川")
    @RequestMapping(value="calculateAdvertisementDailyProfit",method = RequestMethod.POST)
    public InvokeResult calculateAdvertisementDailyProfit(@Validated @RequestBody CalculateProfitByAdvertisementViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        new Thread(()->{
            AuthenticationService.setAdminLogin();
            //清空广告分成
            advertisementProfitJobService.resetAdvertisementShareProfit(viewModel.getCalculateAdvertisementIds());
            //清空门店日收益
            Query query= new Query(Criteria.where("date").gte(viewModel.getStartDate().getTime()).lte(viewModel.getEndDate().getTime()));
            mongoTemplate.remove(query, StoreDailyProfit.class);
            LocalDate date = new LocalDate(viewModel.getStartDate());
            var endDate = new LocalDate(viewModel.getEndDate());
            while (!date.isAfter(endDate)){
                advertisementProfitJobService.reCalculateAdvertisementDailyProfit(date.toDate(),viewModel.getCalculateAdvertisementIds(),viewModel.isActive(),viewModel.getOrderCount(),viewModel.getBootHour());
                date =date.plusDays(1);
            }
        }).start();
        return InvokeResult.SuccessResult();
    }
}