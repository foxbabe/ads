package com.sztouyun.advertisingsystem.api.scheduled;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.DateInfo;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.repository.common.DateInfoRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.scheduled.*;
import com.sztouyun.advertisingsystem.service.job.StoreInfoServiceJob;
import com.sztouyun.advertisingsystem.viewmodel.DayPeriodRequest;
import com.sztouyun.advertisingsystem.viewmodel.TimePeriodRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.CalculateProfitByAdvertisementViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.ManualInvokeProfitShareViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.ReCalculateAdvertisementProfitActiveViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Api(value = "定时任务接口")
@RestController
@RequestMapping("/api/scheduled")
public class ScheduledApiController extends BaseApiController {

    @Autowired
    private StoreInfoScheduledTask storeInfoScheduledTask;

    @Autowired
    private DateInfoRepository dateInfoRepository;

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DailyStatisticScheduledTask dailyStatisticScheduledTask;

    @Autowired
    private AdvertisementDeliveryLogScheduledTask advertisementDeliveryLogScheduledTask;
    @Autowired
    private AdvertisementProfitShareScheduledTask advertisementProfitShareScheduledTask;
    @Autowired
    private StoreDailyStatisticScheduledTask storeDailyStatisticScheduledTask;
    @Autowired
    private StoreInfoServiceJob storeInfoServiceJob;
    @Autowired
    private PartnerAdvertisementStoreOperationScheduledTask partnerAdvertisementStoreOperationScheduledTask;
    private QStoreInfo qStoreInfo =QStoreInfo.storeInfo;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "重算门店类型", notes = "创建人：李川")
    @RequestMapping(value="recalculateStoreType",method = RequestMethod.POST)
    public InvokeResult recalculateStoreType(@RequestParam String startDate){
        new Thread(()-> storeInfoScheduledTask.recalculateStoreType(startDate)).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "同步广告监控统计信息", notes = "创建人：李川")
    @RequestMapping(value="updateDeliveryMonitor",method = RequestMethod.POST)
    public InvokeResult updateDeliveryMonitor(){
        new Thread(()-> advertisementDeliveryLogScheduledTask.updateDeliveryMonitor()).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新门店信息到mongodb", notes = "创建人：李川")
    @RequestMapping(value="updateStoreInfo",method = RequestMethod.POST)
    public InvokeResult updateStoreInfo(){
        new Thread(()->{
            try {
                if(!mongoTemplate.collectionExists(PartnerAdvertisementDeliveryRecord.class)){
                    mongoTemplate.createCollection(PartnerAdvertisementDeliveryRecord.class);
                }
                mongoTemplate.dropCollection(StoreInfo.class);
            }catch (Exception e){
            }
            int pageSize = 2000, pageIndex = 0;
            while (true){
                Pageable pageable = new MyPageRequest(pageIndex, pageSize);
                List<StoreInfo> storeInfos = storeInfoRepository.findAll(q ->
                        q.selectFrom(qStoreInfo).where(qStoreInfo.storeSource.eq(StoreSourceEnum.NEW_OMS.getValue()))
                                .orderBy(qStoreInfo.id.asc()).offset(pageable.getOffset()).limit(pageable.getPageSize()));
                if (storeInfos == null || storeInfos.isEmpty())
                    break;
                mongoTemplate.insertAll(storeInfos);
                pageIndex++;
            }
        }
        ).start();
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新日期信息", notes = "创建人：李川")
    @RequestMapping(value="updateDateInfo",method = RequestMethod.POST)
    public InvokeResult updateDateInfo(){
        new Thread(()->{
            List<DateInfo> dateInfoList  = new ArrayList<>();
            DateTime startDate = DateTime.parse("2018-01-01");
            DateTime maxDate = DateTime.parse("2030-01-01");
            DateTime date =startDate;
            while (date.compareTo(maxDate) < 0){
                DateInfo dateInfo = new DateInfo();
                dateInfo.setDate(date.toDate());
                dateInfo.setYear(date.getYear());
                dateInfo.setMonth(date.getMonthOfYear());
                dateInfo.setDay(date.getDayOfMonth());
                dateInfo.setWeek(date.getDayOfWeek());
                dateInfo.setDayOfYear(date.getDayOfYear());
                dateInfo.setWeekOfYear(date.getWeekOfWeekyear());
                dateInfoList.add(dateInfo);
                date =date.plusDays(1);
            }
            dateInfoRepository.save(dateInfoList);
        }
        ).start();
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "手动执行计算指定门店收益", notes = "创建人：王伟权")
    @RequestMapping(value="manualInvokeProfitShareSchedule",method = RequestMethod.POST)
    public InvokeResult manualInvokeProfitShareSchedule(@Validated @RequestBody ManualInvokeProfitShareViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        new Thread(()-> advertisementProfitShareScheduledTask.manualCalculateProfitShare(viewModel)).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "计算广告选中门店的收益", notes = "创建人：王伟权")
    @RequestMapping(value="calculateProfitByAdvertisement",method = RequestMethod.POST)
    public InvokeResult calculateProfitByAdvertisement(@Validated @RequestBody CalculateProfitByAdvertisementViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        new Thread(()-> advertisementProfitShareScheduledTask.calculateProfitByAdvertisement(viewModel)).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "重算广告收益表中广告是否激活", notes = "创建人：王伟权")
    @RequestMapping(value="reCalculateAdvertisementProfitActive",method = RequestMethod.POST)
    public InvokeResult reCalculateAdvertisementProfitActive(@Validated @RequestBody ReCalculateAdvertisementProfitActiveViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        viewModel.getAdvertisementIds().forEach(advertisementId -> {
            fixedThreadPool.execute(() -> advertisementProfitShareScheduledTask.reCalculateActiveAdvertisement(advertisementId, viewModel.getStartDate(), viewModel.getEndDate()));
        });
        return InvokeResult.SuccessResult();
    }
	
	@PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "第一次往指定第三方推全量的门店信息", notes = "创建者：文丰")
    @GetMapping(value = "{partnerId}")
    public InvokeResult pushAllAvailableStore(@PathVariable("partnerId") String partnerId){
        storeInfoServiceJob.pushStoreToPartner(partnerId);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新门店初始化",notes = "创建人：文丰")
    @RequestMapping(value="initNewStore",method = RequestMethod.POST)
    public InvokeResult initNewStore(){
        new Thread(()-> storeInfoScheduledTask.syncNewStoreInfo()).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "门店画像数据初始化",notes = "创建人：文丰")
    @RequestMapping(value="initStorePortraitInfo",method = RequestMethod.POST)
    public InvokeResult initStorePortraitInfo(){
        new Thread(()->storeInfoScheduledTask.manualSyncStorePortraitInfo()).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "计算门店日统计",notes = "创建人：李川")
    @RequestMapping(value="saveStoreDailyStatistics",method = RequestMethod.POST)
    public InvokeResult saveStoreDailyStatistics(@Validated @RequestBody TimePeriodRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        new Thread(()->storeDailyStatisticScheduledTask.saveStoreDailyStatistics(request.getStartDate(),StoreSourceEnum.NEW_OMS)).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "重算合作方广告门店投放信息")
    @PostMapping(value = "reCalcDailyPartnerStoreAdvertisementDisplayInfo")
    public InvokeResult reCalcDailyPartnerStoreAdvertisementDisplayInfo(@Validated @RequestBody TimePeriodRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        new Thread(()->{
            partnerAdvertisementStoreOperationScheduledTask.calcPartnerStoreAdvertisementDisplayInfo(request.getStartDate(),request.getEndDate());
        }).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "重算门店设备开关机时间时长")
    @PostMapping(value = "calcStoreOpenCloseTime")
    public InvokeResult calcStoreOpenCloseTime(@Validated @RequestBody TimePeriodRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        new Thread(()->{
            storeInfoServiceJob.calcStoreOpenCloseTime(request.getStartDate(),request.getEndDate());
        }).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "全量同步门店周边信息",notes = "创建人：文丰")
    @RequestMapping(value="sysStoreNearByInfo",method = RequestMethod.POST)
    public InvokeResult sysStoreNearByInfo(){
        new Thread(()->storeInfoServiceJob.syncStoreNearByInfo()).start();
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "模拟统计每日客户数量",notes = "创建人：文丰")
    @PostMapping(value = "statisticDailyCustomerInfo")
    public InvokeResult statisticDailyCustomerInfo(@Validated @RequestBody DayPeriodRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        if(request.getStartTime().after(request.getEndTime()))
            throw new BusinessException("开始时间必须在结束时间之前");
        dailyStatisticScheduledTask.statisticDailyCustomerInfo(request.getStartTime(),request.getEndTime());
        return InvokeResult.SuccessResult();

    }
}
