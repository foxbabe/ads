package com.sztouyun.advertisingsystem.api.coorperationPartner;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.mapper.PartnerMapper;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerAdProfitViewModel;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerProfitStreamRequestViewModel;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerProfitStreamViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "合作方收益")
@RestController
@RequestMapping("/api/partner/profit")
public class PartnerProfitApiController extends BaseApiController {

    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;
    @Autowired
    private HistoricalParamConfigService historicalParamConfigService;
    @Autowired
    private PartnerMapper partnerMapper;

    @ApiOperation(value = "合作方收益统计概览", notes = "创建人: 毛向军")
    @GetMapping("/{partnerId}/statistic")
    public InvokeResult<CooperationPartnerProfitStatisticViewModel> statistic(@PathVariable("partnerId") String partnerId) {
        Date yesterday = LocalDate.now().minusDays(1).toDate();
        Date week = LocalDate.now().minusDays(6).toDate();
        Date month = LocalDate.now().withDayOfMonth(1).toDate();
        return InvokeResult.SuccessResult(cooperationPartnerService.profitStatistic(partnerId,yesterday,week,month));
    }

    @ApiOperation(value = "合作方收益趋势", notes = "创建人: 毛向军")
    @PostMapping("/profitTrend")
    public InvokeResult<CooperationPartnerLineChartProfitTrendViewModel> partnerProfitTrendLineChart(@Validated @RequestBody CooperationPartnerPieChartRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Date startTime = request.getStartTime();
        Date endTime = request.getEndTime();
        if(request.getStartTime()==null||request.getEndTime()==null)
            return InvokeResult.Fail("开始时间或结束时间不能为空");

        CooperationPartnerLineChartProfitTrendViewModel viewModel = new CooperationPartnerLineChartProfitTrendViewModel();
        Map<Date,CooperationPartnerLineChartProfitTrendInfo> map = new HashMap<>();

        List<CooperationPartnerLineChartProfitTrendInfo> cooperationPartnerLineChartProfitTrendInfos = cooperationPartnerService.partnerProfitTrendLineChart(request.getCooperationPartnerId(), startTime, endTime);

        for (CooperationPartnerLineChartProfitTrendInfo cooperationPartnerLineChartProfitTrendInfo : cooperationPartnerLineChartProfitTrendInfos) {
            map.put(cooperationPartnerLineChartProfitTrendInfo.getDate(),cooperationPartnerLineChartProfitTrendInfo);
            viewModel.setProfitAmount(viewModel.getProfitAmountLong()+cooperationPartnerLineChartProfitTrendInfo.getProfitAmountLong());
            viewModel.setValidDisplayTimes(viewModel.getValidDisplayTimes()+cooperationPartnerLineChartProfitTrendInfo.getValidDisplayTimes());
        }
        while (!endTime.before(startTime)){
            if(map.get(startTime)==null){
                cooperationPartnerLineChartProfitTrendInfos.add(new CooperationPartnerLineChartProfitTrendInfo(startTime));
            }
            startTime =new LocalDate(startTime).plusDays(1).toDate();
        }
        cooperationPartnerLineChartProfitTrendInfos.sort(Comparator.comparing(CooperationPartnerLineChartProfitTrendInfo::getDate));
        viewModel.setCooperationPartnerLineChartProfitTrendInfos(cooperationPartnerLineChartProfitTrendInfos);
        return InvokeResult.SuccessResult(viewModel);
    }


    @ApiOperation(value = "收益流水", notes = "创建人: 王伟权")
    @PostMapping(value = "/profitStream")
    public InvokeResult<PageList<PartnerProfitStreamViewModel>> profitStream(@Validated @RequestBody PartnerProfitStreamRequestViewModel request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        return InvokeResult.SuccessResult(getProfitStreamPageList(request));
    }

