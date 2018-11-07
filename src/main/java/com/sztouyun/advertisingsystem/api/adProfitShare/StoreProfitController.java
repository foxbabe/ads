package com.sztouyun.advertisingsystem.api.adProfitShare;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.StoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.service.adProfitShare.PeriodStoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.adProfitShare.StoreProfitStreamService;
import com.sztouyun.advertisingsystem.thirdpart.StoreSourceImplFactory;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreQueryRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoMonthList;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeDetailRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.*;

/**
 * 门店收益
 * @author guangpu.yan
 * @create 2018-01-11 12:23
 **/
@Api("门店收益接口")
@RestController
@RequestMapping("/api/StoreProfit")
public class StoreProfitController extends BaseApiController {

    @Autowired
    StoreProfitStreamService storeProfitStreamService;

    @Autowired
    private PeriodStoreProfitStatisticService periodStoreProfitStatisticService;

    @ApiOperation(value = "查询收益流水列表", notes = "创建人：晏光普")
    @PostMapping(value = "/stream")
    public InvokeResult<PageList<StoreProfitStreamViewListModel>> getStoreProfitStream(@Validated @RequestBody StoreProfitStreamRequest request){
        Page<StoreProfitStatistic> pages = storeProfitStreamService.getStoreProfitStream(request);
        PageList<StoreProfitStreamViewListModel> resultList = ApiBeanUtils.convertToPageList(pages, storeProfitStatistic -> {
            StoreProfitStreamViewListModel storeProfitStreamViewModel = new StoreProfitStreamViewListModel();
            BeanUtils.copyProperties(storeProfitStatistic, storeProfitStreamViewModel);
            BeanUtils.copyProperties(storeProfitStatistic.getStoreProfitStatisticExtension(), storeProfitStreamViewModel);
            if(storeProfitStreamViewModel.getTotalAdvertisementCount()!=null && storeProfitStreamViewModel.getTotalAdvertisementCount()!=0){
                //计算收益广告占比
                storeProfitStreamViewModel.setProportion(NumberFormatUtil.format(storeProfitStreamViewModel.getEffectiveAdvertisementCount().longValue(),storeProfitStreamViewModel.getTotalAdvertisementCount().longValue(),Constant.RATIO_PATTERN));
            }
            storeProfitStreamViewModel.setOpeningTimeConvert(DateUtils.formatOneDayTimes((long)(storeProfitStatistic.getStoreProfitStatisticExtension().getOpeningTime() * 60D * 60D)));
            return storeProfitStreamViewModel;
        });
        Double shareAmountCount = storeProfitStreamService.getShareAmountCount(request);
        resultList.setTotalAmount(NumberFormatUtil.format(shareAmountCount, Constant.SCALE_TWO, RoundingMode.HALF_UP));
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "导出收益流水", notes = "创建人: 晏光普")
    @PostMapping(value = "/exportStoreProfitStream")
    public InvokeResult export(@Validated @RequestBody StoreProfitStreamRequest request, HttpServletResponse response, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader excelHeader=new ExcelHeader(
                null,
                new String[]{"编号","日期","收益金额","广告总数","收益广告数量","收益广告占比","开机是否达标","订单是否达标","开机时长","日交易订单数量","是否结算"},
                new String[]{"profitDate","shareAmount","totalAdvertisementCount","effectiveAdvertisementCount","proportion","openingTimeStandardIs","orderStandardIs","openingTimeConvert","orderNum","settled"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<StoreProfitStreamViewListModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            pageList= getStoreProfitStream(request).getData();
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(excelHeader).addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                put("profitDate",TimeFormatEnum.Default);
            }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="收益流水.xls";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询收益流水详情", notes = "创建人：晏光普")
    @GetMapping(value = "/storeProfitStreamDetail")
    public InvokeResult<StoreProfitStreamViewDetailModel> getStoreProfitStreamDetail(@RequestParam(required = false)String id){
        StoreProfitStreamViewDetailModel result = new StoreProfitStreamViewDetailModel();
        StoreProfitStatistic storeProfitStatistic = storeProfitStreamService.getStoreProfitStreamDetail(id);
        if(storeProfitStatistic!=null){
            BeanUtils.copyProperties(storeProfitStatistic.getStoreInfo(),result);
            BeanUtils.copyProperties(storeProfitStatistic.getStoreProfitStatisticExtension(),result);
            BeanUtils.copyProperties(storeProfitStatistic,result);
            result.setProvinceName(getAreaName(storeProfitStatistic.getStoreInfo().getProvinceId()));
            result.setCityName(getAreaName(storeProfitStatistic.getStoreInfo().getCityId()));
            result.setRegionName(getAreaName(storeProfitStatistic.getStoreInfo().getRegionId()));
            result.setStoreCreatedTime(DateUtils.getIntervalDays(storeProfitStatistic.getStoreInfo().getCreatedTime(),storeProfitStatistic.getProfitDate()));
            result.setCycleBegin(DateUtils.addDays(storeProfitStatistic.getProfitDate(),-29));
            result.setCycleEnd(storeProfitStatistic.getProfitDate());
            result.setOpeningTimeConvert(DateUtils.formatOneDayTimes((long)(storeProfitStatistic.getStoreProfitStatisticExtension().getOpeningTime() * 60D * 60D)));
        }
        return InvokeResult.SuccessResult(result);
    }

    @ApiOperation(value = "查询心跳时间", notes = "创建人：杨浩")
    @PostMapping(value = "/heart")
    public InvokeResult<PageList<HeartBeatViewModel>> getHeartDate(@Validated @RequestBody HeartViewModel heartViewModel,
                                                                   BindingResult result) {
        PageList<HeartBeatViewModel> resultList = new PageList<>();
        StoreProfitStatistic storeProfitStatistic = storeProfitStreamService
            .getStoreProfitStreamDetail(heartViewModel.getId());

        if (storeProfitStatistic != null) {
            StoreOpeningTimeDetailRequest storeOpeningTimeDetailRequest = new StoreOpeningTimeDetailRequest(
                storeProfitStatistic.getProfitDate(),
                storeProfitStatistic.getStoreInfo().getStoreNo());
            storeOpeningTimeDetailRequest.setPageIndex(heartViewModel.getPageIndex());
            storeOpeningTimeDetailRequest.setPageSize(heartViewModel.getPageSize());
            Page<Date> pages = StoreSourceImplFactory
                    .getInstance(EnumUtils.toEnum(storeProfitStatistic.getStoreInfo().getStoreSource(), StoreSourceEnum.class))
                    .getStoreOpeningTimeDetail(storeOpeningTimeDetailRequest);
            resultList = ApiBeanUtils.convertToPageList(pages, r ->  new HeartBeatViewModel(r));
        }
        return InvokeResult.SuccessResult(resultList);
    }


    @ApiOperation(value = "门店分成金额统计", notes = "创建人：王伟权")
    @GetMapping(value = "/storeProfitStatisticWithMonth/{storeId}")
    public InvokeResult<StoreProfitStatisticWithMonthViewModel> storeProfitStatisticWithMonth(@PathVariable String storeId) {
        if(org.springframework.util.StringUtils.isEmpty(storeId))
            return InvokeResult.Fail("门店ID不能为空");
        return InvokeResult.SuccessResult(periodStoreProfitStatisticService.storeProfitStatisticWithMonth(storeId));
    }



    @ApiOperation(value = "月结算明细", notes = "创建人：王伟权")
    @PostMapping(value = "/periodDetailItem")
    public InvokeResult<PageList<PeriodDetailItemViewModel>> periodDetailItem(@Validated @RequestBody PeriodDetailItemRequestViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<PeriodStoreProfitStatistic> page = periodStoreProfitStatisticService.getPeriodDetailItem(viewModel);
        PageList<PeriodDetailItemViewModel> resultList = ApiBeanUtils.convertToPageList(page, item -> {
            PeriodDetailItemViewModel periodDetailItemViewModel = ApiBeanUtils.copyProperties(item, PeriodDetailItemViewModel.class);
            periodDetailItemViewModel.setDate(item.getSettledMonth());
            periodDetailItemViewModel.setStartDate(new LocalDate(item.getSettledMonth()).dayOfMonth().withMinimumValue().toDate());
            periodDetailItemViewModel.setEndDate(new LocalDate(item.getSettledMonth()).dayOfMonth().withMaximumValue().toDate());
            periodDetailItemViewModel.setShareAmount(Double.valueOf(NumberFormatUtil.format(periodDetailItemViewModel.getShareAmount(), Constant.SCALE_TWO)));
            return periodDetailItemViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "广告详情下单个门店的月份列表", notes = "创建人：毛向军")
    @PostMapping(value = "/monthList")
    public InvokeResult<List<StoreInfoViewModel>> getStoreInfoViewModelMonthList(@Validated @RequestBody AdvertisementPeriodStoreProfitStatisticRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<StoreInfoMonthList> storeInfoMonthList = periodStoreProfitStatisticService.getStoreMonthProfitList(request);
        return InvokeResult.SuccessResult(getStoreInfoMonthList(storeInfoMonthList));
    }


    @ApiOperation(value = "导出广告详情下多个门店的月份列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreProfitStatistics",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementProfitShares(@Validated  @RequestBody ContractStoreQueryRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID", "是否达标","是否可用","分成月份","分成金额","已结算","未结算"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId", "isQualified","available","shareAmountMinMonth","totalShareAmount","settledAmount","unsettledAmount"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Integer index=0;
        Integer totalPage;
        do{
            request.setPageIndex(index);
            List<StoreInfoMonthList> storeInfoMonthList = periodStoreProfitStatisticService.getAdvertisementStoreMonthProfitList(request);
            List<StoreInfoViewModel> list = getStoreInfoMonthList(storeInfoMonthList);
            totalPage=1;
            ExcelData excelData=new ExcelData("第"+index+"页").addData(list).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("shareAmountMinMonth",TimeFormatEnum.Month);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="广告详情下多个门店的月份列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    private List<StoreInfoViewModel> getStoreInfoMonthList(List<StoreInfoMonthList> storeInfoMonthList){
        Map<String,String> areaMap=areaService.getAllAreaNames();
        List<StoreInfoViewModel> list = new ArrayList<>();
        storeInfoMonthList.stream().forEach(storeInfoViewMonth->{
            StoreInfoViewModel storeInfoViewModel = ApiBeanUtils.copyProperties(storeInfoViewMonth, StoreInfoViewModel.class);
            storeInfoViewModel.setCityName(getAreaName(storeInfoViewModel.getCityId(), areaMap));
            storeInfoViewModel.setProvinceName(getAreaName(storeInfoViewModel.getProvinceId(), areaMap));
            storeInfoViewModel.setRegionName(getAreaName(storeInfoViewModel.getRegionId(), areaMap));
            storeInfoViewModel.setTotalShareAmount(NumberFormatUtil.format(storeInfoViewMonth.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            if(storeInfoViewMonth.getSettled()){
                storeInfoViewModel.setSettledAmount(NumberFormatUtil.format(storeInfoViewMonth.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            }else {
                storeInfoViewModel.setUnsettledAmount(NumberFormatUtil.format(storeInfoViewMonth.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            }
            storeInfoViewModel.setShareAmountMinMonth(storeInfoViewMonth.getShareAmountMonth());
            storeInfoViewModel.setShareAmountMaxMonth(storeInfoViewMonth.getShareAmountMonth());
            list.add(storeInfoViewModel);
        });
        return list;
    }
}
