package com.sztouyun.advertisingsystem.api.coorperationPartner;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.partner.*;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.monitor.RequestStoreRankInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "合作方管理接口")
@RestController
@RequestMapping("/api/cooperationPartner")
public class CooperationPartnerController extends BaseApiController {

    @Autowired
    private CooperationPartnerService cooperationPartnerService;

    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;

    @ApiOperation(value = "新建合作方", notes = "修改人: 杨浩")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InvokeResult<String> createCooperationPartner(@Validated @RequestBody CooperationPartnerViewModel viewModel,
                                                         BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        CooperationPartner cooperationPartner = new CooperationPartner();
        BeanUtils.copyProperties(viewModel, cooperationPartner);
        return InvokeResult
            .SuccessResult(cooperationPartnerService.createCooperationPartner(cooperationPartner));
    }

    @ApiOperation(value = "合作方详情", notes = "修改人: 杨浩")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InvokeResult<CooperationPartnerDetailViewModel> cooperationPartnerDetail(@PathVariable String id) {
        CooperationPartnerDetailViewModel cooperationPartnerDetailViewModel = new CooperationPartnerDetailViewModel();
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        CooperationPartner cooperationPartner = cooperationPartnerService
            .findCooperationPartnerById(id);
        BeanUtils.copyProperties(cooperationPartner, cooperationPartnerDetailViewModel);

        cooperationPartnerDetailViewModel
            .setCreatorName(getUserNickname(cooperationPartnerDetailViewModel.getCreatorId()));
        cooperationPartnerDetailViewModel
            .setOwnerName(getUserNickname(cooperationPartnerDetailViewModel.getOwnerId()));

        cooperationPartnerDetailViewModel.setProvinceName(getAreaName(cooperationPartnerDetailViewModel.getProvinceId()));
        cooperationPartnerDetailViewModel.setCityName(getAreaName(cooperationPartnerDetailViewModel.getCityId()));
        cooperationPartnerDetailViewModel.setRegionName(getAreaName(cooperationPartnerDetailViewModel.getRegionId()));

        cooperationPartnerDetailViewModel.setCooperationPatternName(EnumUtils.getDisplayName(cooperationPartner.getCooperationPattern(), CooperationPatternEnum.class));
        cooperationPartnerDetailViewModel.setDurationUnitName(EnumUtils.getDisplayName(cooperationPartner.getDurationUnit(), UnitEnum.class));

        Map<String,PartnerAdvertisementRelatedInfo> info =  partnerAdvertisementService.getPartnerAdvertisementRelatedInfo(Arrays.asList(id));
        PartnerAdvertisementRelatedInfo partnerAdvertisementRelatedInfo = info.get(id);
        if(partnerAdvertisementRelatedInfo!=null){
            cooperationPartnerDetailViewModel.setAdvertisementDelivery(partnerAdvertisementRelatedInfo.getDeliveryQuantity()>0);
            cooperationPartnerDetailViewModel.setTotalDeliveryQuantity(partnerAdvertisementRelatedInfo.getTotalDeliveryQuantity());
        }
        if (!getUser().isAdmin()) {//仅系统管理员对apiUrl可见
            cooperationPartnerDetailViewModel.setApiUrl("");
        }
        return InvokeResult.SuccessResult(cooperationPartnerDetailViewModel);
    }

