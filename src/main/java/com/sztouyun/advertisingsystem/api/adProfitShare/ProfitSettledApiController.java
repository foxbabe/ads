package com.sztouyun.advertisingsystem.api.adProfitShare;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStatusEnum;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStoreProfit;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.adProfitShare.PeriodStoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.adProfitShare.SettledStoreProfitService;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.utils.*;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "分成结算接口")
@RestController
@RequestMapping("/api/profitsettled")
public class ProfitSettledApiController extends BaseApiController {
    @Autowired
    private SettledStoreProfitService settledStoreProfitService;
    @Autowired
    PeriodStoreProfitStatisticService periodStoreProfitStatisticService;

    @Autowired
    private HistoricalParamConfigService advertisementProfitShareConfigService;

    @ApiOperation(value = "门店结算基本详情接口",notes = "创建人：王英峰")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InvokeResult<StoreProfitSettledViewModel> getProfitSettledInfo(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        SettledStoreProfit settledStoreProfit = settledStoreProfitService.getProfitSettledInfo(id);
        StoreProfitSettledViewModel viewModel = new StoreProfitSettledViewModel();
        BeanUtils.copyProperties(settledStoreProfit,viewModel);

        SettledStatusEnum settledStatus = EnumUtils.toEnum(settledStoreProfit.getSettleStatus(), SettledStatusEnum.class);
        viewModel.setSettleStatusName(settledStatus.getDisplayName());
        viewModel.setSettledAmount(NumberFormatUtil.format(settledStoreProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.DOWN));
        viewModel.setCreator(getUserNickname(settledStoreProfit.getCreatorId()));

        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "清空已选中的结算列表", notes = "创建人：毛向军")
    @PostMapping(value = "/{settledStoreProfitId}/empty")
    public InvokeResult emptyBySettledStoreProfitId(@PathVariable("settledStoreProfitId") String settledStoreProfitId){
        settledStoreProfitService.emptyBySettledStoreProfitId(settledStoreProfitId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询结算详情下的广告柱状图", notes = "创建人：毛向军")
    @GetMapping("/{settledStoreProfitId}/advertisement")
    public InvokeResult<List<SettledDetailAdvertisementStatisticViewModel>> getSettledDetailAdvertisementStatistic(@PathVariable("settledStoreProfitId") String settledStoreProfitId) {
        return InvokeResult.SuccessResult(settledStoreProfitService.getSettledDetailAdvertisementStatistic(settledStoreProfitId));
    }

    @ApiOperation(value = "根据结算ID查广告", notes = "创建人：毛向军")
    @GetMapping("/{settledStoreProfitId}/area")
    public InvokeResult<List<AreaViewModel>> getAreasBySettledStoreProfitId(@PathVariable("settledStoreProfitId") String settledStoreProfitId) {
        List<Area> areas = periodStoreProfitStatisticService.getAreasBySettledStoreProfitId(settledStoreProfitId);
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询结算详情下的门店列表", notes = "创建人：毛向军")
    @PostMapping(value = "/")
    public InvokeResult<PageList<PeriodStoreProfitStatisticViewModel>> getStoreProfitStatistics(@Validated @RequestBody PeriodStoreProfitStatisticRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getSettledStoreProfitStatisticViewModelPageList(request));
    }

    private PageList<PeriodStoreProfitStatisticViewModel> getSettledStoreProfitStatisticViewModelPageList(PeriodStoreProfitStatisticRequest request) {
        Page<PeriodStoreProfitStatistic> pages = periodStoreProfitStatisticService.getSettledStoreProfitStatisticById(request);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        PageList<PeriodStoreProfitStatisticViewModel> pageList = ApiBeanUtils.convertToPageList(pages, periodStoreProfitStatistic -> {
            PeriodStoreProfitStatisticViewModel viewModel = ApiBeanUtils.copyProperties(periodStoreProfitStatistic.getStoreInfo(), PeriodStoreProfitStatisticViewModel.class);
            viewModel.setCityName(getAreaName(periodStoreProfitStatistic.getStoreInfo().getCityId(), areaMap));
            viewModel.setProvinceName(getAreaName(periodStoreProfitStatistic.getStoreInfo().getProvinceId(), areaMap));
            viewModel.setRegionName(getAreaName(periodStoreProfitStatistic.getStoreInfo().getRegionId(), areaMap));
            viewModel.setSettledAmount(NumberFormatUtil.format(periodStoreProfitStatistic.getShareAmount(), Constant.SCALE_TWO));
            viewModel.setShopId(periodStoreProfitStatistic.getStoreInfo().getStoreNo());
            viewModel.setCanView(!periodStoreProfitStatistic.getStoreInfo().isDeleted());
            return viewModel;
        });
        Double totalAmount = periodStoreProfitStatisticService.getTotalAmount(request);
        pageList.setTotalAmount(NumberFormatUtil.format(totalAmount, Constant.SCALE_TWO, RoundingMode.DOWN));
        return pageList;
    }

    @ApiOperation(value = "导出门店详情结算列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreProfitStatistics",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementProfitShares(@Validated  @RequestBody PeriodStoreProfitStatisticRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","设备ID", "是否达标","结算金额","银行卡号"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","deviceId", "isQualified","settledAmount","bankCard"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Integer index = 0;
        Integer totalPage;
        do{
            request.setPageIndex(index);
            PageList<PeriodStoreProfitStatisticViewModel> pageList = getSettledStoreProfitStatisticViewModelPageList(request);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData = new ExcelData("第" + index + "页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index < totalPage);
        String filename="门店结算列表_"+request.getSettledStoreProfitId()+".xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询结算管理列表", notes = "创建人：王英峰")
    @PostMapping(value = "/getSettledManageList")
    public InvokeResult<PageList<SettledStoreManageViewModel>> getSettledManageList(@Validated @RequestBody SettledStoreManageRequest request,BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<SettledStoreProfit> pages = settledStoreProfitService.getSettledManageList(request);
        PageList<SettledStoreManageViewModel> pageList = ApiBeanUtils.convertToPageList(pages, settledStoreProfit->{
            SettledStoreManageViewModel settledStoreManageViewModel = new SettledStoreManageViewModel();
            BeanUtils.copyProperties(settledStoreProfit,settledStoreManageViewModel);

            SettledStatusEnum settledStatus = EnumUtils.toEnum(settledStoreProfit.getSettleStatus(), SettledStatusEnum.class);
            settledStoreManageViewModel.setSettleStatusName(settledStatus.getDisplayName());
            settledStoreManageViewModel.setSettledAmount(NumberFormatUtil.format(settledStoreProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.DOWN));
            settledStoreManageViewModel.setCreator(getUserNickname(settledStoreProfit.getCreatorId()));
            return settledStoreManageViewModel;
        });
        pageList.setCanCreate(canCreate());
        return InvokeResult.SuccessResult(pageList);
    }

    private Boolean canCreate(){
        Date now=new Date();
        HistoricalParamConfigTypeEnum configType=HistoricalParamConfigTypeEnum.SETTLEMENT_DATE;
        HistoricalParamConfig advertisementProfitShareConfig=advertisementProfitShareConfigService.getHistoricalParamConfig(configType.getValue(),configType.getGroup().getValue(),now,"");
        if(advertisementProfitShareConfig==null)
            throw new BusinessException("请配置收益结算日期");
        LocalDate sysSettleDate= DateUtils.getFixedDayOfCurrentMonth(advertisementProfitShareConfig.getValue().intValue());
        Date availableSettleMonth=new DateTime(org.joda.time.LocalDate.now().toDateTimeAtStartOfDay().dayOfMonth().withMinimumValue().minusMonths(1)).toDate();
        Date firstSettleDate=settledStoreProfitService.getFirstSettleDate(availableSettleMonth);
        return !(new LocalDate(now).isBefore(sysSettleDate) && (firstSettleDate !=null) && firstSettleDate.getTime()==availableSettleMonth.getTime());
    }

    @ApiOperation(value = "删除结算管理信息", notes = "创建人：王英峰")
    @RequestMapping(value="{id}/delete",method = RequestMethod.POST)
    public InvokeResult deleteSettledManageInfo(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        settledStoreProfitService.deleteSettledManageInfo(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "结算消息提醒", notes = "创建人：杨浩")
    @RequestMapping(value = "settledMessage/notify", method = RequestMethod.GET)
    public InvokeResult<SettledMessageNoticeViewModel> settledMessageNotify() {
        SettledMessageNoticeViewModel settledMessageNoticeViewModel = new SettledMessageNoticeViewModel();

        RoleTypeEnum roleTypeEnum = getUser().getRoleTypeEnum();
        if(!(roleTypeEnum==RoleTypeEnum.ManagerialStaff||roleTypeEnum==RoleTypeEnum.Admin)){
            return InvokeResult.SuccessResult(settledMessageNoticeViewModel);
        }

        HistoricalParamConfig advertisementProfitShareConfig = advertisementProfitShareConfigService
            .getHistoricalParamConfigFromCache(
                HistoricalParamConfigTypeEnum.MESSAGE_REMINDER_DATE, new Date());

        if (advertisementProfitShareConfig == null) {
            return InvokeResult.Fail("广告结算消息提醒日期不存在, 请与系统管理员联系");
        }

        ComparisonTypeEnum comparisonType = EnumUtils.toEnum(
            advertisementProfitShareConfig.getComparisonType(), ComparisonTypeEnum.class);

        if (!ComparisonUtil.compare(LocalDate.now().getDayOfMonth(),
            advertisementProfitShareConfig.getValue(), comparisonType)) {
            return InvokeResult.SuccessResult(settledMessageNoticeViewModel);
        }

        Date unSettledDate = settledStoreProfitService.findByStatusAndSettledMonth();
        if (unSettledDate != null) {
            settledMessageNoticeViewModel.setRemind(true);
            settledMessageNoticeViewModel.setRemindDate(unSettledDate);
        }
        return InvokeResult.SuccessResult(settledMessageNoticeViewModel);
    }

}

