package com.sztouyun.advertisingsystem.api.store;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.StoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.store.SurroundingsDistrictEnum;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.service.adProfitShare.AdvertisementProfitStatisticService;
import com.sztouyun.advertisingsystem.service.adProfitShare.PeriodStoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.adProfitShare.StoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementPriceConfigService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.contract.ContractStoreService;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.service.store.StorePortraitService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.*;
import com.sztouyun.advertisingsystem.utils.excel.*;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitSharePageInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitSharePageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementProfitShareDaysPageInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementProfitShareDaysPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.StoreAdvertisementInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.BasePartnerInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoItem;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.monitor.ContractStoreInfoQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "门店信息接口")
@RestController
@RequestMapping("/api/store")
public class StoreInfoApiController extends BaseApiController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private AdvertisementPriceConfigService advertisementPriceConfigService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementProfitStatisticService advertisementProfitStatisticService;
    @Autowired
    private StoreProfitStatisticService storeProfitStatisticService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PeriodStoreProfitStatisticService periodStoreProfitStatisticService;
    @Autowired
    private StorePortraitService storePortraitService;
    @Autowired
    private ContractStoreService contractStoreService;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @ApiOperation(value = "订单详情内的门店列表",notes = "创建人：毛向军")
    @RequestMapping(value = "/orderStoreList", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreOrderViewModel>> getOrderStoreList(@Validated @RequestBody StoreOrderRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Page<StoreOrderViewModel> pages = orderService.getOrderStoreList(pageable,request);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        PageList<StoreOrderViewModel> pageList = ApiBeanUtils.convertToPageList(pages, storeOrderViewModel -> {
            StoreOrderViewModel soreInfoViewModel = ApiBeanUtils.copyProperties(storeOrderViewModel, StoreOrderViewModel.class);
            soreInfoViewModel.setCityName(getAreaName(storeOrderViewModel.getCityId(), areaMap));
            soreInfoViewModel.setProvinceName(getAreaName(storeOrderViewModel.getProvinceId(), areaMap));
            soreInfoViewModel.setRegionName(getAreaName(storeOrderViewModel.getRegionId(), areaMap));
            return soreInfoViewModel;
        });

        return InvokeResult.SuccessResult(pageList);

    }

    @ApiOperation(value = "门店管理展示列表",notes = "创建人：毛向军")
    @RequestMapping(value = "/storeInfoStatistic", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoStatisticViewModel>> getStoreInfoStatistic(@Validated @RequestBody StoreInfoStatisticQueryRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        PageList<StoreInfoStatisticViewModel> resultList = getStoreInfoStatisticViewModelPageList(request);

        return InvokeResult.SuccessResult(resultList);

    }

    private PageList<StoreInfoStatisticViewModel> getStoreInfoStatisticViewModelPageList( StoreInfoStatisticQueryRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        if (request.getIsPaveGoods() != null) {
            request.getPaveGoodsConditionInfo().setIsPaveGoods(request.getIsPaveGoods());
        }
        if (!CollectionUtils.isEmpty(request.getPaveGoodsList())) {
            request.getPaveGoodsConditionInfo().setPaveGoodsList(request.getPaveGoodsList());
        }
        Page<StoreInfoStatisticViewModel> page = storeService.getStoreInfoStatistic(request, pageable);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(page, storeInfoStatisticViewModel -> {
            storeInfoStatisticViewModel.setProvinceName(getAreaName(storeInfoStatisticViewModel.getProvinceId(),areaMap));
            storeInfoStatisticViewModel.setCityName(getAreaName(storeInfoStatisticViewModel.getCityId(),areaMap));
            storeInfoStatisticViewModel.setRegionName(getAreaName(storeInfoStatisticViewModel.getRegionId(),areaMap));
            return storeInfoStatisticViewModel;
        });
    }

    @ApiOperation(value="合同选择门店",notes = "修改人: 王伟权")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoViewModel>> getStoreInfoList(@Validated @RequestBody StoreInfoPageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(),viewModel.getPageSize());
        Page<StoreInfoQueryResult> page = storeService.findStoreListByArea(convertViewModelToRequestDto(viewModel), pageable);

        PageList<StoreInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page, storeInfo->
        {
            StoreInfoViewModel storeInfoViewModel = new StoreInfoViewModel();
            BeanUtils.copyProperties(storeInfo,storeInfoViewModel);
            storeInfoViewModel.setCityName(getAreaName(storeInfo.getCityId()));
            storeInfoViewModel.setRegionName(getAreaName(storeInfo.getRegionId()));
            return storeInfoViewModel;
        });

        Integer chooseStoreInContract = storeService.getChooseStoresCount(viewModel.getContractId(), viewModel.getStoreType());
        resultList.setChooseCount(chooseStoreInContract == null? 0 : chooseStoreInContract.intValue());
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value="合同选择门店一键选取",notes = "修改人：王伟权  data返回当前操作选中的数量, 若返回0, 表示没有变化")
    @RequestMapping(value = "/autoChoose", method = RequestMethod.POST)
    public InvokeResult autoChoose(@Validated @RequestBody AutoChooseStoreInfoViewModel viewModel, BindingResult result) {
        if (viewModel.getIsCheck() != null && viewModel.getIsCheck()) {
            return InvokeResult.SuccessResult(0);
        }
        if(!advertisementPriceConfigService.hasCompletedPriceConfig())
            return InvokeResult.Fail("暂无门店类别价格配置，请联系系统管理员");
        if(result.hasErrors())
            return ValidateFailResult(result);
        if(viewModel.getRecordCount() <= 0)
            return InvokeResult.Fail("一键选取记录数必须大于0");
        if(!EnumUtils.isValidValue(viewModel.getStoreType() ,StoreTypeEnum.class)) {
            return InvokeResult.Fail("请输入正确的门店类型");
        }

        StoreInfoQueryRequest storeInfoQueryRequest = convertViewModelToRequestDto(viewModel);
        OneKeyInsertStoreInfoRequest oneKeyInsertStoreInfoRequest = ApiBeanUtils.copyProperties(storeInfoQueryRequest, OneKeyInsertStoreInfoRequest.class);
        oneKeyInsertStoreInfoRequest.setInsertCount(viewModel.getRecordCount());
        oneKeyInsertStoreInfoRequest.setPreviousDate(new LocalDate().minusDays(1).toDate());
        storeService.chooseStoreIdTop(oneKeyInsertStoreInfoRequest);
        return InvokeResult.SuccessResult(storeService.getChooseStoresCount(viewModel.getContractId(), viewModel.getStoreType()));
    }

    private void fillPaveGoodsInfo(StoreInfoQueryRequest info, StoreInfoPageInfoViewModel viewModel) {
        if (viewModel.getIsPaveGoods() != null) {
            info.getPaveGoodsConditionInfo().setIsPaveGoods(viewModel.getIsPaveGoods());
        }
        if (!CollectionUtils.isEmpty(viewModel.getPaveGoodsList())) {
            info.getPaveGoodsConditionInfo().setPaveGoodsList(viewModel.getPaveGoodsList());
        }
    }

    @ApiOperation(value = "合同门店列表",notes = "创建人：毛向军")
    @RequestMapping(value="/getStoreInfoByContractId",method = RequestMethod.POST)
    public InvokeResult<PageList<ContractStoreInfoViewModel>> getStoreInfoByContractId(@Validated  @RequestBody ContractStoreInfoQueryRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Page<StoreInfo> page = storeService.getContractStoreInfos(request.getContractId(),pageable);
        PageList<ContractStoreInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page,storeInfo ->{
            ContractStoreInfoViewModel contractStoreInfoViewModel  = ApiBeanUtils.copyProperties(storeInfo,ContractStoreInfoViewModel.class);
            contractStoreInfoViewModel.setCityId(getAreaName(storeInfo.getCityId()));
            contractStoreInfoViewModel.setRegionId(getAreaName(storeInfo.getRegionId()));
            return contractStoreInfoViewModel;
        });

        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "广告详情下的门店列表",notes = "创建人：毛向军")
    @RequestMapping(value="/queryStoreList",method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoViewModel>> queryStoreList(@Validated  @RequestBody ContractStoreQueryRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        PageList<StoreInfoViewModel> resultList = getStoreInfoViewModelPageList(request);
        if(resultList==null)
            throw new BusinessException("暂无匹配的投放门店数据");
        return InvokeResult.SuccessResult(resultList);
    }

    public PageList<StoreInfoViewModel> getStoreInfoViewModelPageList(ContractStoreQueryRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Page<ContractStoreInfo> pages = storeService.queryStoreList(request,pageable);
        List<String> storeIds = Linq4j.asEnumerable(pages.getContent()).select(p->p.getId()).toList();
        List<PeriodStoreProfitStatisticInfo> periodStoreProfitStatisticInfos = new ArrayList<>();
        if(storeIds!=null&&storeIds.size()!=0)
            periodStoreProfitStatisticInfos = periodStoreProfitStatisticService.getPeriodStoreProfitStatisticByContractId(new PeriodStoreProfitStatisticViewModel(storeIds,request.getAdvertisementId()));
        Map<String,PeriodStoreProfitStatisticInfo> periodStoreProfitStatisticInfoMap = Linq4j.asEnumerable(periodStoreProfitStatisticInfos).toMap(p->p.getStoreId(),p->p);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(pages, contractStoreInfo -> {
            StoreInfoViewModel storeInfoViewModel = ApiBeanUtils.copyProperties(contractStoreInfo, StoreInfoViewModel.class);
            storeInfoViewModel.setCityName(getAreaName(contractStoreInfo.getCityId(), areaMap));
            storeInfoViewModel.setProvinceName(getAreaName(contractStoreInfo.getProvinceId(), areaMap));
            storeInfoViewModel.setRegionName(getAreaName(contractStoreInfo.getRegionId(), areaMap));
            PeriodStoreProfitStatisticInfo periodStoreProfitStatisticInfo = periodStoreProfitStatisticInfoMap.get(contractStoreInfo.getId());
            if(periodStoreProfitStatisticInfo !=null){
                storeInfoViewModel.setTotalShareAmount(NumberFormatUtil.format( periodStoreProfitStatisticInfo.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
                storeInfoViewModel.setSettledAmount(NumberFormatUtil.format(periodStoreProfitStatisticInfo.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
                storeInfoViewModel.setUnsettledAmount(NumberFormatUtil.format((periodStoreProfitStatisticInfo.getShareAmount()*100 -periodStoreProfitStatisticInfo.getSettledAmount()*100)/100, Constant.SCALE_TWO, RoundingMode.HALF_UP));
                storeInfoViewModel.setShareAmountMinMonth(periodStoreProfitStatisticInfo.getStartDate());
                storeInfoViewModel.setShareAmountMaxMonth(periodStoreProfitStatisticInfo.getEndDate());
            }
            return storeInfoViewModel;
        });
    }


    private StoreInfoQueryRequest convertViewModelToRequestDto(StoreInfoPageInfoViewModel viewModel) {
        StoreInfoQueryRequest queryRequest = ApiBeanUtils.copyProperties(viewModel, StoreInfoQueryRequest.class);
        List<String> areaIds = Arrays.asList(viewModel.getAreaIds().split(","));
        queryRequest.setRegionIds(areaService.filterAreaIdsByLevel(areaIds, 3));
        queryRequest.setHasAbnormalNode(areaIds.contains(Constant.AREA_ABNORMAL_NODE_ID));//选中无省市区节点
        queryRequest.setHasTestNode(areaIds.contains(Constant.AREA_TEST_NODE_ID));
        queryRequest.setOffset(viewModel.getPageIndex() * viewModel.getPageSize());
        queryRequest.setPageSize(viewModel.getPageSize());
        queryRequest.setTopCount((viewModel.getPageIndex() + 1) * viewModel.getPageSize());

        if(viewModel.getHeartEndTime() == null) {
            queryRequest.setHeartEndTime(LocalDate.now().toDate());
        }
        if (viewModel.getLongitude() != null && viewModel.getLatitude() != null && viewModel.getDistance() != null) {
            queryRequest.setStoreDataMapInfo(DistanceUtil.returnSquarePoint(viewModel.getLongitude(), viewModel.getLatitude(), viewModel.getDistance()));
        }
        fillPaveGoodsInfo(queryRequest, viewModel);
        return queryRequest;
    }

    @ApiOperation(value = "查询门店详情",notes = "创建人：文丰")
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public InvokeResult<StoreAdvertisementInfoViewModel> get(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            throw new BusinessException("门店ID不能为空");
        StoreAdvertisementInfoViewModel viewModel=storeService.getStoreInfo(id);
        viewModel.setSurroundingsDistrict(getJointName(Linq4j.asEnumerable(storePortraitService.getStorePortrait(id)).select(q->q.getValue()).toList(),SurroundingsDistrictEnum.class));
        viewModel.setCityName(getAreaName(viewModel.getCityId()));
        viewModel.setCityCode(getAreaCode(viewModel.getCityId()));
        viewModel.setProvinceName(getAreaName(viewModel.getProvinceId()));
        viewModel.setProvinceCode(getAreaCode(viewModel.getProvinceId()));
        viewModel.setRegionName(getAreaName(viewModel.getRegionId()));
        viewModel.setRegionCode(getAreaCode(viewModel.getRegionId()));
        viewModel.setAdvertisementActiveRatio(NumberFormatUtil.format(viewModel.getAdvertisementActiveCount().longValue(),viewModel.getAdvertisementTotalCount().longValue(),Constant.RATIO_PATTERN));
        return InvokeResult.SuccessResult(viewModel);
    }


    @ApiOperation(value = "查询门店下的广告收益列表", notes = "创建者：毛向军")
    @RequestMapping(value = "/queryAdvertisementProfitShares", method = RequestMethod.POST)
    public InvokeResult<PageList<AdvertisementProfitSharePageInfoViewModel>> queryAdvertisementProfitShares(@Validated @RequestBody AdvertisementProfitSharePageInfoRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementProfitSharePageList(request));
    }

    private PageList<AdvertisementProfitSharePageInfoViewModel> getAdvertisementProfitSharePageList(AdvertisementProfitSharePageInfoRequest request) {
        Page<AdvertisementProfitStatisticInfo> pages = advertisementProfitStatisticService.getAdvertisementStoreProfitPeriodStatisticInfo(request);
        Map<String, Double> advertisementStoreProfitPeriodStatistic = advertisementProfitStatisticService.getAdvertisementStoreProfitPeriodStatistic(pages.map(d -> d.getAdvertisementId()).getContent(), request.getStoreId());
        Map<String, List<String>> advertisementDeliveryPlatforms = advertisementService.getAdvertisementDeliveryPlatforms(pages.map(d->d.getContractId()).getContent());
        return ApiBeanUtils.convertToPageList(pages, statistic->{
            AdvertisementProfitSharePageInfoViewModel viewModel = ApiBeanUtils.copyProperties(statistic,AdvertisementProfitSharePageInfoViewModel.class);
            Date effectiveEndTime = statistic.getEffectiveEndTime();
            viewModel.setId(statistic.getAdvertisementId());
            viewModel.setPeriod(DateUtils.formateYmd(DateUtils.getIntervalDays(statistic.getEffectiveStartTime(),effectiveEndTime==null?new Date():effectiveEndTime)));
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(statistic.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            viewModel.setAdvertisementType(EnumUtils.getDisplayName(statistic.getAdvertisementType(),MaterialTypeEnum.class));
            List<String> platforms = advertisementDeliveryPlatforms.get(statistic.getContractId());
            if(EnumUtils.toValueMap(TerminalTypeEnum.class).size()==platforms.size()){
                viewModel.setTerminalNames( Constant.ALL_PLATFORM);
            }else{
                viewModel.setTerminalNames( org.thymeleaf.util.StringUtils.join(platforms,Constant.DELIVERYPLATFORMSEPARATOR));
            }
            viewModel.setShareAmount(NumberFormatUtil.format(advertisementStoreProfitPeriodStatistic.getOrDefault(statistic.getAdvertisementId(),0D), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            viewModel.setOwner(getUserNickname(statistic.getOwnerId()));
            viewModel.setCanView(canView(Arrays.asList(statistic.getCreatorId(),statistic.getOwnerId())));
            return viewModel;
        });
    }

    @ApiOperation(value = "查询日流水明细详情下的广告单日收益列表", notes = "创建者：毛向军")
    @RequestMapping(value = "/queryAdvertisementProfitShareDays", method = RequestMethod.POST)
    public InvokeResult<PageList<AdvertisementProfitShareDaysPageInfoViewModel>> queryAdvertisementProfitShareDays(@Validated @RequestBody AdvertisementProfitShareDaysPageInfoRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementProfitShareDaysPageList(request));
    }

    private PageList<AdvertisementProfitShareDaysPageInfoViewModel> getAdvertisementProfitShareDaysPageList(AdvertisementProfitShareDaysPageInfoRequest request) {
        StoreProfitStatistic storeProfitStatistic =storeProfitStatisticService.getStoreProfitStatisticDay(request.getStoreProfitStatisticId());
        Page<AdvertisementProfitStatistic> pages = advertisementProfitStatisticService.getAdvertisementProfitDays(request);
        PageList<AdvertisementProfitShareDaysPageInfoViewModel> pageList = ApiBeanUtils.convertToPageList(pages, statistic -> {
            AdvertisementProfitShareDaysPageInfoViewModel viewModel = new AdvertisementProfitShareDaysPageInfoViewModel();
            BeanUtils.copyProperties(statistic.getAdvertisement(),viewModel);
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(statistic.getAdvertisement().getAdvertisementStatus(),AdvertisementStatusEnum.class));
            viewModel.setAdvertisementType(EnumUtils.getDisplayName(statistic.getAdvertisement().getAdvertisementType(),MaterialTypeEnum.class));
            viewModel.setActive(statistic.isActived());
            viewModel.setEnableProfitShare(statistic.isEnableProfitShare());
            viewModel.setShareAmountDayRatio(Constant.ZERO_PERCENT);
            Double shareAmountDay = statistic.getShareAmount();
            viewModel.setShareAmountDay(NumberFormatUtil.format(shareAmountDay, Constant.SCALE_TWO));
            if (shareAmountDay != null && shareAmountDay != 0D) {
                viewModel.setShareAmountDayRatio(NumberFormatUtil.format(shareAmountDay, storeProfitStatistic.getShareAmount(), Constant.RATIO_PATTERN));
            }
            viewModel.setOwner(getUserNickname(statistic.getContract().getOwnerId()));
            viewModel.setCanView(canView(Arrays.asList(statistic.getAdvertisement().getCreatorId(),statistic.getContract().getOwnerId())));
            return viewModel;
        });
        Double shareAmountCount = advertisementProfitStatisticService.getShareAmountCount(request);
        pageList.setTotalAmount(NumberFormatUtil.format(shareAmountCount, Constant.SCALE_TWO, RoundingMode.DOWN));
        return pageList;
    }

    @ApiOperation(value = "查询门店下的广告列表",notes = "创建人：文丰")
    @RequestMapping(value="/advertisements",method = RequestMethod.POST)
    public InvokeResult<PageList<AdvertisementStoreInfoItem>> getAdvertisementList(@Valid @RequestBody AdvertisementStoreInfoRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementStoreInfoItemPageList(request));
    }

    private PageList<AdvertisementStoreInfoItem> getAdvertisementStoreInfoItemPageList(AdvertisementStoreInfoRequest request) {
        Page<StoreAdvertisementInfo> page = advertisementService.getStoreAdvertisements(request);
        Map<String, List<String>> advertisementDeliveryPlatforms = advertisementService.getAdvertisementDeliveryPlatforms(page.map(d -> d.getContractId()).getContent());
        return ApiBeanUtils.convertToPageList(page, storeAdvertisementInfo -> {
            AdvertisementStoreInfoItem item = ApiBeanUtils.copyProperties(storeAdvertisementInfo,AdvertisementStoreInfoItem.class);
            item.setCustomerName(getCustomerName(storeAdvertisementInfo.getCustomerId()));
            item.setCanView(canView(Arrays.asList(storeAdvertisementInfo.getCreatorId(), storeAdvertisementInfo.getOwnerId())));
            List<String> platforms = advertisementDeliveryPlatforms.get(storeAdvertisementInfo.getContractId());
            if(EnumUtils.toValueMap(TerminalTypeEnum.class).size() == platforms.size()){
                item.setTerminalNames(Constant.ALL_PLATFORM);
            }else{
                item.setTerminalNames(org.apache.commons.lang3.StringUtils.join(platforms,Constant.DELIVERYPLATFORMSEPARATOR));
            }
            return item;
        });
    }

    @ApiOperation(value = "查询门店下的广告统计数据",notes = "创建人：文丰")
    @RequestMapping(value="/{id}/advertisementStatistic",method = RequestMethod.GET)
    public InvokeResult<AdvertisementStoreInfoStatisticViewModel> getAdvertisementStatistic(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("门店ID不能为空");
        return InvokeResult.SuccessResult(storeService.getAdvertisementStatisticByStoreId(id));
    }

    @ApiOperation(value = "统计门店是否使用总数", notes = "修改人 毛向军")
    @GetMapping(value = "allStoreInfoUseCountStatistic")
    public InvokeResult<StoreInfoUseCountStatistic> allStoreInfoUseCountStatistic() {
        return InvokeResult.SuccessResult(new StoreInfoUseCountStatistic(storeService.storeInfoUseCountStatistic().intValue(),storeService.getAllStoreInfoCount()));
    }

    @ApiOperation(value = "门店城市数量排名", notes = "创建人 王伟权")
    @PostMapping(value = "/StoreInfoAreaStatistic")
    public InvokeResult<StoreInfoAreaCountStatistic> storeInfoAreaStatistic(@Validated  @RequestBody StoreAreaStatisticRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        StoreInfoAreaCountStatistic storeInfoAreaCountStatistic = new StoreInfoAreaCountStatistic();
        List<StoreInfoAreaStatistic> storeInfoCountByCityStatistic = storeService.getStoreInfoUsedCountStatisticByCity(request);
        Integer allStoreCount=storeService.getAllStoreInfoCount();
        storeInfoCountByCityStatistic.forEach(item -> {
            item.setTotalStoreCountRatio(NumberFormatUtil.format(item.getStoreCount(),allStoreCount.longValue(), Constant.RATIO_PATTERN));
            item.setUsedStoreCountRatio(NumberFormatUtil.format(item.getUsedStoreCount().longValue(),item.getStoreCount(), Constant.RATIO_PATTERN));
            item.setAreaName(getAreaName(item.getAreaId()));
            item.setCode(getAreaCode(item.getAreaId()));
            storeInfoAreaCountStatistic.getStoreInfoAreaStatistics().add(item);
        });


        storeInfoAreaCountStatistic.getStoreInfoAreaStatistics().sort(Comparator.comparing(StoreInfoAreaStatistic::getStoreCount).reversed());
        return InvokeResult.SuccessResult(storeInfoAreaCountStatistic);
    }

    @ApiOperation(value = "导出门店下的广告收益列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportAdvertisementProfitShares",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementProfitShares(@Validated  @RequestBody AdvertisementProfitSharePageInfoRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","广告ID","广告名称","投放平台","广告类型","广告状态","投放时间","实际已投放天数","是否开启分成","分成金额","维护人"},
                new String[]{"id","advertisementName","terminalNames","advertisementType","advertisementStatusName","effectiveStartTime","period","enableProfitShare","shareAmount","owner"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementProfitSharePageInfoViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementProfitSharePageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("effectiveStartTime", TimeFormatEnum.Default);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="store_"+request.getStoreId()+"下的广告收益列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "导出日流水明细详情下的广告单日收益列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportAdvertisementProfitShareDays",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementProfitShareDays(@Validated  @RequestBody AdvertisementProfitShareDaysPageInfoRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","广告ID","广告名称","广告类型","广告状态","是否激活","是否开启分成","单日收益金额","收益占比","维护人"},
                new String[]{"id","advertisementName","advertisementType","advertisementStatusName","active","enableProfitShare","shareAmountDay","shareAmountDayRatio","owner"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementProfitShareDaysPageInfoViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementProfitShareDaysPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="store_"+storeProfitStatisticService.getStoreProfitStatisticDay(request.getStoreProfitStatisticId()).getStoreId()+"下的广告单日收益列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }
	
    @ApiOperation(value = "导出门店下的广告列表",notes = "创建人：文丰")
    @RequestMapping(value="/exportAdvertisements",method = RequestMethod.POST)
    public InvokeResult export(@Valid  @RequestBody AdvertisementStoreInfoRequest request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);

        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","广告ID","广告名称","合同名称","广告客户","投放平台","广告类型","是否开启分成","广告状态","是否激活","更新时间"},
                new String[]{"id","advertisementName","contractName","customerName","terminalNames","advertisementTypeName","enableProfitShare","advertisementStatusName","active","updatedTime"},
                HSSFCellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Integer index = 0;
        Integer totalPage;
        do{
            request.setPageIndex(index);
            val pageList = getAdvertisementStoreInfoItemPageList(request);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData = new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("updatedTime", TimeFormatEnum.Default);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index < totalPage);
        String filename = "门店广告详情 - 内部广告列表.xls";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "门店管理门店投放状况列表导出",notes = "创建人：文丰")
    @RequestMapping(value="/exportStoreInfoStatistic",method = RequestMethod.POST)
    public InvokeResult exportStoreInfoStatistic(@Validated  @RequestBody StoreInfoStatisticQueryRequest request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb=new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID", "是否达标","是否可用","是否使用","是否激活","是否铺货","有无门店画像"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId", "isQualified","available","used","active","isPaveGoods","isStorePortrait"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Integer index = 0;
        Integer totalPage;
        String filename="门店投放状况报表.xlsx";
        do{
            request.setPageIndex(index);
            PageList<StoreInfoStatisticViewModel> pageList = getStoreInfoStatisticViewModelPageList(request);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData = new ExcelData("第" + index + "页")
                    .addData(pageList.getList())
                    .addHeader(header)
                    .addBooleanTypeConfig(new HashMap<String,BooleanTypeEnum>(){
                        {
                            put("isStorePortrait",BooleanTypeEnum.HasOrNot);
                        }
                    });
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while( index<totalPage);
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="门店管理地图点位门店相关信息")
    @PostMapping(value = "/query/storePointPosition")
    public InvokeResult<PrimaryStoreInfoViewModel> getStoreInfo(@Validated  @RequestBody PrimaryStoreInfoRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(storeService.queryStoreInfo(request));
    }

	@ApiOperation(value = "获取推荐门店")
    @PostMapping(value = "/query")
    public InvokeResult<List<BaseStoreInfoViewModel>> queryStores(@Validated  @RequestBody RecommendedStoreRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        List<BaseStoreInfoViewModel> resultList=new ArrayList<>();
        if(StringUtils.isEmpty(request.getAddress()))
            return InvokeResult.SuccessResult(resultList);
        if(request.getCount()==null || request.getCount()<1){
            request.setCount(10);
        }
        List<StoreInfo> list=storeService.queryStores(request.getAddress(),request.getCount());
        if(list!=null && !list.isEmpty()){
            list.stream().forEach(item->{
                BaseStoreInfoViewModel viewModel=ApiBeanUtils.copyProperties(item,BaseStoreInfoViewModel.class);
                viewModel.setProvinceCode(getAreaCode(item.getProvinceId()));
                viewModel.setCityCode(getAreaCode(item.getCityId()));
                viewModel.setRegionCode(getAreaCode(item.getRegionId()));
                viewModel.setShopId(item.getStoreNo());
                viewModel.setProvinceName(getAreaName(item.getProvinceId()));
                viewModel.setCityName(getAreaName(item.getCityId()));
                viewModel.setRegionName(getAreaName(item.getRegionId()));
                resultList.add(viewModel);
            });
        }
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "获取坐标门店落点")
    @PostMapping(value = "/getStoreInfoInDistance")
    public InvokeResult<List<StorePlacementViewModel>> getStoreInfoInDistance(@Validated @RequestBody StoreDistanceViewModel request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        StoreInfoQueryRequest info = convertViewModelToRequestDto(request);
        return InvokeResult.SuccessResult(storeService.getStoreInfoByCoordinate(info));
    }

    @ApiOperation(value = "客户选点获取坐标门店落点")
    @PostMapping(value = "/getCustomerStoreInfoInDistance")
    public InvokeResult<List<CustomerStorePlacementViewModel>> getStoreInfoInDistance(@Validated @RequestBody CustomerStoreDistanceViewModel request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        CustomerStoreInfoQueryRequest info = convertCustomerStoreRequestToRequestDto(request);
        return InvokeResult.SuccessResult(storeService.getCustomerStoreInfoByCoordinate(info));
    }

    private CustomerStoreInfoQueryRequest convertCustomerStoreRequestToRequestDto(CustomerStoreDistanceViewModel viewModel) {
        CustomerStoreInfoQueryRequest queryRequest = ApiBeanUtils.copyProperties(viewModel, CustomerStoreInfoQueryRequest.class);
        List<String> areaIds = Arrays.asList(viewModel.getAreaIds().split(","));
        queryRequest.setRegionIds(areaService.filterAreaIdsByLevel(areaIds, 3));
        queryRequest.setHasAbnormalNode(areaIds.contains(Constant.AREA_ABNORMAL_NODE_ID));//选中无省市区节点
        queryRequest.setHasTestNode(Boolean.FALSE);
        queryRequest.setOffset(viewModel.getPageIndex() * viewModel.getPageSize());
        queryRequest.setPageSize(viewModel.getPageSize());
        queryRequest.setTopCount((viewModel.getPageIndex() + 1) * viewModel.getPageSize());

        if (viewModel.getLongitude() != null && viewModel.getLatitude() != null && viewModel.getDistance() != null) {
            queryRequest.setStoreDataMapInfo(DistanceUtil.returnSquarePoint(viewModel.getLongitude(), viewModel.getLatitude(), viewModel.getDistance()));
        }
        return queryRequest;
    }

    @ApiOperation(value = "查询投放该门店的第三方广告列表")
    @PostMapping(value = "/partnerAdvertisements")
    public InvokeResult<PageList<StorePartnerAdvertisementInfo>> getPartnerAdvertisements(@Validated @RequestBody StorePartnerAdvertisementRequest request,BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        if(request.getPartnerAdvertisementStatus().isEmpty())
            return emptyInvokeResult(request.getPageSize());
        Page<PartnerAdvertisementRequestInfo> pages=partnerAdvertisementService.getPartnerAdvertisementRequestInfoPages(request);
        if(pages==null || pages.getContent().isEmpty())
            return emptyInvokeResult(request.getPageSize());
        List<String> partnerAdvertisementIdList=pages.getContent().stream().map(a->a.getPartnerAdvertisementId()).collect(Collectors.toList());
        Map<String,PartnerAdvertisement> map=partnerAdvertisementService.getPartnerAdvertisementList(partnerAdvertisementIdList).stream().collect(Collectors.toMap(a->a.getId(),a->a));
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pages,temp->{
            return convertStorePartnerAdvertisementInfo(map, temp);
        }));
    }

    @ApiOperation(value = "导出投放该门店的第三方广告列表")
    @PostMapping(value = "/exportPartnerAdvertisements")
    public InvokeResult exportPartnerAdvertisements(@Validated @RequestBody StorePartnerAdvertisementRequest request,HttpServletResponse response,BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<PartnerAdvertisementRequestInfo> pages =null;
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        new String[]{"编号","广告ID","广告类型","合作方","合作模式","请求次数","展示次数","有效次数","有效比例","门店广告状态","更新时间"},
                        new String[]{"thirdPartId","materialTypeName","partnerName","cooperationPatternName","requestTimes","displayTimes","validTimes","validRatio","partnerAdvertisementStatusName","updatedTime"},
                        CellStyle.ALIGN_CENTER
                )
        ),new  HashMap<String, TimeFormatEnum>(){{
            put("updatedTime", TimeFormatEnum.Second);
        }});
        List<StorePartnerAdvertisementInfo> storePartnerAdvertisementInfoList=new ArrayList<>();
        Integer index=0;
        Long count=0L;
        request.setPageSize(Constant.QUERY_RECORD_SIZE);
        do{
            request.setPageIndex(index);
            sheetConfig.setSheetNameTemplate("第%d页");
            pages=partnerAdvertisementService.getPartnerAdvertisementRequestInfoPages(request);
            if(pages.getTotalElements()>0){
               count=pages.getTotalElements();
                List<String> partnerAdvertisementIdList=pages.getContent().stream().map(a->a.getPartnerAdvertisementId()).collect(Collectors.toList());
                Map<String,PartnerAdvertisement> map=partnerAdvertisementService.getPartnerAdvertisementList(partnerAdvertisementIdList).stream().collect(Collectors.toMap(a->a.getId(),a->a));
                storePartnerAdvertisementInfoList=pages.getContent().stream().map(temp->{
                    return convertStorePartnerAdvertisementInfo(map, temp);
                }).collect(Collectors.toList());
            }
            myWorkbook.addData(storePartnerAdvertisementInfoList,sheetConfig);
        }while(count>(++index)*request.getPageSize());
        String filename="第三方广告列表"+".xlsx";
        outputStream(filename, response, myWorkbook);

        return InvokeResult.SuccessResult();

    }

    private StorePartnerAdvertisementInfo convertStorePartnerAdvertisementInfo(Map<String, PartnerAdvertisement> map, PartnerAdvertisementRequestInfo temp) {
        StorePartnerAdvertisementInfo item= ApiBeanUtils.copyProperties(temp,StorePartnerAdvertisementInfo.class);
        PartnerAdvertisement partnerAdvertisement=map.get(temp.getPartnerAdvertisementId());
        item.setCanView(canView(Arrays.asList(partnerAdvertisement.getPartner().getOwnerId())));
        if(partnerAdvertisement!=null){
            item.setCooperationPatternName(EnumUtils.getDisplayName(partnerAdvertisement.getPartner().getCooperationPattern(),CooperationPatternEnum.class));
            item.setPartnerName(partnerAdvertisement.getPartner().getName());
        }
        if(item.getUpdateTime()!=null){
            item.setUpdatedTime(new Date(item.getUpdateTime()));
        }
        Integer advertisementStatus=item.getAdvertisementStatus();
        if(partnerAdvertisement!=null && partnerAdvertisement.getAdvertisementStatus().equals(PartnerAdvertisementStatusEnum.TakeOff.getValue())){
            advertisementStatus=PartnerAdvertisementStatusEnum.TakeOff.getValue();
            item.setAdvertisementStatus(advertisementStatus);
        }
        item.setPartnerAdvertisementStatusName(EnumUtils.getDisplayName(advertisementStatus,PartnerAdvertisementStatusEnum.class));
        item.setMaterialTypeName(EnumUtils.getDisplayName(temp.getMaterialType(),MaterialTypeEnum.class));
        return item;
    }

    @ApiOperation(value = "查询放该门店有投放广告的合作方")
    @GetMapping(value = "/{id}/partners")
    public InvokeResult<List<BasePartnerInfo>> queryStorePartners(@PathVariable("id")String id){
        return InvokeResult.SuccessResult(partnerAdvertisementService.queryBasePartnerInfo(id));
    }

    @ApiOperation("门店广告填充趋势")
    @PostMapping("/storeAdFillTrend")
    public InvokeResult<List<StoreAdFillTrendViewModel>> storeAdFillTrend(@Valid @RequestBody StoreAdFillTrendRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);
        if (request.getBeginDate().after(request.getEndDate())) throw new BusinessException("开始日期不能大于结束日期");

        List<StoreAdFillTrendViewModel> list;
        Date nowDate = LocalDate.now().toDate();
        if (nowDate.equals(request.getBeginDate())) {
            val storeNumStatistics = storeService.getStoreNumStatisticsByNow();
            val viewModel = ApiBeanUtils.copyProperties(storeNumStatistics, StoreAdFillTrendViewModel.class);
            viewModel.setDate(nowDate);
            list = Arrays.asList(viewModel);
        } else {
            val storeNumMap = storeService.getStoreNumStatistics(request);
            val dateList = DateUtils.getDateList(request.getBeginDate(), request.getEndDate(), Calendar.DATE);
            list = dateList.stream().map(date -> {
                val storeNumStatistics = nowDate.equals(date) ? storeService.getStoreNumStatisticsByNow() : storeNumMap.get(date);
                val viewModel = ApiBeanUtils.copyProperties(Optional.ofNullable(storeNumStatistics).orElse(new StoreNumStatistics()), StoreAdFillTrendViewModel.class);
                viewModel.setDate(date);
                return viewModel;
            }).collect(Collectors.toList());
        }
        return InvokeResult.SuccessResult(list);
    }
}