    @ApiOperation(value = "导出收益流水", notes = "创建人: 王伟权")
    @PostMapping(value = "/exportProfitStream")
    public InvokeResult exportProfitStream(@Validated @RequestBody PartnerProfitStreamRequestViewModel request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","收益时间","有效次数","计费模式","单价","收益金额"},
                new String[]{"date","validTimes","chargingPatternName","unitPrice","profitAmount"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);

        PageList<PartnerProfitStreamViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getProfitStreamPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="合作方收益流水列表";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    public PageList<PartnerProfitStreamViewModel> getProfitStreamPageList(PartnerProfitStreamRequestViewModel request) {
        Page<PartnerProfitStreamViewModel> pages = partnerAdvertisementService.profitStream(request);
        return  ApiBeanUtils.convertToPageList(pages, viewModel -> {
            HistoricalParamConfig chargingPatternConfig = historicalParamConfigService
                    .getHistoricalParamConfigFromCache(null, HistoricalParamConfigGroupEnum.PartnerProfitMode.getValue(), viewModel.getDate(), viewModel.getPartnerId());
            if (chargingPatternConfig != null) {
                viewModel.setChargingPatternName(EnumUtils.toEnum(chargingPatternConfig.getType(), HistoricalParamConfigTypeEnum.class).getDisplayName());
                viewModel.setUnitPriceDigit(chargingPatternConfig.getValue());
                viewModel.setUnit(chargingPatternConfig.getUnit());
                viewModel.setUnitName(EnumUtils.getDisplayName(chargingPatternConfig.getUnit(), UnitEnum.class));
            }
            return viewModel;
        });
    }


    @ApiOperation(value = "广告位收益统计", notes = "创建人: 王伟权")
    @PostMapping(value = "/advertisementPositionProfitStatistic")
    public InvokeResult<PageList<PartnerAdProfitViewModel>> advertisementPositionProfitStatistic(@Validated @RequestBody PartnerProfitStreamRequestViewModel request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        return InvokeResult.SuccessResult(getAdvertisementPositionProfitStatistic(request));
    }

    @ApiOperation(value = "导出广告位收益统计", notes = "创建人: 王伟权")
    @PostMapping(value = "/exportAdvertisementPositionProfitStatistic")
    public InvokeResult exportAdvertisementPositionProfitStatistic(@Validated @RequestBody PartnerProfitStreamRequestViewModel request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","广告位","有效次数","收益金额"},
                new String[]{"adPositionName","validTimes","profitAmount"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);

        PageList<PartnerAdProfitViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementPositionProfitStatistic(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="合作方广告位收益列表";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }


    public PageList<PartnerAdProfitViewModel> getAdvertisementPositionProfitStatistic(PartnerProfitStreamRequestViewModel request) {
        Page<PartnerAdProfitViewModel> pages = partnerAdvertisementService.getAdvertisementPositionProfitStatistic(request);
        return ApiBeanUtils.convertToPageList(pages, viewModel -> {
            viewModel.setAdPositionName(EnumUtils.toEnum(viewModel.getAdvertisementPositionCategory(), AdvertisementPositionCategoryEnum.class).getDisplayName());
            return viewModel;
        });
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "合作方收益配置列表", notes = "创建人: 文丰")
    @RequestMapping(value = "/configs", method = RequestMethod.POST)
    public InvokeResult<PageList<PartnerProfitConfigInfo>> getProfitShareConfig(@Validated @RequestBody PartnerProfitConfigInfoRequest request,BindingResult result) {
        List<PartnerProfitConfigInfo> list=partnerMapper.getPartnerProfitConfigInfo(request);
        if(CollectionUtils.isEmpty(list))
            return emptyInvokeResult(request.getPageSize());
        Long count=partnerMapper.getPartnerProfitConfigInfoCount(request);
        Pageable pageable=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Map<String,PartnerProfitConfigInfo> timeInfo=partnerMapper.getPartnerProfitConfigTime(list.stream().map(a->a.getObjectId()).collect(Collectors.toList())).stream().collect(Collectors.toMap(a->a.getObjectId(),a->a));
        list.forEach(item->{
            item.setProfitModeName(EnumUtils.getDisplayName(item.getType(),PartnerProfitTypeEnum.class));
            PartnerProfitConfigInfo time=timeInfo.get(item.getObjectId());
            if(time!=null){
                item.setCreatedTime(time.getCreatedTime());
                item.setUpdatedTime(time.getUpdatedTime());
            }
            item.setCreator(getUserNickname(item.getCreatorId()));
        });
        Collections.sort(list,Comparator.comparing(PartnerProfitConfigInfo::getUpdatedTime).reversed());
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList( pageResult(list, pageable, count)));
    }

}
