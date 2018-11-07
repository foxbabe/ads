package com.sztouyun.advertisingsystem.api.advertisement;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractExtension;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitConfig;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementSettlementService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.MaterialItemViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.statistic.ContractAdvertisementDeliveryStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.val;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by wenfeng on 2017/8/4.
 */

@Api("广告接口")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController extends BaseApiController {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementOperationService advertisementOperationService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private AdvertisementSettlementService advertisementSettlementService;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QStoreInfo qStoreInfo=QStoreInfo.storeInfo;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "所有广告状态", notes = "创建者：李川")
    @RequestMapping(value = "statuses", method = RequestMethod.GET)
    public InvokeResult<Map<Integer,String>> queryAdvertisementStatusList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return InvokeResult.SuccessResult(EnumUtils.toValueMap(AdvertisementStatusEnum.class));
    }


    @ApiOperation(value = "查询广告操作", notes = "创建者：毛向军")
    @RequestMapping(value = "/operate/query", method = RequestMethod.POST)
    public InvokeResult<PageList<AdvertisementOperationInfoViewModel>> queryOperateAdvertisement(@Validated @RequestBody AdvertisementOperationPageInfoRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<AdvertisementOperationLog> page = advertisementOperationService.queryOperateAdvertisement(request);
        PageList<AdvertisementOperationInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page,advertisementOperationLog->{
            AdvertisementOperationInfoViewModel advertisementOperationInfoViewModel = new AdvertisementOperationInfoViewModel();
            BeanUtils.copyProperties(advertisementOperationLog,advertisementOperationInfoViewModel);
            advertisementOperationInfoViewModel.setOperationStatus(advertisementOperationLog.getOperation(),advertisementOperationLog.isSuccessed());
            advertisementOperationInfoViewModel.setOperator(getUserNickname(advertisementOperationLog.getCreatorId()));
            advertisementOperationInfoViewModel.setOperateTime(advertisementOperationLog.getCreatedTime());
            return advertisementOperationInfoViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "新增广告", notes = "创建人：文丰")
    @PostMapping(value = "/create")
    public InvokeResult<String> createAdvertisement(@Valid @RequestBody CreateAdvertisementViewModel viewModel, BindingResult result) {
        if(result.hasErrors()) return ValidateFailResult(result);

        return InvokeResult.SuccessResult(saveAdvertisement(viewModel,true));
    }

    private String saveAdvertisement(BaseAdvertisementViewModel viewModel, boolean isNew) {
        if (viewModel.isEnableProfitShare() && Objects.isNull(viewModel.getAdvertisementProfitConfig()))
            throw new BusinessException("请配置广告分成！");

        val materials = viewModel.getMaterialList();
        checkQRCodePosition(materials);

        val advertisement = ApiBeanUtils.copyProperties(viewModel, Advertisement.class);
        AdvertisementProfitConfig profitConfig = viewModel.getAdvertisementProfitConfig();
        if (viewModel.isEnableProfitShare()) {
            advertisement.setMode(profitConfig.getMode());
        }
        if(isNew){
            advertisement.setId(null);
        }

        advertisementService.saveAdvertisement(advertisement, materials, profitConfig);
        return advertisement.getId();
    }

    /**
     * 校验素材二维码位置
     */
    private void checkQRCodePosition(List<MaterialItemViewModel> materialList) {
        materialList.forEach(materialItemViewModel -> {
            if (!StringUtils.isEmpty(materialItemViewModel.getMaterialQRCodeUrl()) && materialItemViewModel.getQRCodePosition() == null)
                throw new BusinessException("请选择二维码位置");
        });
    }

    @ApiOperation(value = "修改广告", notes = "创建人：文丰")
    @PostMapping(value = "/update")
    public InvokeResult updateAdvertisement(@Valid @RequestBody UpdateAdvertisementViewModel viewModel, BindingResult result) {
        if(result.hasErrors()) return ValidateFailResult(result);

        saveAdvertisement(viewModel,false);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询广告回显信息", notes = "创建人：文丰")
    @RequestMapping(value = "/{id}/editInfo", method = RequestMethod.GET)
    public InvokeResult<AdvertisementReapperViewModel> getAdvertisementEditInfo(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        Advertisement advertisement= advertisementService.getAdvertisementAuthorized(id);
        if (advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.PendingAuditing.getValue()) && getUser().getRoleTypeEnum().equals(RoleTypeEnum.SaleMan)) {
            return InvokeResult.Fail("您没有该操作权限");
        }
        AdvertisementReapperViewModel detailViewModel=new AdvertisementReapperViewModel();
        BeanUtils.copyProperties(advertisement,detailViewModel);
        if(advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue())){
             detailViewModel.setData(advertisement.getData());
        }
        if(advertisement.isEnableProfitShare()){
            detailViewModel.setAdvertisementProfitConfig(advertisementService.getAdvertisementProfitConfig(id));
        }
        List<TerminalAdvertisementConfigInfo> Configs=advertisementService.getAdvertisementMaterialInfo(id,true);
        detailViewModel.setMaterialItems(Configs);
        return InvokeResult.SuccessResult(detailViewModel);
    }

    @ApiOperation(value = "删除广告信息", notes = "创建人：文丰")
    @PostMapping(value = "/{id}/delete")
    public InvokeResult delete(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        advertisementService.deleteAdvertisement(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "统计广告状态信息", notes = "创建人: 文丰")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<AdvertisementStatusViewModel> retrieveContractStatusStatistics(@Validated @RequestBody AdvertisementStatusCountViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementStatusViewModel viewModelResult = advertisementService.getAdvertisementStatusStatistics(viewModel);
        return InvokeResult.SuccessResult(viewModelResult);
    }

    @ApiOperation(value = "查询广告列表", notes = "创建人：文丰")
    @PostMapping(value = "")
    public InvokeResult<PageList<AdvertisementListItemViewModel>> getAdvertisementList(@Validated @RequestBody AdvertisementPageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        viewModel.setJoinDescriptor(new JoinDescriptor().leftJoin(qAdvertisement.contract));
        Page<Advertisement> pages = advertisementService.queryAdvertisementList(viewModel);
        PageList<AdvertisementListItemViewModel> resultList= ApiBeanUtils.convertToPageList(pages, advertisement ->
        {
            AdvertisementListItemViewModel advertisementListItemView=new AdvertisementListItemViewModel();
            BeanUtils.copyProperties(advertisement,advertisementListItemView);
            setAdvertisementListWithContractInfo(advertisementListItemView,advertisement);
            advertisementListItemView.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            advertisementListItemView.setTerminalNames(getPlatformName(advertisement.getContract().getPlatform()));
            return advertisementListItemView;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    private void setAdvertisementListWithContractInfo(AdvertisementListItemViewModel advertisementListItemView,Advertisement advertisement){
        Contract contract=advertisement.getContract();
        advertisementListItemView.setContractName(contract.getContractName());
        advertisementListItemView.setCustomerName(getCustomerName(contract.getCustomerId()));
        advertisementListItemView.setAdvertisementType(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
        advertisementListItemView.setTotalCost(NumberFormatUtil.format(contract.getTotalCost()));
        advertisementListItemView.setOwner(getUserNickname(contract.getOwnerId()));
        advertisementListItemView.setPeriod(DateUtils.formateYmd(advertisement.getAdvertisementPeriod()));
    }

    @ApiOperation(value = "查询广告详情", notes = "创建人: 文丰")
    @GetMapping(value = "/{id}")
    public InvokeResult<DetailAdvertisementViewModel> getAdvertisement(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            throw  new BusinessException("广告ID不能为空");
        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(id);
        DetailAdvertisementViewModel detailAdvertisementViewModel=getDetailAdvertisementViewModel(advertisement);
        detailAdvertisementViewModel.setMaterialItems(advertisementService.getAdvertisementMaterialInfo(id,false));
        return InvokeResult.SuccessResult( detailAdvertisementViewModel);
    }

    private DetailAdvertisementViewModel getDetailAdvertisementViewModel(Advertisement advertisement){
        DetailAdvertisementViewModel detailAdvertisementViewModel=ApiBeanUtils.copyProperties(advertisement,DetailAdvertisementViewModel.class);
        detailAdvertisementViewModel.setCreator(getUserNickname(advertisement.getCreatorId()));
        detailAdvertisementViewModel.setCustomerName(getCustomerName(advertisement.getCustomerId()));
        detailAdvertisementViewModel.setPortrait(advertisement.getCustomer().getHeadPortrait());
        detailAdvertisementViewModel.setDeliveryOperator(getUserNickname(advertisement.getDeliveryOperatorId()));

        detailAdvertisementViewModel.setAdvertisementTypeName(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
        detailAdvertisementViewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
        detailAdvertisementViewModel.setAdvertisementPeriod(DateUtils.formateYmd(advertisement.getAdvertisementPeriod()));
        detailAdvertisementViewModel.setStartTime(advertisement.getStartTime());
        detailAdvertisementViewModel.setEndTime(advertisement.getEndTime());
        detailAdvertisementViewModel.setOwner(getUserNickname(advertisement.getContract().getOwnerId()));
        setContractInfo(detailAdvertisementViewModel,advertisement);
        detailAdvertisementViewModel.setId(advertisement.getId());// 广告ID被合同ID覆盖
        detailAdvertisementViewModel.setRemark(advertisement.getRemark());// 广告备注被覆盖
        setDeliveryAndTakeOffInfo(detailAdvertisementViewModel,advertisement);
        if(advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue())){
            detailAdvertisementViewModel.setData(advertisement.getData());
        }
        detailAdvertisementViewModel.setUpdatedTime(advertisement.getUpdatedTime());
        detailAdvertisementViewModel.setCreatedTime(advertisement.getCreatedTime());
        detailAdvertisementViewModel.setPeriod(DateUtils.formateYmd(advertisement.getEffectivePeriod()));

        double shareAmount, settledAmount, unsettledAmount;
        if (Objects.equals(AdvertisementProfitModeEnum.FixedAmount.getValue(), advertisement.getMode())) {
            shareAmount = advertisement.getAdvertisementProfit().getShareAmount();
            settledAmount = advertisement.getAdvertisementProfit().getSettledAmount();
            unsettledAmount = advertisement.getAdvertisementProfit().getUnsettledAmount();
        } else {
            shareAmount = advertisement.getShareAmount() / 100.0;
            settledAmount = advertisement.getSettledAmount() / 100.0;
            unsettledAmount = shareAmount - settledAmount;
        }
        detailAdvertisementViewModel.setShareAmount(formatAmount(shareAmount));
        detailAdvertisementViewModel.setSettledAmount(formatAmount(settledAmount));
        detailAdvertisementViewModel.setUnsettledAmount(formatAmount(unsettledAmount));

        detailAdvertisementViewModel.setAdvertisementProfitConfig(advertisementService.getAdvertisementProfitConfig(advertisement.getId()));
        return detailAdvertisementViewModel;
    }

    /**
     * 格式化金额
     */
    private String formatAmount(Double amount) {
        return NumberFormatUtil.format(amount, Constant.SCALE_TWO, RoundingMode.HALF_UP);
    }

    /**
     * 设置合同相关信息
     * @param detailAdvertisementViewModel
     * @param advertisement
     */
    public void setContractInfo(DetailAdvertisementViewModel detailAdvertisementViewModel,Advertisement advertisement){
        Contract contract=advertisement.getContract();
        ContractExtension contractExtension=contract.getContractExtension();
        BeanUtils.copyProperties(contract,detailAdvertisementViewModel);
        BeanUtils.copyProperties(contractExtension,detailAdvertisementViewModel);
        detailAdvertisementViewModel.setTotalCost(NumberFormatUtil.format(contract.getTotalCost()));
        if(contractExtension.getDiscount()<1){
            detailAdvertisementViewModel.setIsDiscount(1);
        }
        detailAdvertisementViewModel.setDiscount(NumberFormatUtil.format(contractExtension.getDiscount()));
        //合同与广告起止时间的属性同名，会覆盖
        detailAdvertisementViewModel.setStartTime(advertisement.getStartTime());
        detailAdvertisementViewModel.setEndTime(advertisement.getEndTime());
        detailAdvertisementViewModel.setStartTimeOfContract(contractExtension.getStartTime());
        detailAdvertisementViewModel.setEndTimeOfContract(contractExtension.getEndTime());
        detailAdvertisementViewModel.setContractPeriod(DateUtils.formateYmd(contract.getContractAdvertisementPeriod()));
        detailAdvertisementViewModel.setUsedContractPeriod(DateUtils.formateYmd(contract.getUsedContractPeriod()));
    }

    /**
     * 设置投放及下架信息
     * @param detailAdvertisementViewModel
     * @param advertisement
     * @return
     */
    private DetailAdvertisementViewModel setDeliveryAndTakeOffInfo(DetailAdvertisementViewModel detailAdvertisementViewModel,Advertisement advertisement){
        detailAdvertisementViewModel.setDeliveryOperator(getUserNickname(advertisement.getDeliveryOperatorId()));
        if(advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.TakeOff.getValue())){
            AdvertisementOperationLog advertisementOperationLog=advertisementOperationService.findTakeOffLog(advertisement.getId());
            detailAdvertisementViewModel.setTakeOffTime(advertisementOperationLog.getCreatedTime());
            detailAdvertisementViewModel.setTakeOffOperator(getUserNickname(advertisementOperationLog.getCreatorId()));
            detailAdvertisementViewModel.setTakeOffRemark(advertisementOperationLog.getRemark());
        }
        return detailAdvertisementViewModel;
    }

    @ApiOperation(value = "操作广告(code:-2 表示失败时备注说明不能为空！)", notes = "创建人: 李川")
    @PostMapping(value = "/operate")
    public InvokeResult operateAdvertisement(@Validated @RequestBody AdvertisementOperationViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (!viewModel.isSuccessed() && StringUtils.isEmpty(viewModel.getRemark()))
            return InvokeResult.Fail("说明不能为空！", -2);
        //广告不能直接执行完成
        if(viewModel.getOperation().equals(AdvertisementOperationEnum.Finish.getValue()) && viewModel.isSuccessed())
            return InvokeResult.Fail("不支持本操作！", -2);

        AdvertisementOperationLog operationLog = new AdvertisementOperationLog();
        BeanUtils.copyProperties(viewModel, operationLog);
        advertisementOperationService.operate(operationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "统计上下架, 高风险广告数据量", notes = "创建人：王伟权")
    @PostMapping(value = "/deliveringStatistics")
    public InvokeResult<List<AdvertisementStatusStatisticsViewModel>> getAdvertisementDeliveringStatistics(@Validated @RequestBody AdvertisementStatusCountViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(advertisementService.getAdvertisementDeliveringStatistics(viewModel));
    }
	
	@ApiOperation(value = "广告投放地区统计列表", notes = "创建人: 文丰")
    @PostMapping(value = "/areaCountList")
    public InvokeResult<PageList<AdvertisementReportInfoViewModel>> getAdvertisementAreaList(@Validated @RequestBody AdvertisementAreaPageInfoViewModel viewModel,BindingResult result ) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable=new PageRequest(viewModel.getPageIndex(),viewModel.getPageSize(),new QSort(QStoreInfo.storeInfo.cityId.asc()));
        Page<AdvertisementAreaCountInfo> page=advertisementService.getAdvertisementReportInfo(viewModel.getId(),pageable);
        PageList<AdvertisementReportInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page, advertisementAreaCountInfo -> {
            AdvertisementReportInfoViewModel item =ApiBeanUtils.copyProperties(advertisementAreaCountInfo,AdvertisementReportInfoViewModel.class);
            item.setCityName(areaService.getAreaNameFromCache(advertisementAreaCountInfo.getCityId()));
            item.setProvinceName((areaService.getParentAreaNameFromCache(advertisementAreaCountInfo.getCityId())));
            return item;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "合同广告投放统计", notes = "创建人: 王伟权")
    @GetMapping("/{contractId}/advertisingHistory")
    public InvokeResult<ContractAdvertisementDeliveryViewModel> getContractAdvertisementDeliveryStatistics(@PathVariable("contractId") String contractId) {
        if(StringUtils.isEmpty(contractId))
            throw new BusinessException("合同ID不能为空");
        Contract contract = contractService.getContract(contractId);
        if (contract == null)
            throw new BusinessException("合同数据不存在");
        ContractAdvertisementDeliveryStatistic contractAdvertisementDeliveryStatistics = advertisementService.getContractAdvertisementDeliveryStatistics(contract);
        ContractAdvertisementDeliveryViewModel contractAdvertisementDeliveryViewModel = ApiBeanUtils.copyProperties(contractAdvertisementDeliveryStatistics, ContractAdvertisementDeliveryViewModel.class);
        contractAdvertisementDeliveryViewModel.setContractName(contract.getContractName());
        contractAdvertisementDeliveryViewModel.setStartTime(contract.getContractExtension().getStartTime());
        contractAdvertisementDeliveryViewModel.setEndTime(contract.getContractExtension().getEndTime());
        contractAdvertisementDeliveryViewModel.setAdvertisePeriod(contract.getContractAdvertisementPeriod());
        contractAdvertisementDeliveryViewModel.setHasAdvertisedPeriod(contract.getUsedContractPeriod());
        return InvokeResult.SuccessResult(contractAdvertisementDeliveryViewModel);
    }

    @ApiOperation(value = "门店日流水列表", notes = "创建人: 毛向军")
    @PostMapping(value = "/storeDailyProfitList")
    public InvokeResult<PageList<AdvertisementStoreDailyProfitViewModel>> getAdvertisementStoreDailyProfitList(@Validated @RequestBody AdvertisementStoreDailyProfitRequest request,BindingResult result ) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementStoreDailyProfitViewModelPageList(request));
    }

    private PageList<AdvertisementStoreDailyProfitViewModel> getAdvertisementStoreDailyProfitViewModelPageList(@RequestBody @Validated AdvertisementStoreDailyProfitRequest request) {
        List<AdvertisementStoreDailyProfitViewModel> list = advertisementService.getAdvertisementStoreDailyProfitList(request);
        Pageable pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        list.stream().forEach(item -> {
            item.setCityName(getAreaName(item.getCityId()));
            item.setProvinceName(getAreaName(item.getProvinceId()));
            item.setRegionName(getAreaName(item.getRegionId()));
        });
        AdvertisementStoreDailyProfitGroupInfo info = advertisementService.getAdvertisementStoreDailyProfitGroupInfo(request);
        PageList<AdvertisementStoreDailyProfitViewModel> pageList = ApiBeanUtils.convertToPageList(pageResult(list, pageRequest, info.getCount()));
        pageList.setTotalAmount(NumberFormatUtil.format(info.getTotalAmount()/100.0, Constant.SCALE_TWO, RoundingMode.DOWN));
        return pageList;
    }

    @ApiOperation(value = "导出门店日流水列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreDailyProfitList",method = RequestMethod.POST)
    public InvokeResult exportStoreDailyProfitList(@Validated  @RequestBody AdvertisementStoreDailyProfitRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID","分成时间","分成金额"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId","profitDate","shareAmount"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementStoreDailyProfitViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementStoreDailyProfitViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="投放点计费 - 门店日流水";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "获取广告投放的门店的所有城市", notes = "创建人: 李海峰")
    @GetMapping("/{id}/getCitys")
    public InvokeResult getCitys(@ApiParam("合同ID") @PathVariable String id) {
        return InvokeResult.SuccessResult(advertisementService.getCitys(id));
    }

    @ApiOperation(value = "广告详情下的门店列表",notes = "创建人：毛向军")
    @RequestMapping(value="/storeList",method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoViewModel>> storeList(@Validated  @RequestBody ContractStoreQueryRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getStoreInfoViewModelPageList(request));
    }

    private PageList<StoreInfoViewModel> getStoreInfoViewModelPageList(ContractStoreQueryRequest request) {
        Page<ContractStoreInfo> pages = advertisementService.storeList(request);
        String advertisementId=request.getAdvertisementId();
        Advertisement advertisement = advertisementService.getAdvertisement(advertisementId);
        List<String> storeIds=Linq4j.asEnumerable(pages.getContent()).select(p -> p.getId()).toList();
        final Map<String, AdvertisementSettlementStatistic> advertisementSettlementStatisticsMap;
        Map<String, String> areaMap = areaService.getAllAreaNames();
        if(!advertisement.getMode().equals(AdvertisementProfitModeEnum.DeliveryEffect.getValue())) {
            advertisementSettlementStatisticsMap= advertisementService.getAdvertisementSettlementStatisticsMap(advertisementId, storeIds);
        }else{
            advertisementSettlementStatisticsMap=advertisementSettlementService.getEffectProfitStoreSettleInfoMap(advertisementId,storeIds);
        }
        return ApiBeanUtils.convertToPageList(pages, contractStoreInfo -> {
            StoreInfoViewModel storeInfoViewModel = ApiBeanUtils.copyProperties(contractStoreInfo, StoreInfoViewModel.class);
            storeInfoViewModel.setCityName(getAreaName(contractStoreInfo.getCityId(), areaMap));
            storeInfoViewModel.setProvinceName(getAreaName(contractStoreInfo.getProvinceId(), areaMap));
            storeInfoViewModel.setRegionName(getAreaName(contractStoreInfo.getRegionId(), areaMap));
            AdvertisementSettlementStatistic advertisementSettlementStatistic = advertisementSettlementStatisticsMap.get(contractStoreInfo.getId());
             if (advertisementSettlementStatistic != null) {
                storeInfoViewModel.setTotalShareAmount(NumberFormatUtil.format(advertisementSettlementStatistic.getTotalShareAmount() / 100.0, Constant.SCALE_TWO, RoundingMode.HALF_UP));
                if(!advertisement.getMode().equals(AdvertisementProfitModeEnum.DeliveryEffect.getValue())){
                    storeInfoViewModel.setSettledAmount(NumberFormatUtil.format(advertisementSettlementStatistic.getSettledAmount() / 100.0, Constant.SCALE_TWO, RoundingMode.HALF_UP));
                    storeInfoViewModel.setUnsettledAmount(NumberFormatUtil.format((advertisementSettlementStatistic.getTotalShareAmount() - advertisementSettlementStatistic.getSettledAmount()) / 100.0, Constant.SCALE_TWO, RoundingMode.HALF_UP));
                }
            }
            if(advertisement.getMode().equals(AdvertisementProfitModeEnum.DeliveryEffect.getValue())){
                storeInfoViewModel.setSettledAmount("--");
                storeInfoViewModel.setUnsettledAmount("--");
            }
            return storeInfoViewModel;
        });
    }

    @ApiOperation(value = "导出广告详情下的门店列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreList",method = RequestMethod.POST)
    public InvokeResult exportStoreList(@Validated  @RequestBody ContractStoreQueryRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID","是否达标","是否可用","分成金额","已结算","未结算"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId","isQualified","available","totalShareAmount","settledAmount","unsettledAmount"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);

        PageList<StoreInfoViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getStoreInfoViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="广告详情下的门店列表";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }
}