    @ApiOperation(value = "编辑合作方", notes = "修改人: 杨浩")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public InvokeResult updateCooperationPartner(@PathVariable String id,
                                                 @Validated @RequestBody CooperationPartnerViewModel viewModel,
                                                 BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        CooperationPartner cooperationPartner = new CooperationPartner();
        BeanUtils.copyProperties(viewModel, cooperationPartner);
        cooperationPartnerService.updateCooperationPartner(id, cooperationPartner);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "禁用/取消禁用合作方", notes = "修改人: 杨浩")
    @RequestMapping(value = "/{id}/disabled", method = RequestMethod.POST)
    public InvokeResult updateCooperationPartnerStatus(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        cooperationPartnerService.toggleEnabled(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询合作方列表", notes = "创建人:杨浩")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public InvokeResult<PageList<CooperationPartnerListItemViewModel>> queryCooperationPartner(@Validated @RequestBody CooperationPartnerBasePageInfoViewModel viewModel,
                                                                                               BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(),
            new QSort(QCooperationPartner.cooperationPartner.createdTime.desc()));
        Page<CooperationPartner> pages = cooperationPartnerService
            .queryCooperationPartnerList(viewModel, pageable);

        List<String> partnerIds = Linq4j.asEnumerable(pages.getContent()).select(q -> q.getId()).toList();
        Map<String,PartnerAdvertisementRelatedInfo> info =  partnerAdvertisementService.getPartnerAdvertisementRelatedInfo(partnerIds);
        PageList<CooperationPartnerListItemViewModel> pageList= ApiBeanUtils.convertToPageList(pages, cooperationPartner->{
            CooperationPartnerListItemViewModel item =  new CooperationPartnerListItemViewModel();
            BeanUtils.copyProperties(cooperationPartner,item);
            item.setCreatorName(getUserNickname(item.getCreatorId()));
            item.setOwnerName(getUserNickname(item.getOwnerId()));
            item.setCityName(getAreaName(item.getCityId()));
            item.setCooperationPattern(EnumUtils.getDisplayName(cooperationPartner.getCooperationPattern(), CooperationPatternEnum.class));
            item.setDuration(cooperationPartner.getDuration()+EnumUtils.getDisplayName(cooperationPartner.getDurationUnit(), UnitEnum.class));
            if(cooperationPartner.getDuration()==null||cooperationPartner.getDuration()==0)
                item.setDuration("");
            PartnerAdvertisementRelatedInfo partnerAdvertisementRelatedInfo = info.get(cooperationPartner.getId());
            if(partnerAdvertisementRelatedInfo!=null){
                item.setAdvertisementDelivery(partnerAdvertisementRelatedInfo.getDeliveryQuantity()>0);
                item.setTotalDeliveryQuantity(partnerAdvertisementRelatedInfo.getTotalDeliveryQuantity());
            }
            return item;
            }
        );
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "为合作方分配业务员",notes = "修改人：杨浩")
    @RequestMapping(value="distribute/{cooperationPartnerId}/{ownerId}",method = RequestMethod.POST)
    public InvokeResult distributeCooperationPartner(@PathVariable String cooperationPartnerId, @PathVariable String ownerId){
        if(StringUtils.isEmpty(cooperationPartnerId))
            return InvokeResult.Fail("合作方id不能为空");
        if(StringUtils.isEmpty(ownerId))
            return InvokeResult.Fail("业务员id不能为空");
        cooperationPartnerService.distributeCooperationPartner(cooperationPartnerId,ownerId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "请求次数柱状图",notes = "创建人：毛向军")
    @RequestMapping(value="histogram/requestTimes",method = RequestMethod.POST)
    public InvokeResult<List<CooperationPartnerChartRequestTimesViewModel>> requestTimesHistogram(@Validated @RequestBody CooperationPartnerChartBaseRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(cooperationPartnerService.partnerAdvertisementRequestLogStatistic(request));
    }

    @ApiOperation(value = "合作方详情的请求次数饼图",notes = "创建人：毛向军")
    @RequestMapping(value="pieChart/requestTimes",method = RequestMethod.POST)
    public InvokeResult<CooperationPartnerPieChartRequestTimesViewModel> requestTimesPieChart(@Validated @RequestBody CooperationPartnerPieChartRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        CooperationPartnerPieChartRequestTimesViewModel viewModel = new CooperationPartnerPieChartRequestTimesViewModel();
        List<CooperationPartnerPieChartRequestTimesInfo> cooperationPartnerPieChartRequestTimesInfos = cooperationPartnerService.requestTimesPieChartStatistic(request);
        if(cooperationPartnerPieChartRequestTimesInfos!=null&&cooperationPartnerPieChartRequestTimesInfos.size()>0){
            long totalRequestTimes = cooperationPartnerPieChartRequestTimesInfos.stream().mapToLong(CooperationPartnerPieChartRequestTimesInfo::getRequestTimes).sum();
            for (CooperationPartnerPieChartRequestTimesInfo cooperationPartnerPieChartRequestTimesInfo : cooperationPartnerPieChartRequestTimesInfos) {
                viewModel.setApiErrorTimes(viewModel.getApiErrorTimes()+cooperationPartnerPieChartRequestTimesInfo.getApiErrorTimes());
                viewModel.setGetNoAdTimes(viewModel.getGetNoAdTimes()+cooperationPartnerPieChartRequestTimesInfo.getGetNoAdTimes());
                viewModel.setRequestSuccessTimes(viewModel.getRequestSuccessTimes()+cooperationPartnerPieChartRequestTimesInfo.getRequestSuccessTimes());
                cooperationPartnerPieChartRequestTimesInfo.setTotalRequestTimes(totalRequestTimes);
            }
            viewModel.setCooperationPartnerPieChartRequestTimesInfos(cooperationPartnerPieChartRequestTimesInfos);
        }
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "合作方详情的请求门店趋势图",notes = "创建人：毛向军")
    @RequestMapping(value="lineChart/requestStoreCount",method = RequestMethod.POST)
    public InvokeResult<CooperationPartnerLineChartStoreCountViewModel> requestStoreCountLineChart(@Validated @RequestBody CooperationPartnerPieChartRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Date startTime = request.getStartTime();
        Date endTime = request.getEndTime();
        CooperationPartnerLineChartStoreCountViewModel viewModel = cooperationPartnerService.requestStoreCountLineChartStatistic(request);
        List<CooperationPartnerLineChartStoreCountInfo> cooperationPartnerLineChartStoreCountInfos = viewModel.getCooperationPartnerLineChartStoreCountInfos();
        Map<Date, CooperationPartnerLineChartStoreCountInfo> map = Linq4j.asEnumerable(cooperationPartnerLineChartStoreCountInfos).toMap(CooperationPartnerLineChartStoreCountInfo::getDate, b -> b);
        while (!endTime.before(startTime)){
            if(map.get(startTime)==null){
                cooperationPartnerLineChartStoreCountInfos.add(new CooperationPartnerLineChartStoreCountInfo(startTime,viewModel.getConfigStoreCount()));
            }
            startTime =new LocalDate(startTime).plusDays(1).toDate();
        }
        cooperationPartnerLineChartStoreCountInfos.sort(Comparator.comparing(CooperationPartnerLineChartStoreCountInfo::getDate));
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "合作方广告展示次数占比", notes = "创建人: 李海峰")
    @PostMapping("/displayTimesProportion")
    public InvokeResult<DisplayTimesProportionViewModel> displayTimesProportion(@RequestBody DisplayTimesProportionRequest request) {
        cooperationPartnerService.findCooperationPartnerById(request.getPartnerId());// 校验合作方是否存在

        val adPositionStatisticsTimes = cooperationPartnerService.getAdPositionStatisticsTimes(request);
        val displayTimes = adPositionStatisticsTimes.stream().mapToLong(AdPositionStatisticsTimes::getDisplayTimes).sum();
        DisplayTimesProportionViewModel viewModel = new DisplayTimesProportionViewModel();
        viewModel.setValidTimes(adPositionStatisticsTimes.stream().mapToLong(AdPositionStatisticsTimes::getValidTimes).sum());
        viewModel.setValidProportion(NumberFormatUtil.format(viewModel.getValidTimes(), displayTimes, Constant.RATIO_PATTERN));
        viewModel.setInvalidTimes(adPositionStatisticsTimes.stream().mapToLong(AdPositionStatisticsTimes::getInvalidTimes).sum());
        viewModel.setInvalidProportion(NumberFormatUtil.format(viewModel.getInvalidTimes(), displayTimes, Constant.RATIO_PATTERN));
        viewModel.setItem(adPositionStatisticsTimes.stream().map(e -> {
            DisplayTimesProportionItem item = new DisplayTimesProportionItem();
            item.setAdvertisementPositionCategoryName(e.getAdvertisementPositionCategoryName());
            item.setValidTimes(e.getValidTimes());
            item.setValidProportion(NumberFormatUtil.format(item.getValidTimes(), displayTimes, Constant.RATIO_PATTERN));
            item.setInvalidTimes(e.getInvalidTimes());
            item.setInvalidProportion(NumberFormatUtil.format(item.getInvalidTimes(), displayTimes, Constant.RATIO_PATTERN));
            return item;
        }).collect(Collectors.toList()));

        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "合作方广告展示次数统计", notes = "创建人: 李海峰")
    @PostMapping("/displayTimesStatistics")
    public InvokeResult<List<DisplayTimesViewModel>> displayTimesStatistics(@Valid @RequestBody DisplayTimesRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);

        val list = cooperationPartnerService.getPartnerDisplayTimesStatistic(request);
        list.forEach(e -> {
            e.setValidProportion(NumberFormatUtil.format(e.getValidTimes(), e.getDisplayTimes(), Constant.RATIO_PATTERN));
            e.setInvalidTimes(e.getDisplayTimes() - e.getValidTimes());
            e.setInvalidProportion(NumberFormatUtil.format(e.getInvalidTimes(), e.getDisplayTimes(), Constant.RATIO_PATTERN));
        });
        return InvokeResult.SuccessResult(list);
    }
    @ApiOperation(value = "请求城市排名",notes = "创建人：文丰")
    @PostMapping(value = "requestStoreRankInfo")
    public InvokeResult<List<RequestStoreRankInfo>> getRequestStoreRankInfo(@Validated @RequestBody StoreRankInfoRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<RequestStoreRankInfo> resultList=cooperationPartnerService.getRequestStoreRankInfo(request);
        Map<String,Long> configMap=cooperationPartnerService.findStoreCountByCity(request.getPartnerId());
        resultList.stream().forEach(item->{
            if(configMap.containsKey(item.getCityId())){
                item.setConfigStoreCount(configMap.get(item.getCityId()));
            }
            item.setCityName(getAreaName(item.getCityId()));
        });
        resultList.sort(getRequestRankComparator(EnumUtils.toEnum(request.getSortType(),RequestStoreRankSortTypeEnum.class)));
        return InvokeResult.SuccessResult(resultList);
    }

