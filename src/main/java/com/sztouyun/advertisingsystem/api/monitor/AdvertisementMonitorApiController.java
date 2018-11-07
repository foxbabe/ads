package com.sztouyun.advertisingsystem.api.monitor;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.api.store.StoreInfoApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.monitor.*;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.contract.ContractAdvertisementPositionConfigService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.monitor.AdvertisementMonitorStatisticService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.*;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementMaterialPositionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementMaterialViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.monitor.*;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNameInfo;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Api(value = "广告监控管理接口")
@RestController
@RequestMapping("/api/advertisement/monitor")
public class AdvertisementMonitorApiController extends BaseApiController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private StoreInfoApiController storeInfoApiController;
    @Autowired
    private AdvertisementMonitorStatisticService advertisementMonitorStatisticService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private ContractAdvertisementPositionConfigService contractAdvertisementPositionConfigService;
    @Autowired
    private StoreInfoMapper storeInfoMapper;

    @ApiOperation(value = "广告监控管理列表", notes = "创建人: 毛向军")
    @PostMapping(value = "")
    public InvokeResult<PageList<AdvertisementMonitorStatisticViewModel>> advertisementMonitorStatistic(@Validated @RequestBody AdvertisementMonitorStatisticRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<AdvertisementMonitorStatistic> page=advertisementMonitorStatisticService.getAdvertisementMonitorStatistic(request);
        PageList<AdvertisementMonitorStatisticViewModel> pageList=ApiBeanUtils.convertToPageList(page, advertisementMonitorStatistic-> getAdvertisementMonitorStatisticViewModel( advertisementMonitorStatistic));
        return InvokeResult.SuccessResult(pageList);
    }

    private AdvertisementMonitorStatisticViewModel getAdvertisementMonitorStatisticViewModel( AdvertisementMonitorStatistic advertisementMonitorStatistic) {
        AdvertisementMonitorStatisticViewModel item= ApiBeanUtils.copyProperties(advertisementMonitorStatistic,AdvertisementMonitorStatisticViewModel.class);
        Advertisement advertisement=advertisementMonitorStatistic.getAdvertisement();
        Contract contract=advertisementMonitorStatistic.getContract();
        String platform=contract.getPlatform();
        BeanUtils.copyProperties(contract,item);
        BeanUtils.copyProperties(advertisement,item);
        item.setStartTime(advertisementMonitorStatistic.getCreatedTime());
        item.setEndTime(advertisementMonitorStatistic.getEndTime());
        item.setOwner(getUserNickname(contract.getOwnerId()));
        item.setCustomerName(getCustomerName(contract.getCustomerId()));
        MonitorStatusEnum monitorStatusEnum = getMonitorStatusEnum(advertisement.getAdvertisementStatus());
        item.setStatus(monitorStatusEnum.getValue());
        item.setStatusName(monitorStatusEnum.getDisplayName());
        item.setChoseCashRegister(contract.isChoseCashRegister());
        item.setPlatform(getPlatformName(contract.getPlatform()));
        Date endTime = advertisementMonitorStatistic.getEndTime();
        item.setDeliveryPeroid(DateUtils.formateYmd(DateUtils.getIntervalDays(advertisementMonitorStatistic.getCreatedTime(),endTime==null?new Date():endTime)));
        item.setUpdatedTime(advertisementMonitorStatistic.getUpdatedTime());
        var totalDisplayTimes =advertisementMonitorStatistic.getTotalDisplayTimes();
        if(platform.contains(TerminalTypeEnum.IOS.getValue().toString()) || platform.contains(TerminalTypeEnum.Android.getValue().toString())){
            Map<Integer, Integer> allMobileTerminalTypeDisplayTimes = advertisementMonitorStatisticService.getTerminalDisplayTimes(advertisement.getId(),Arrays.asList(TerminalTypeEnum.Android,TerminalTypeEnum.IOS));
            if(allMobileTerminalTypeDisplayTimes!=null){
                totalDisplayTimes += allMobileTerminalTypeDisplayTimes.getOrDefault(TerminalTypeEnum.IOS.getValue(),0)+allMobileTerminalTypeDisplayTimes.getOrDefault(TerminalTypeEnum.Android.getValue(),0);
            }
        }
        item.setTotalDisplayTimes(totalDisplayTimes);
        return item;
    }

    @ApiOperation(value = "广告投放监控详情", notes = "创建人: 毛向军")
    @GetMapping(value = "/{advertisementId}/detail")
    public InvokeResult<AdvertisementMonitorDetailViewModel> queryAdvertisementMonitorDetail(@PathVariable("advertisementId") String advertisementId) {
        AdvertisementMonitorStatistic advertisementMonitorStatistic = advertisementMonitorStatisticService.queryAdvertisementMonitorDetail(advertisementId);
        if(advertisementMonitorStatistic == null)
            return InvokeResult.Fail("广告不存在！");

        AdvertisementMonitorDetailViewModel viewModel = ApiBeanUtils.copyProperties(getAdvertisementMonitorStatisticViewModel(advertisementMonitorStatistic),AdvertisementMonitorDetailViewModel.class);
        Advertisement advertisement=advertisementMonitorStatistic.getAdvertisement();
        viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
        viewModel.setAdvertisementTypeName(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
        viewModel.setHeadPortrait(advertisementMonitorStatistic.getCustomer().getHeadPortrait());
        viewModel.setMonitorStartTime(advertisementMonitorStatistic.getCreatedTime());
        viewModel.setIsConfiguredUrl(isConfiguredUrl(advertisementId));
        if(MonitorStatusEnum.Finished.getValue().equals(viewModel.getStatus())){
            Date endTime = advertisementMonitorStatistic.getEndTime();
            viewModel.setMonitorFinishedTime(endTime);
            viewModel.setMonitorPeriod(DateUtils.formateYmd(DateUtils.getIntervalDays(advertisementMonitorStatistic.getCreatedTime(),endTime==null?new Date():endTime)));
        }
        return InvokeResult.SuccessResult(viewModel);
    }

    private MonitorStatusEnum getMonitorStatusEnum(Integer advertisementStatus){
        if(advertisementStatus.equals(AdvertisementStatusEnum.Delivering.getValue())){
            return MonitorStatusEnum.OnWatching;
        }else if(advertisementStatus.equals(AdvertisementStatusEnum.Finished.getValue()) || advertisementStatus.equals(AdvertisementStatusEnum.TakeOff.getValue())){
            return MonitorStatusEnum.Finished;
        }
        return null;
    }

    @ApiOperation(value = "广告监控状态数量统计", notes = "创建人: 毛向军")
    @PostMapping(value = "statusStatistics")
    public InvokeResult<List<MonitorStatusStatistic>> queryAdvertisementMonitorStatusStatistics(@Validated @RequestBody AdvertisementMonitorStatisticRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        List<MonitorStatusStatistic> list = advertisementMonitorStatisticService.queryAdvertisementStatusStatistics(request);
        Map<Integer, Long> map = Linq4j.asEnumerable(list).groupBy(a -> getMonitorStatusEnum(a.getStatus()).getValue()).toMap(b -> b.getKey(), c -> c.sum(MonitorStatusStatistic::getCount));
        List<MonitorStatusStatistic> resultList = new ArrayList<>();
        EnumUtils.getAllItems(MonitorStatusEnum.class).forEach(a->{
            if(MonitorStatusEnum.All.getValue().equals(a.getValue())){
                resultList.add(new MonitorStatusStatistic(a.getValue(),Linq4j.asEnumerable(list).select(q->q.getCount()).sum(Long::longValue)));
            }else{
                resultList.add(new MonitorStatusStatistic(a.getValue(),map.get(a.getValue())));
            }
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "上刊报告广告列表", notes = "创建人: 王伟权")
    @PostMapping(value = "report")
    public InvokeResult<PageList<AdvertisementMonitorReport>> advertisementMonitorReport(@Validated @RequestBody ContractStoreInfoQueryRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementMonitorReport(request));
    }

    @ApiOperation(value = "导出上刊报告", notes = "创建人: 文丰")
    @PostMapping(value = "/report/export")
    public InvokeResult export(@Validated @RequestBody ContractStoreQueryRequest request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        addDeliveryWorkBook(wb,request);
        addStoreWorkBook(wb,request);
        outputStream(request.getContractId()+".xls", response, wb);
        return InvokeResult.SuccessResult();
    }

    private PageList<AdvertisementMonitorReport> getAdvertisementMonitorReport(ContractStoreInfoQueryRequest request) {
        AdvertisementMonitorStatistic advertisementMonitorStatistic = advertisementMonitorStatisticService.getAdvertisementMonitorStatistic(request);
        List<AdvertisementMaterialPositionViewModel> advertisementMaterialPosition = advertisementService.getAdvertisementMaterialPosition(request.getContractId());

        AdvertisementMonitorReport report = ApiBeanUtils.copyProperties(advertisementMonitorStatistic,AdvertisementMonitorReport.class);
        Advertisement advertisement = advertisementMonitorStatistic.getAdvertisement();
        BeanUtils.copyProperties(advertisement,report);
        report.setAdvertisementId(advertisement.getId());
        report.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
        report.setAdvertisementTypeName(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
        report.setStartTime(advertisement.getEffectiveStartTime());
        if(advertisementMonitorStatistic.getEndTime()==null){
            report.setDeliveryPeroid(String.valueOf(advertisement.getAdvertisementPeriod()));
            report.setEndTime(DateUtils.addDays(advertisementMonitorStatistic.getCreatedTime(),advertisement.getAdvertisementPeriod()));
        }else{
            report.setDeliveryPeroid(String.valueOf(DateUtils.getIntervalDays(advertisement.getStartTime(),advertisement.getEndTime())));
        }
        if(!advertisement.getAdvertisementStatus().equals(AdvertisementStatusEnum.Delivering.getValue())) {
            report.setEndTime(advertisement.getEffectiveEndTime());
            report.setAdvertisementStatusName(AdvertisementStatusEnum.Finished.getDisplayName());
            report.setDeliveryPeroid(String.valueOf(advertisement.getEffectivePeriod()));
        }
        report.setRemark("");
        StringBuffer material=new StringBuffer();
        advertisementMaterialPosition.stream().forEach(advertisementMaterialPositionViewModel -> {
            if (advertisementMaterialPositionViewModel.getAdvertiementId().equals(report.getAdvertisementId())) {
                report.getAdvertisementMaterials().add(new AdvertisementMaterialViewModel(advertisementMaterialPositionViewModel.getUrl(), advertisementMaterialPositionViewModel.getPositionType()));
                if(!advertisement.getAdvertisementType().equals(MaterialTypeEnum.Text.getValue())){
                    material.append(EnumUtils.getDisplayName(Integer.valueOf(advertisementMaterialPositionViewModel.getPositionType()),AdvertisementPositionTypeEnum.class)).append("、");
                }
            }
        });
        report.setAdvertisementMaterial(material.toString());

        PageList<AdvertisementMonitorReport> reportPageList = new PageList<>();
        reportPageList.setList(Arrays.asList(report));
        return reportPageList;
    }

    private void addDeliveryWorkBook(Workbook workbook,ContractStoreInfoQueryRequest request){
        PageList<AdvertisementMonitorReport> pageList = getAdvertisementMonitorReport(request);
        Contract contract =contractService.getContract(request.getContractId());
        ExcelData excelData=new ExcelData("投放形式").addData(pageList.getList());
        excelData.addHeader(new ExcelHeader(
                new Integer[]{0, 0, 0,13},
                new String[]{"投放形式"},
                null,
                CellStyle.ALIGN_CENTER
        )).addHeader(new ExcelHeader(
                new Integer[]{1, 1, 1, 13},
                new String[]{"客户名称",getCustomerName(contract.getCustomerId())},
                null,
                CellStyle.ALIGN_LEFT
        )).addHeader(new ExcelHeader(
                new Integer[]{2, 2, 1, 12},
                new String[]{"合同名称",contract.getContractName()},
                null,
                CellStyle.ALIGN_LEFT
        )).addHeader(new ExcelHeader(
                null,
                new String[]{"序号","广告形式","展示时长（秒）","频次（次/天）","投放起始日期","投放结束日期","天数","预计展示总频次","实际展示总频次","投放城市","投放门店数量","素材","备注","状态"},
                new String[]{"advertisementTypeName","duration","displayFrequency","startTime","endTime","deliveryPeroid","totalDisplayTimes","displayTimes","deliveryCities","totalStoreCount","advertisementMaterial","remark","advertisementStatusName"},
                CellStyle.ALIGN_CENTER
        )).addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
            put("startTime",TimeFormatEnum.Default);
            put("endTime",TimeFormatEnum.Default);
        }});
        ExcelSheetUtil.addSheet(workbook,excelData);
    }

    private void addStoreWorkBook(Workbook workbook, ContractStoreQueryRequest request){
        PageList<StoreInfoViewModel> resultList = storeInfoApiController.getStoreInfoViewModelPageList(request);
        ExcelData excelData=new ExcelData("具体点位").addData(resultList.getList());
        excelData.addHeader(new ExcelHeader(
                new Integer[]{0, 0, 0, 4},
                new String[]{"广告点位"},
                null,
                CellStyle.ALIGN_CENTER
        )).addHeader(new ExcelHeader(
                null,
                new String[]{"城市","城区","设备ID","店铺名","地址"},
                new String[]{"cityName","regionName","deviceId","storeName","storeAddress"},
                CellStyle.ALIGN_CENTER
        ));
        ExcelSheetUtil.addSheet(workbook,excelData);
    }

    @ApiOperation(value = "投放终端占比", notes = "创建人: 李海峰")
    @GetMapping(value = "/{advertisementId}/pieChart/terminal")
    public InvokeResult<List<TerminalProportion>> pieChartTerminal(@ApiParam("广告ID") @PathVariable String advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(advertisementId);
        Map<Integer, Integer> data = advertisementMonitorStatisticService.getTerminalDisplayTimes(advertisementId);
        List<TerminalTypeEnum> configuredTerminalTypeEnums = getConfiguredTerminalTypeEnum(getConfiguredAdvertisementPositionCategoryEnum(advertisement.getContractId()));
        List<TerminalProportion> list = getAllTerminalProportion(data, configuredTerminalTypeEnums);
        return InvokeResult.SuccessResult(list);
    }

    /**
     * 获得所有终端占比
     * @param data
     * @return
     */
    private List<TerminalProportion> getAllTerminalProportion(Map<Integer, Integer> data, List<TerminalTypeEnum> configuredTerminalTypeEnums) {
        List<TerminalProportion> list = new ArrayList<>();
        int num = 0;
        for (TerminalTypeEnum terminalTypeEnum : configuredTerminalTypeEnums) {
            TerminalProportion terminalProportion = new TerminalProportion();
            terminalProportion.setTerminalType(terminalTypeEnum.getValue());
            terminalProportion.setTerminalName(terminalTypeEnum.getDisplayName());
            Integer displayTimes = data.get(terminalTypeEnum.getValue());
            displayTimes = displayTimes == null ? 0 : displayTimes;
            num += displayTimes;// 计算总展示次数
            terminalProportion.setDisplayTimes(displayTimes);
            list.add(terminalProportion);
        }
        final long count = num;
        list.forEach(terminalProportion -> terminalProportion.setProportion(NumberFormatUtil.format(terminalProportion.getDisplayTimes().longValue(), count, Constant.RATIO_PATTERN)));
        return list;
    }

    @ApiOperation(value = "投放位置占比", notes = "创建人: 李海峰")
    @GetMapping(value = "/{advertisementId}/pieChart/position")
    public InvokeResult<List<AdPositionProportion>> pieChartPosition(@ApiParam("广告ID") @PathVariable String advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(advertisementId);// 校验广告是否存在
        Map<Integer, Integer> data = advertisementMonitorStatisticService.getAllAdvertisementPositionCategoryDisplayTimes(advertisementId);
        List<AdvertisementPositionCategoryEnum> advertisementPositionCategoryEnums = getConfiguredAdvertisementPositionCategoryEnum(advertisement.getContractId());
        List<AdPositionProportion> list = getAllAdPositionProportion(data, advertisementPositionCategoryEnums);
        return InvokeResult.SuccessResult(list);
    }

    /**
     * 获得所有广告位的占比情况
     * @param data
     * @return
     */
    private List<AdPositionProportion> getAllAdPositionProportion(Map<Integer, Integer> data, List<AdvertisementPositionCategoryEnum> advertisementPositionCategoryEnums) {
        List<AdPositionProportion> list = new ArrayList<>();
        int num = 0;
        for (AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum : advertisementPositionCategoryEnums) {
            AdPositionProportion adPositionProportion = new AdPositionProportion();
            adPositionProportion.setAdvertisementPositionCategory(advertisementPositionCategoryEnum.getValue());
            adPositionProportion.setAdPositionName(advertisementPositionCategoryEnum.getDisplayName());
            Integer displayTimes = data.get(advertisementPositionCategoryEnum.getValue());
            displayTimes = displayTimes == null ? 0 : displayTimes;
            num += displayTimes;// 计算总显示次数
            adPositionProportion.setDisplayTimes(displayTimes);
            list.add(adPositionProportion);
        }
        final long count = num;
        list.forEach(adPositionProportion -> adPositionProportion.setProportion(NumberFormatUtil.format(adPositionProportion.getDisplayTimes().longValue(),count,Constant.RATIO_PATTERN)));
        return list;
    }

    /**
     * 通过合同ID获得已配置的广告位类型枚举
     */
    private List<AdvertisementPositionCategoryEnum> getConfiguredAdvertisementPositionCategoryEnum(String contractId) {
        return contractAdvertisementPositionConfigService.getContractAdvertisementPositionConfigs(contractId).stream().filter(e -> Objects.nonNull(e.getAdvertisementPositionCategoryEnum())).map(ContractAdvertisementPositionConfig::getAdvertisementPositionCategoryEnum).distinct().collect(Collectors.toList());
    }

    /**
     * 通过已配置的广告位类型枚举获得已配置的终端类型枚举
     */
    private List<TerminalTypeEnum> getConfiguredTerminalTypeEnum(List<AdvertisementPositionCategoryEnum> advertisementPositionCategoryEnums) {
        return advertisementPositionCategoryEnums.stream().map(AdvertisementPositionCategoryEnum::getTerminalType).distinct().collect(Collectors.toList());
    }

    @ApiOperation(value = "展示次数曲线图", notes = "创建人: 李海峰")
    @PostMapping("/lineChart")
    public InvokeResult<DisplayTimesBrokenChartViewModel> lineChart(@Validated@RequestBody DisplayTimesBrokenChartRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);

        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(request.getAdvertisementId());
        List<AdvertisementPositionCategoryEnum> allAdvertisementPositionCategoryEnum = getConfiguredAdvertisementPositionCategoryEnum(advertisement.getContractId());
        List<TerminalTypeEnum> allTerminalTypeEnums = getConfiguredTerminalTypeEnum(allAdvertisementPositionCategoryEnum);

        List<Date> dateList = DateUtils.getDateList(request.getStartDate(), request.getEndDate(), Calendar.DATE);
        List<AdvertisementPositionDailyDisplayTimesStatistic> displayTimesStatistics = advertisementMonitorStatisticService.getDaliyDisplayTimesStatistics(request);

        DisplayTimesBrokenChartViewModel displayTimesBrokenChartViewModel = new DisplayTimesBrokenChartViewModel();
        displayTimesBrokenChartViewModel.setStartDate(request.getStartDate());
        displayTimesBrokenChartViewModel.setEndDate(request.getEndDate());
        displayTimesBrokenChartViewModel.setDisplayTimes(displayTimesStatistics.stream().mapToLong(AdvertisementPositionDailyDisplayTimesStatistic::getTotalDisplayTimes).sum());
        displayTimesBrokenChartViewModel.setItems(dateList.stream().map(date -> {
            Optional<AdvertisementPositionDailyDisplayTimesStatistic> optional = displayTimesStatistics.stream().filter(e -> Objects.equals(e.getDate(), date)).findAny();
            Long totalDisplayTimes = optional.isPresent() ? optional.get().getTotalDisplayTimes() : 0;
            Long storeNum = optional.isPresent()? optional.get().getStoreNum() : 0;

            DisplayTimesBrokenChartItem displayTimesBrokenChartItem = new DisplayTimesBrokenChartItem();
            displayTimesBrokenChartItem.setDate(date);
            displayTimesBrokenChartItem.setStoreNum(storeNum);
            displayTimesBrokenChartItem.setDisplayTimes(totalDisplayTimes);

            if (allTerminalTypeEnums.contains(TerminalTypeEnum.CashRegister)) {
                displayTimesBrokenChartItem.setCashRegisterDisplayTimes(optional.isPresent() ? optional.get().getCashRegisterDisplayTimes() : 0);
                displayTimesBrokenChartItem.setCashRegisterProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getCashRegisterDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allTerminalTypeEnums.contains(TerminalTypeEnum.IOS)) {
                displayTimesBrokenChartItem.setIosDisplayTimes(optional.isPresent() ? optional.get().getIOSDisplayTimes() : 0);
                displayTimesBrokenChartItem.setIosProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getIosDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allTerminalTypeEnums.contains(TerminalTypeEnum.Android)) {
                displayTimesBrokenChartItem.setAndroidDisplayTimes(optional.isPresent() ? optional.get().getAndroidDisplayTimes() : 0);
                displayTimesBrokenChartItem.setAndroidProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getAndroidDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.FullScreen)) {
                displayTimesBrokenChartItem.setFullScreenDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory1DisplayTimes() : 0);
                displayTimesBrokenChartItem.setFullScreenProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getFullScreenDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.ScanPay)) {
                displayTimesBrokenChartItem.setScanPayDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory2DisplayTimes() : 0);
                displayTimesBrokenChartItem.setScanPayProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getScanPayDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.SellerFullScreen)) {
                displayTimesBrokenChartItem.setSellerFullScreenDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory3DisplayTimes() : 0);
                displayTimesBrokenChartItem.setSellerFullScreenProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getSellerFullScreenDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.BusinessBanner)) {
                displayTimesBrokenChartItem.setBusinessBannerDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory4DisplayTimes() : 0);
                displayTimesBrokenChartItem.setBusinessBannerProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getBusinessBannerDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.AndroidAppStartingUp)) {
                displayTimesBrokenChartItem.setAndroidAppStartingUpDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory5DisplayTimes() : 0);
                displayTimesBrokenChartItem.setAndroidAppStartingUpProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getAndroidAppStartingUpDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.AndroidAppBanner)) {
                displayTimesBrokenChartItem.setAndroidAppBannerDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory6DisplayTimes() : 0);
                displayTimesBrokenChartItem.setAndroidAppBannerProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getAndroidAppBannerDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.IOSAppStartingUp)) {
                displayTimesBrokenChartItem.setIosAppStartingUpDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory7DisplayTimes() : 0);
                displayTimesBrokenChartItem.setIosAppStartingUpProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getIosAppStartingUpDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            if (allAdvertisementPositionCategoryEnum.contains(AdvertisementPositionCategoryEnum.IOSAppBanner)) {
                displayTimesBrokenChartItem.setIosAppBannerDisplayTimes(optional.isPresent() ? optional.get().getAdvertisementPositionCategory8DisplayTimes() : 0);
                displayTimesBrokenChartItem.setIosAppBannerProportion(NumberFormatUtil.format(displayTimesBrokenChartItem.getIosAppBannerDisplayTimes(), totalDisplayTimes, Constant.RATIO_PATTERN));
            }
            return displayTimesBrokenChartItem;
        }).collect(Collectors.toList()));

        return InvokeResult.SuccessResult(displayTimesBrokenChartViewModel);
    }

    @ApiOperation(value = "获广告监控门店列表", notes = "创建人: 王伟权")
    @PostMapping(value = "getMonitorStoreItems")
    public InvokeResult<PageList<AdvertisementStoreMonitorItem>> getMonitorStoreItems(@Validated @RequestBody AdvertisementStoreMonitorItemRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        PageList<AdvertisementStoreMonitorItem> resultList = getMonitorStoreItemPageList(request);
        return InvokeResult.SuccessResult(resultList);
    }

    private PageList<AdvertisementStoreMonitorItem> getMonitorStoreItemPageList(AdvertisementStoreMonitorItemRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Page<AdvertisementStoreMonitorItem> page = advertisementMonitorStatisticService.getMonitorStoreItems(request, pageable);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(page, advertisementStoreMonitorItem -> {
            advertisementStoreMonitorItem.setProvinceName(getAreaName(advertisementStoreMonitorItem.getProvinceId(),areaMap));
            advertisementStoreMonitorItem.setCityName(getAreaName(advertisementStoreMonitorItem.getCityId(),areaMap));
            advertisementStoreMonitorItem.setRegionName(getAreaName(advertisementStoreMonitorItem.getRegionId(),areaMap));
            return advertisementStoreMonitorItem;
        });
    }

    @ApiOperation(value = "导出广告门店监控信息", notes = "创建人: 王伟权")
    @PostMapping(value = "exportMonitorStoreInfo")
    public InvokeResult export(@Validated @RequestBody AdvertisementStoreMonitorItemRequest request,HttpServletResponse response,BindingResult result) throws IOException {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","城区","具体地址","设备ID", "是否达标","是否可用","是否激活","已展示次数","更新时间"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId", "isQualified","available","isActive","displayTimes","updatedTime"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Integer index=0;
        Integer totalPage;
        do{
            request.setPageIndex(index);
            PageList<AdvertisementStoreMonitorItem> pageList = getMonitorStoreItemPageList(request);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+(index+1)+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("updatedTime", TimeFormatEnum.Default);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="store_"+request.getAdvertisementId()+".xls";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value = "获取广告监控详情门店列表地区筛选项, 组装成树", notes = "修改人: 王伟权")
    @GetMapping("/{advertisementId}/storeAreaInfo")
    public InvokeResult<List<AreaViewModel>> storeAreaInfo(@PathVariable String advertisementId) {
        if(StringUtils.isEmpty(advertisementId)) {
            return InvokeResult.Fail("广告ID不能为空");
        }
        List<Area> areas = advertisementMonitorStatisticService.getStoreAreaInfo(advertisementId);
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> {
            AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "广告监控门店激活曲线图", notes = "创建人: 王伟权")
    @PostMapping("/getActiveStoreGraphData")
    public InvokeResult<List<AdvertisementStoreGraphDailyDataViewModel>> getActiveStoreGraphData(@Validated @RequestBody StoreGraphTimeSpanViewModel viewModel, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }

        Long totalStoreCount = advertisementService.getTotalStoreCount(viewModel.getAdvertisementId());
        List<Date> dateList = DateUtils.getDateList(viewModel.getStartDate(), viewModel.getEndDate(), Calendar.DAY_OF_MONTH);
        List<AdvertisingDailyStoreMonitorStatistic> activeStoreGraphData = advertisementMonitorStatisticService.getActiveStoreGraphData(viewModel);
        judgeIncludeToday(viewModel, activeStoreGraphData);
        Map<Long, AdvertisingDailyStoreMonitorStatistic> activeStoreGraphDataMap = Linq4j.asEnumerable(activeStoreGraphData).toMap(a -> a.getDate().getTime(), b -> b);
        List<AdvertisementStoreGraphDailyDataViewModel> resultList = new ArrayList<>();

        dateList.forEach(date -> {
            AdvertisingDailyStoreMonitorStatistic statistic = activeStoreGraphDataMap.get(date.getTime());
            AdvertisementStoreGraphDailyDataViewModel dataViewModel = new AdvertisementStoreGraphDailyDataViewModel(date);
            if(statistic != null) {
                dataViewModel.setDate(statistic.getDate());
                dataViewModel.setActiveStoreCount(statistic.getActiveStoreCount());
                dataViewModel.setUnActiveStoreCount(totalStoreCount.intValue() - statistic.getActiveStoreCount());
                dataViewModel.setRatio(NumberFormatUtil.format(statistic.getActiveStoreCount().longValue(), totalStoreCount, Constant.RATIO_PATTERN));
            } else {
                dataViewModel.setUnActiveStoreCount(totalStoreCount.intValue() - dataViewModel.getActiveStoreCount());
            }
            resultList.add(dataViewModel);
        });
        return InvokeResult.SuccessResult(resultList);
    }

    private void judgeIncludeToday(StoreGraphTimeSpanViewModel viewModel, List<AdvertisingDailyStoreMonitorStatistic> activeStoreGraphData) {
        LocalDate startLocalDate = new LocalDate(viewModel.getStartDate());
        LocalDate endLocalDate = new LocalDate(viewModel.getEndDate());
        LocalDate now = new LocalDate();
        if (now.isEqual(startLocalDate) || now.isEqual(endLocalDate) || (now.isAfter(startLocalDate) && now.isBefore(endLocalDate))) {
            AdvertisingDailyStoreMonitorStatistic statistic = new AdvertisingDailyStoreMonitorStatistic();
            statistic.setDate(now.toDate());
            statistic.setActiveStoreCount(advertisementMonitorStatisticService.getActiveStoreCountAtTime(viewModel.getAdvertisementId(), now.toDate()).intValue());
            statistic.setAdvertisementId(viewModel.getAdvertisementId());
            activeStoreGraphData.add(statistic);
        }
    }

    @ApiOperation(value = "广告监控详情门店统计", notes = "创建人: 王伟权")
    @GetMapping(value = "/{advertisementId}/storeCountStatistic")
    public InvokeResult<AdvertisementStoreCountViewModel> storeCountStatistic(@PathVariable String advertisementId) {
        if (StringUtils.isEmpty(advertisementId)) {
            return InvokeResult.Fail("广告ID不能为空");
        }
        AdvertisementStoreCountViewModel viewModel = new AdvertisementStoreCountViewModel();
        viewModel.setTotalStoreCount(advertisementService.getTotalStoreCount(advertisementId).intValue());
        viewModel.setAvailableCount(advertisementMonitorStatisticService.getAvailableStoreCount(advertisementId));
        viewModel.setActivatedCount(advertisementMonitorStatisticService.getActiveStoreCount(advertisementId).intValue());
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "投放门店参与统计", notes = "创建人: 李海峰")
    @GetMapping("/{advertisementId}/storeParticipationStatistics")
    public InvokeResult<List<StoreParticipationStatisticsViewModel>> storeParticipationStatistics(@PathVariable @ApiParam("广告ID") String advertisementId) {
        if (StringUtils.isEmpty(advertisementId)) return InvokeResult.Fail("广告ID不能为空");
        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(advertisementId);
        if (advertisement == null) return InvokeResult.Fail("广告不存在");
        if (!isConfiguredUrl(advertisementId)) return InvokeResult.Fail("该广告未配置链接");

        Long totalStores = contractService.getTotalStores(advertisement.getContractId());
        List<StoreParticipationStatisticsViewModel> storeParticipationStatisticsViewModels = new ArrayList<>();
        List<StoreParticipationStatistics> allStoreParticipationStatistics = advertisementMonitorStatisticService.getAllStoreParticipationStatistics(advertisementId);
        Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> advertisementPositionCategoryEnums = advertisementService.getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(advertisementId);
        Set<Map.Entry<AdvertisementPositionCategoryEnum, AdvertisementMaterial>> entrySet = advertisementPositionCategoryEnums.entrySet();
        entrySet.forEach(e -> {
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = e.getKey();
            AdvertisementMaterial advertisementMaterial = e.getValue();

            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialClickUrl())) {
                StoreParticipationStatisticsViewModel urlClickViewModel = new StoreParticipationStatisticsViewModel();
                urlClickViewModel.setAdvertisementPositionName(advertisementPositionCategoryEnum.getDisplayName());
                urlClickViewModel.setLinkType(MaterialLinkTypeEnum.MaterialClick.getDisplayName());
                urlClickViewModel.setParticipatingStoreNum(getParticipatingStoreNum(allStoreParticipationStatistics, advertisementPositionCategoryEnum.getValue(), MaterialLinkTypeEnum.MaterialClick));
                urlClickViewModel.setNoParticipatingStoreNum(totalStores - urlClickViewModel.getParticipatingStoreNum());
                storeParticipationStatisticsViewModels.add(urlClickViewModel);
            }

            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialQRCodeUrl())) {
                StoreParticipationStatisticsViewModel qrCodeViewModel = new StoreParticipationStatisticsViewModel();
                qrCodeViewModel.setAdvertisementPositionName(advertisementPositionCategoryEnum.getDisplayName());
                qrCodeViewModel.setLinkType(MaterialLinkTypeEnum.MaterialQRCode.getDisplayName());
                qrCodeViewModel.setParticipatingStoreNum(getParticipatingStoreNum(allStoreParticipationStatistics, advertisementPositionCategoryEnum.getValue(), MaterialLinkTypeEnum.MaterialQRCode));
                qrCodeViewModel.setNoParticipatingStoreNum(totalStores - qrCodeViewModel.getParticipatingStoreNum());
                storeParticipationStatisticsViewModels.add(qrCodeViewModel);
            }
        });
        return InvokeResult.SuccessResult(storeParticipationStatisticsViewModels);
    }

    private Long getParticipatingStoreNum(List<StoreParticipationStatistics> allStoreParticipationStatistics, Integer advertisementPositionCategory, MaterialLinkTypeEnum materialLinkTypeEnum) {
        Optional<StoreParticipationStatistics> optional = allStoreParticipationStatistics.stream().filter(e -> Objects.equals(e.getAdvertisementPositionCategory(), advertisementPositionCategory) && Objects.equals(e.getLinkType(), materialLinkTypeEnum.getValue())).findAny();
        return optional.isPresent() ? optional.get().getCount() : 0;
    }

    @ApiOperation(value = "点击/扫码次数占比", notes = "创建人: 李海峰")
    @GetMapping(value = "/{advertisementId}/clickOrScanTimesRatio")
    public InvokeResult<List<ClickOrScanTimesViewModel>> clickOrScanTimesRatio(@PathVariable @ApiParam("广告ID") String advertisementId) {
        if (StringUtils.isEmpty(advertisementId)) return InvokeResult.Fail("广告ID不能为空");
        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(advertisementId);
        if (Objects.isNull(advertisement)) return InvokeResult.Fail("广告不存在");
        if (!isConfiguredUrl(advertisementId)) return InvokeResult.Fail("该广告未配置链接");

        List<ClickOrScanTimes> allClickOrScanTimes = advertisementMonitorStatisticService.getAllClickOrScanTimes(advertisementId);
        long totalLinkTimes = allClickOrScanTimes.stream().mapToLong(ClickOrScanTimes::getTotalTimes).sum();
        Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> advertisementPositionCategoryEnums = advertisementService.getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(advertisementId);
        Set<Map.Entry<AdvertisementPositionCategoryEnum, AdvertisementMaterial>> entrySet = advertisementPositionCategoryEnums.entrySet();
        List<ClickOrScanTimesViewModel> list = entrySet.stream().map(e -> {
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = e.getKey();
            AdvertisementMaterial advertisementMaterial = e.getValue();

            ClickOrScanTimesViewModel viewModel = new ClickOrScanTimesViewModel();
            viewModel.setAdvertisementPositionCategory(advertisementPositionCategoryEnum.getValue());
            viewModel.setAdvertisementPositionName(advertisementPositionCategoryEnum.getDisplayName());
            long currentAdLinkTimes = allClickOrScanTimes.stream().filter(q -> Objects.equals(q.getAdvertisementPositionCategory(), advertisementPositionCategoryEnum.getValue())).mapToLong(ClickOrScanTimes::getTotalTimes).sum();
            viewModel.setAdvertisementPositionProportion(NumberFormatUtil.format(currentAdLinkTimes, totalLinkTimes, Constant.RATIO_PATTERN));
            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialClickUrl())) {
                viewModel.setClickTimes(getLinkTimes(allClickOrScanTimes, advertisementPositionCategoryEnum.getValue(), MaterialLinkTypeEnum.MaterialClick));
                viewModel.setClickTimesProportion(NumberFormatUtil.format(viewModel.getClickTimes(), currentAdLinkTimes, Constant.RATIO_PATTERN));
            }
            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialQRCodeUrl())) {
                viewModel.setScanTimes(getLinkTimes(allClickOrScanTimes, advertisementPositionCategoryEnum.getValue(), MaterialLinkTypeEnum.MaterialQRCode));
                viewModel.setScanTimesProportion(NumberFormatUtil.format(viewModel.getScanTimes(), currentAdLinkTimes, Constant.RATIO_PATTERN));
            }
            return viewModel;
        }).collect(Collectors.toList());
        return InvokeResult.SuccessResult(list);
    }

    private Long getLinkTimes(List<ClickOrScanTimes> list, Integer advertisementPositionCategory, MaterialLinkTypeEnum materialLinkTypeEnum) {
        Optional<ClickOrScanTimes> optional = list.stream().filter(e -> Objects.equals(e.getAdvertisementPositionCategory(), advertisementPositionCategory) && Objects.equals(e.getLinkType(), materialLinkTypeEnum.getValue())).findAny();
        return optional.isPresent() ? optional.get().getTotalTimes() : 0;
    }

    @ApiOperation(value = "点击/扫码次数趋势（点击/扫码次数 / 时间）", notes = "创建人: 李海峰")
    @PostMapping("/clickOrScanTimesTrend")
    public InvokeResult<ClickOrScanTimesTrendViewModel> clickOrScanTimesTrend(@Validated @RequestBody ClickOrScanTimesTrendRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);
        if (Objects.isNull(advertisementService.getAdvertisementAuthorized(request.getAdvertisementId()))) return InvokeResult.Fail("广告不存在");

        Collection<AdvertisementMaterial> advertisementMaterials = advertisementService.getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(request.getAdvertisementId()).values();
        boolean isConfiguredClickUrl = isConfiguredUrl(advertisementMaterials, MaterialLinkTypeEnum.MaterialClick);// 是否配置点击url
        boolean isConfiguredQRCodeUrl = isConfiguredUrl(advertisementMaterials, MaterialLinkTypeEnum.MaterialQRCode);// 是否配置二维码url
        if (!isConfiguredClickUrl && !isConfiguredQRCodeUrl) return InvokeResult.Fail("该广告未配置链接");

        List<DateLinkTimes> allLinkTimes = advertisementMonitorStatisticService.getAllLinkTimes(request);// 根据条件查询出所有数据
        List<Date> dateList = DateUtils.getDateList(request.getBeginDate(), request.getEndDate(), Calendar.DATE);

        ClickOrScanTimesTrendViewModel viewModel = new ClickOrScanTimesTrendViewModel();
        viewModel.setBeginDate(request.getBeginDate());
        viewModel.setEndDate(request.getEndDate());
        if (isConfiguredClickUrl) {
            viewModel.setClickTimes(getLinkTimes(allLinkTimes, MaterialLinkTypeEnum.MaterialClick));
        }
        if (isConfiguredQRCodeUrl) {
            viewModel.setScanTimes(getLinkTimes(allLinkTimes, MaterialLinkTypeEnum.MaterialQRCode));
        }
        viewModel.setItem(dateList.stream().map(date -> {
            ClickOrScanTimesTrendItem item = new ClickOrScanTimesTrendItem();
            item.setDate(date);
            if (isConfiguredClickUrl) {
                item.setClickTimes(getLinkTimes(allLinkTimes, date, MaterialLinkTypeEnum.MaterialClick));
            }
            if (isConfiguredQRCodeUrl) {
                item.setScanTimes(getLinkTimes(allLinkTimes, date, MaterialLinkTypeEnum.MaterialQRCode));
            }
            return item;
        }).collect(Collectors.toList()));

        return InvokeResult.SuccessResult(viewModel);
    }

    private Long getLinkTimes(List<DateLinkTimes> list, MaterialLinkTypeEnum materialLinkTypeEnum) {
        return list.stream().filter(e -> Objects.equals(e.getLinkType(), materialLinkTypeEnum.getValue())).mapToLong(DateLinkTimes::getTotalTimes).sum();
    }

    private Long getLinkTimes(List<DateLinkTimes> list, Date date, MaterialLinkTypeEnum materialLinkTypeEnum) {
        Optional<DateLinkTimes> optional = list.stream().filter(e -> Objects.equals(e.getDate(), date) && Objects.equals(e.getLinkType(), materialLinkTypeEnum.getValue())).findAny();
        return optional.isPresent() ?  optional.get().getTotalTimes() : 0;
    }

    /**
     * 根据传入的枚举值判断广告是否配置了对应的链接
     */
    private boolean isConfiguredUrl(Collection<AdvertisementMaterial> advertisementMaterials, MaterialLinkTypeEnum materialLinkTypeEnum) {
        return advertisementMaterials.stream().filter(e -> {
            if (Objects.equals(materialLinkTypeEnum, MaterialLinkTypeEnum.MaterialClick)) return !StringUtils.isEmpty(e.getMaterialClickUrl());
            else if (Objects.equals(materialLinkTypeEnum, MaterialLinkTypeEnum.MaterialQRCode)) return !StringUtils.isEmpty(e.getMaterialQRCodeUrl());
            else return false;
        }).findFirst().isPresent();
    }

    /**
     * 广告是否配置了链接
     */
    private boolean isConfiguredUrl(String advertisementId) {
        Collection<AdvertisementMaterial> advertisementMaterials = advertisementService.getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(advertisementId).values();
        return isConfiguredUrl(advertisementMaterials, MaterialLinkTypeEnum.MaterialClick) || isConfiguredUrl(advertisementMaterials, MaterialLinkTypeEnum.MaterialQRCode);
    }

    @ApiOperation(value ="导出URL监控的手机号",notes = "创建人：文丰")
    @PostMapping(value = "/{urlStepIds}/exportAdvertisementURlMonitorPhone")
    public InvokeResult  exportAdvertisementURlMonitorPhone(@PathVariable String urlStepIds,HttpServletResponse response){
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        new String[]{"手机号","门店ID","门店名称"},
                        new String[]{"phone","shopId","storeName"},
                        CellStyle.ALIGN_CENTER
                )
        ));
        List<String> stepIds=Arrays.asList(urlStepIds.split(Constant.SEPARATOR));
        Long count=advertisementMonitorStatisticService.getAdvertisementLinkPhoneCountByUrlStepIds(stepIds);
        List<StorePhoneInfo> list =null;
        Map<String,StoreNameInfo> storeNameMap=new HashMap<>();
        Long index=0L;
        do{
            list=advertisementMonitorStatisticService.getAdvertisementDailyLinkMonitorInfo(stepIds,index*Constant.MONGODB_PAGESIZE,Constant.MONGODB_PAGESIZE);
            storeNameMap.putAll(storeInfoMapper.getStoreNameInfo(list.stream().map(a->a.getStoreId()).collect(Collectors.toList())).stream().collect(Collectors.toMap(a->a.getId(),a->a)));
            list.stream().forEach(a->{
                if(storeNameMap.containsKey(a.getStoreId())){
                    StoreNameInfo storeNameInfo=storeNameMap.get(a.getStoreId());
                    a.setShopId(storeNameInfo.getShopId());
                    a.setStoreName(storeNameInfo.getStoreName());
                }
            });
            sheetConfig.setSheetNameTemplate("第%d页");
            myWorkbook.addData(list,sheetConfig);
            storeNameMap.clear();
        }while(count>(++index)*Constant.MONGODB_PAGESIZE);
        String filename="Url监控-手机号列表"+".xlsx";
        outputStream(filename, response, myWorkbook);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "点击/扫码统计列表", notes = "创建人: 毛向军")
    @PostMapping("/materialLinkStatistics")
    public InvokeResult<PageList<MaterialLinkMonitorViewModel>> materialLinkMonitorStatistics(@Validated @RequestBody MaterialLinkMonitorRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return  InvokeResult.SuccessResult(getMaterialLinkMonitorViewModelPageList(request));
    }

    private PageList<MaterialLinkMonitorViewModel> getMaterialLinkMonitorViewModelPageList(@Validated @RequestBody MaterialLinkMonitorRequest request) {
        Page<AdvertisementMaterial> page = advertisementService.getAdvertisementMaterialContainLinkUrl(request);
        PageList<MaterialLinkMonitorViewModel> pageList = ApiBeanUtils.convertToPageList(page);
        List<MaterialLinkMonitorViewModel> materialLinkMonitorViewModels = new ArrayList<>();
        Map<AdvertisementPositionCategoryEnum, AdvertisementMaterial> materialLinkAdvertisementPositionCategoryEnums = advertisementService.getConfiguredMaterialLinkAdvertisementPositionCategoryEnums(page.getContent());
        Set<Map.Entry<AdvertisementPositionCategoryEnum, AdvertisementMaterial>> entrySet = materialLinkAdvertisementPositionCategoryEnums.entrySet();
        entrySet.forEach(e -> {
            AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = e.getKey();
            AdvertisementMaterial advertisementMaterial = e.getValue();

            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialClickUrl())&&(request.getLinkType()==null||request.getLinkTypes().contains(MaterialLinkTypeEnum.MaterialClick.getValue()))){
                MaterialLinkMonitorViewModel urlClickViewModel = getMaterialLinkMonitorViewModel(advertisementPositionCategoryEnum, advertisementMaterial, MaterialLinkTypeEnum.MaterialClick);
                urlClickViewModel.setPageConversionRatio(NumberFormatUtil.format(urlClickViewModel.getPromotionPageSucceedTimes(),urlClickViewModel.getUrlClickTimes(), Constant.RATIO_PATTERN));
                materialLinkMonitorViewModels.add(urlClickViewModel);
            }

            if (!StringUtils.isEmpty(advertisementMaterial.getMaterialQRCodeUrl())&&(request.getLinkType()==null||request.getLinkTypes().contains(MaterialLinkTypeEnum.MaterialQRCode.getValue()))){
                MaterialLinkMonitorViewModel qrCodeViewModel = getMaterialLinkMonitorViewModel(advertisementPositionCategoryEnum, advertisementMaterial,MaterialLinkTypeEnum.MaterialQRCode);
                qrCodeViewModel.setPageConversionRatio(NumberFormatUtil.format(qrCodeViewModel.getPromotionPageSucceedTimes(),qrCodeViewModel.getQRCodeTimes(), Constant.RATIO_PATTERN));
                qrCodeViewModel.setPhonePageConversionRatio(NumberFormatUtil.format(qrCodeViewModel.getPromotionPageSucceedTimes(),qrCodeViewModel.getPhonePageSucceedTimes(), Constant.RATIO_PATTERN));
                materialLinkMonitorViewModels.add(qrCodeViewModel);
            }
        });
        List<String> urlStepIds = materialLinkMonitorViewModels.stream().map(MaterialLinkMonitorViewModel::getMaterialUrlStepId).collect(Collectors.toList());
        Map<String, Integer> urlStepIdsToPhoneCount = advertisementMonitorStatisticService.getPhoneCount(urlStepIds);
        materialLinkMonitorViewModels.forEach(viewModel->viewModel.setPhoneCount(urlStepIdsToPhoneCount.getOrDefault(viewModel.getMaterialUrlStepId(),0)));
        Collections.sort(materialLinkMonitorViewModels);
        pageList.setList(materialLinkMonitorViewModels);
        return pageList;
    }

    private MaterialLinkMonitorViewModel getMaterialLinkMonitorViewModel(AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum, AdvertisementMaterial advertisementMaterial,MaterialLinkTypeEnum materialLinkTypeEnum) {
        MaterialLinkMonitorViewModel viewModel = new MaterialLinkMonitorViewModel();
        viewModel.setAdvertisementPositionType(advertisementPositionCategoryEnum.getValue());
        viewModel.setAdvertisementPositionTypeName(advertisementPositionCategoryEnum.getDisplayName());
        viewModel.setLinkType(materialLinkTypeEnum.getValue());
        viewModel.setLinkTypeName(materialLinkTypeEnum.getDisplayName());
        List<MaterialLinkMonitorInfo> materialLinkMonitorInfos = advertisementMonitorStatisticService.getMaterialLinkMonitorInfo(advertisementMaterial.getId(), viewModel.getLinkType());
        materialLinkMonitorInfos.forEach(materialLinkMonitorInfo -> {
            Long triggerTimes = materialLinkMonitorInfo.getTriggerTimes();
            switch (EnumUtils.toEnum(materialLinkMonitorInfo.getStepType(), AdvertisementMaterialUrlStepTypeEnum.class)){
                case FillPhone:
                    viewModel.setMaterialUrlStepId(materialLinkMonitorInfo.getMaterialUrlStepId());
                    switch (EnumUtils.toEnum(materialLinkMonitorInfo.getAction(), MaterialLinkActionEnum.class)){
                        case load:
                            switch (materialLinkTypeEnum){
                                case MaterialClick:
                                    viewModel.setUrlClickTimes(triggerTimes);
                                    break;
                                case MaterialQRCode:
                                    viewModel.setQRCodeTimes(triggerTimes);
                                    break;
                            }
                            break;
                        case loadComplete:
                            viewModel.setPhonePageSucceedTimes(triggerTimes);
                            break;
                    }
                    break;
                case Promotion:
                    switch (EnumUtils.toEnum(materialLinkMonitorInfo.getAction(), MaterialLinkActionEnum.class)){
                        case load:
                            switch (materialLinkTypeEnum){
                                case MaterialClick:
                                    if(viewModel.getUrlClickTimes().equals(0L)) viewModel.setUrlClickTimes(triggerTimes);
                                    break;
                                case MaterialQRCode:
                                    if(viewModel.getQRCodeTimes().equals(0L)) viewModel.setQRCodeTimes(triggerTimes);
                                    break;
                            }
                            break;
                        case loadComplete:
                            viewModel.setPromotionPageSucceedTimes(triggerTimes);
                            break;
                    }
                    break;
            }
        });
        return viewModel;
    }

    @ApiOperation(value = "导出点击/扫码统计列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportMaterialLinkStatistics",method = RequestMethod.POST)
    public InvokeResult exportMaterialLinkStatistics(@Validated  @RequestBody MaterialLinkMonitorRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","投放位置","链接方式","Url点击次数","二维码扫码次数","成功打开手机号页面次数","成功打开推广页面次数","手机号数量","页面转化率","手机号转化率"},
                new String[]{"advertisementPositionTypeName","linkTypeName","urlClickTimes","qRCodeTimes","phonePageSucceedTimes","promotionPageSucceedTimes","phoneCount","pageConversionRatio","phonePageConversionRatio"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<MaterialLinkMonitorViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            pageList=getMaterialLinkMonitorViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="点击/扫码次数统计列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }
}