    private Comparator getRequestRankComparator(RequestStoreRankSortTypeEnum sortTypeEnum ){
        switch (sortTypeEnum){
            case ValidRatio:
                return Comparator.comparing(RequestStoreRankInfo::getDoubleValidRatio).reversed().thenComparing(RequestStoreRankInfo::getCityId);
            case ValidRequestCount:
                return Comparator.comparing(RequestStoreRankInfo::getValidTimes).reversed().thenComparing(RequestStoreRankInfo::getCityId);
                default:
                    return Comparator.comparing(RequestStoreRankInfo::getRequestStoreCount).reversed().thenComparing(RequestStoreRankInfo::getCityId);
        }
    }

    @ApiOperation(value = "门店请求排名", notes = "创建人: 李海峰")
    @PostMapping("/storeRequestRanking")
    public InvokeResult<List<StoreRequestRankingViewModel>> storeRequestRanking(@RequestBody StoreRequestRankingRequest request) {
        val list = cooperationPartnerService.getAllPartnerStoreNum(request);
        list.forEach(e -> e.setNoRequestStoreNum(e.getStoreNum() - e.getRequestStoreNum()));
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "请求广告数量", notes = "创建人: 毛向军")
    @PostMapping("/advertisementRequestCount")
    public InvokeResult<List<AdvertisementRequestCountViewModel>> advertisementRequestCount(@RequestBody CooperationPartnerChartBaseRequest request){
        List<AdvertisementRequestCountViewModel> list = new ArrayList<>();
        MapReduceResults<AdvertisementRequestCountResult> mapReduceResults = cooperationPartnerService.getAdvertisementRequestStatistic(null,request.getStartTime(),request.getEndTime());
        long totalRequestCount = Linq4j.asEnumerable(mapReduceResults).select(q -> q.getValue().getRequestAdvertisementCount()).sum(Long::longValue);
        for (AdvertisementRequestCountResult mapReduceResult : mapReduceResults) {
            AdvertisementRequestCountViewModel viewModel = mapReduceResult.getValue();
            viewModel.setPartnerName(cooperationPartnerService.getPartnerNameFromCache(viewModel.getPartnerId()));
            viewModel.setDisplayAdvertisementRatio(NumberFormatUtil.format(viewModel.getDisplayAdvertisementCount(),viewModel.getRequestAdvertisementCount(),Constant.RATIO_PATTERN));
            viewModel.setNoDisplayAdvertisementCount(viewModel.getRequestAdvertisementCount()-viewModel.getDisplayAdvertisementCount());
            viewModel.setNoDisplayAdvertisementRatio(NumberFormatUtil.format(viewModel.getNoDisplayAdvertisementCount(),viewModel.getRequestAdvertisementCount(),Constant.RATIO_PATTERN));
            viewModel.setRequestAdvertisementRatio(NumberFormatUtil.format(viewModel.getRequestAdvertisementCount(),totalRequestCount,Constant.RATIO_PATTERN));
            list.add(viewModel);
        }
        list.sort(Comparator.comparing(AdvertisementRequestCountViewModel::getRequestAdvertisementCount).reversed());

        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "合作方详情的请求广告趋势", notes = "创建人: 毛向军")
    @PostMapping("/lineChart/requestAdvertisementCount")
    public InvokeResult<CooperationPartnerLineChartAdsCountViewModel> advertisementRequestCountLineChart(@Validated@RequestBody CooperationPartnerPieChartRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        if(request.getStartTime()==null||request.getEndTime()==null)
            return InvokeResult.Fail("开始时间或结束时间不能为空");
        CooperationPartnerLineChartAdsCountViewModel viewModel = new CooperationPartnerLineChartAdsCountViewModel();
        List<CooperationPartnerLineChartAdsCountInfo> cooperationPartnerLineChartAdsCountInfos = viewModel.getCooperationPartnerLineChartAdsCountInfos();
        Map<Date,CooperationPartnerLineChartAdsCountInfo> map = new HashMap<>();

        for (AdvertisementRequestCountResult mapReduceResult : cooperationPartnerService.getAdvertisementRequestStatistic(request.getCooperationPartnerId(),request.getStartTime(),request.getEndTime())){
            AdvertisementRequestCountViewModel advertisementRequestCountViewModel = mapReduceResult.getValue();
            if(advertisementRequestCountViewModel.getPartnerId().equals(request.getCooperationPartnerId())){
                viewModel.setRequestAdvertisementCount(advertisementRequestCountViewModel.getRequestAdvertisementCount());
                viewModel.setDisplayAdvertisementCount(advertisementRequestCountViewModel.getDisplayAdvertisementCount());
                break;
            }
        }

        Date startTime = request.getStartTime();
        Date endTime = request.getEndTime();
        MapReduceResults<AdvertisementRequestCountLineChartResult> mapReduceResults = cooperationPartnerService.getAdvertisementRequestLineChartStatistic(request.getCooperationPartnerId(), startTime, endTime);
        for (AdvertisementRequestCountLineChartResult mapReduceResult : mapReduceResults) {
            map.put(mapReduceResult.getId(),mapReduceResult.getValue());
            cooperationPartnerLineChartAdsCountInfos.add(mapReduceResult.getValue());
        }

        while (!endTime.before(startTime)){
            if(map.get(startTime)==null){
                cooperationPartnerLineChartAdsCountInfos.add(new CooperationPartnerLineChartAdsCountInfo(startTime));
            }
            startTime =new LocalDate(startTime).plusDays(1).toDate();
        }

        cooperationPartnerLineChartAdsCountInfos.sort(Comparator.comparing(CooperationPartnerLineChartAdsCountInfo::getDate));

        return InvokeResult.SuccessResult(viewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "合作方广告位配置列表", notes = "创建人: 毛向军")
    @PostMapping("/{partnerId}/partnerAdSlotConfig")
    public InvokeResult<PageList<PartnerAdSlotConfigViewModel>> partnerAdSlotConfigList(@PathVariable("partnerId") String partnerId) {
        List<PartnerAdSlotConfig> list = cooperationPartnerService.partnerAdSlotConfigList(partnerId);
        List<PartnerAdSlotConfigViewModel> viewModels = ApiBeanUtils.toList(list, l -> {
            PartnerAdSlotConfigViewModel viewModel = ApiBeanUtils.copyProperties(l, PartnerAdSlotConfigViewModel.class);
            viewModel.setUpdaterName(getUserNickname(l.getUpdaterId()));
            return viewModel;
        });
        PageList<PartnerAdSlotConfigViewModel> pageList=new PageList<>();
        pageList.setList(viewModels);
        return InvokeResult.SuccessResult(pageList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "是否禁用合作方的单个广告位", notes = "创建人: 毛向军")
    @GetMapping("/{partnerAdSlotConfigId}/toggleEnabled")
    public InvokeResult toggleEnabled(@PathVariable("partnerAdSlotConfigId") long partnerAdSlotConfigId) {
        cooperationPartnerService.toggleAdSlotEnabled(partnerAdSlotConfigId);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新合作方单个广告位的优先级", notes = "创建人:毛向军")
    @RequestMapping(value = "updateAdSlotPriority", method = RequestMethod.POST)
    public InvokeResult updateAdSlotPriority(@Validated @RequestBody PartnerAdSlotConfigUpdateRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        cooperationPartnerService.updateAdSlotPriority(request.getCheckedId(),request.getReplacedPriority());
        return InvokeResult.SuccessResult();
    }


}
