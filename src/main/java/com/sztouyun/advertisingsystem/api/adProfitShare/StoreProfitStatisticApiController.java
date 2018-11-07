package com.sztouyun.advertisingsystem.api.adProfitShare;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStoreProfit;
import com.sztouyun.advertisingsystem.model.adProfitShare.StoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.adProfitShare.PeriodStoreProfitStatisticService;
import com.sztouyun.advertisingsystem.service.adProfitShare.SettledStoreProfitService;
import com.sztouyun.advertisingsystem.service.adProfitShare.StoreProfitStatisticService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Api(value = "门店收益接口")
@RestController
@RequestMapping("/api/storeprofit")
public class StoreProfitStatisticApiController extends BaseApiController {
    @Autowired
    private StoreProfitStatisticService storeProfitStatisticService;
    @Autowired
    private SettledStoreProfitService settledStoreProfitService;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private PeriodStoreProfitStatisticService  periodStoreProfitStatisticService;

    @ApiOperation(value = "广告分成概要统计",notes = "创建人：王英峰")
    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET)
    public InvokeResult<StoreProfitStatisticViewModel> getAdProfitStatistic(@PathVariable String storeId){
        if(StringUtils.isEmpty(storeId))
            return InvokeResult.Fail("门店id不能为空");

        StoreProfitStatisticViewModel viewModel = storeProfitStatisticService.getStoreProfitStatistic(storeId);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "广告分成收入曲线",notes = "创建人：王英峰")
    @RequestMapping(value = "/adProfitIncomeCurve", method = RequestMethod.POST)
    public InvokeResult<StoreProfitCurveViewModel> getAdProfitIncomeCurve(@Validated @RequestBody StoreProfitCurveRequest storeProfitCurveRequest, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<StoreProfitCurveData> resultList = new ArrayList<>();
        List<Date> dateList = DateUtils.getDateList(storeProfitCurveRequest.getStartDate(), storeProfitCurveRequest.getEndDate(), Calendar.DAY_OF_MONTH);
        dateList.forEach(date -> {
            StoreProfitCurveData storeProfitCurveData = new StoreProfitCurveData();
            storeProfitCurveData.setProfitDate(date);
            resultList.add(storeProfitCurveData);
        });

        List<StoreProfitStatistic> storeProfitStatisticList = storeProfitStatisticService.getStoreProfitCurve(storeProfitCurveRequest);
        Double totalAmount = 0.0;
        for (StoreProfitStatistic storeProfitStatistic: storeProfitStatisticList) {
            totalAmount += storeProfitStatistic.getShareAmount();
            resultList.forEach(resultBean ->{
                if(DateUtils.isEqualsDateByYearMonthDay(resultBean.getProfitDate(),storeProfitStatistic.getProfitDate())){
                    BeanUtils.copyProperties(storeProfitStatistic,resultBean);
                    resultBean.setShareAmount(NumberFormatUtil.format(storeProfitStatistic.getShareAmount(),Constant.SCALE_TWO, RoundingMode.DOWN));
                    resultBean.setShareAdvertisementPr(NumberFormatUtil.format(storeProfitStatistic.getEffectiveAdvertisementCount().longValue(),
                            storeProfitStatistic.getTotalAdvertisementCount().longValue(), Constant.RATIO_PATTERN));
                }
            });
        }
        StoreProfitCurveViewModel storeProfitCurveViewModel = new StoreProfitCurveViewModel();
        storeProfitCurveViewModel.setTotalShareAmount(NumberFormatUtil.format(totalAmount,Constant.SCALE_TWO, RoundingMode.DOWN));
        storeProfitCurveViewModel.setProfitList(resultList);

        return InvokeResult.SuccessResult(storeProfitCurveViewModel);
    }


    @ApiOperation(value = "获取分成概要",notes = "文丰")
    @GetMapping(value = "/profitStatistic")
    public InvokeResult<ProfitStatisticViewModel> getProfitStatistic(){
        ProfitOverview profitOverview = storeProfitStatisticService.getProfitOverviewInfo();
        ProfitStatisticViewModel result=new ProfitStatisticViewModel();
        result.setTotalShareAmount(NumberFormatUtil.format(profitOverview.getTotalShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
        result.setSettledAmount(NumberFormatUtil.format(profitOverview.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
        result.setUnsettledAmount(NumberFormatUtil.format(profitOverview.getTotalShareAmount() - profitOverview.getSettledAmount(),Constant.SCALE_TWO, RoundingMode.HALF_UP));
        return InvokeResult.SuccessResult(result);
    }

    @ApiOperation(value = "获取分成概要和分成概览",notes = "修改人：王伟权")
    @RequestMapping(value = "/profitOverview", method = RequestMethod.POST)
    public InvokeResult<ProfitOverviewViewModel> getProfitOverview(@Validated @RequestBody ProfitOverviewRequest profitOverviewRequest, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        ProfitOverview profitOverview = storeProfitStatisticService.getProfitOverviewInfo();
        ProfitOverviewViewModel viewModel = new ProfitOverviewViewModel();
        List<ProfitOverviewDataViewModel> resultList = new ArrayList<>();
        List<Date> dateList = DateUtils.getDateList(profitOverviewRequest.getStartDate(), profitOverviewRequest.getEndDate(),Calendar.MONTH);
        dateList.forEach(date -> {
            ProfitOverviewDataViewModel profitOverviewDataViewModel = new ProfitOverviewDataViewModel();
            profitOverviewDataViewModel.setSelDate(date);
            resultList.add(profitOverviewDataViewModel);
        });

        List<ProfitSettledInfo> dataList = storeProfitStatisticService.getProfitOverviewStatistic(profitOverviewRequest);
        dataList.forEach(data->{
            resultList.forEach(resultBean -> {
                        if (DateUtils.isEqualsDateByYearMonth(resultBean.getSelDate(), data.getYear(),data.getMonth())) {
                            resultBean.setShareAmountPr(NumberFormatUtil.format(data.getShareAmount(),profitOverview.getTotalShareAmount(), Constant.RATIO_PATTERN));
                            resultBean.setShareAmount(NumberFormatUtil.format(data.getShareAmount(),Constant.SCALE_TWO,RoundingMode.DOWN));
                            resultBean.setSettledAmount(NumberFormatUtil.format(data.getSettledAmount(),Constant.SCALE_TWO,RoundingMode.DOWN));
                            resultBean.setUnsettledAmount(NumberFormatUtil.format(data.getUnsettledAmount(),Constant.SCALE_TWO,RoundingMode.HALF_UP));
                        }
                    });
        });
        viewModel.setProfitOverviewDataList(resultList);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "查询门店月流水包含收益地区", notes = "创建人:文丰")
    @GetMapping("/areas")
    public InvokeResult<List<AreaViewModel>> getAreasWithDailyProfit() {
        List<Area> areas = storeProfitStatisticService.getAreasWithPeriodProfit();
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value="门店月流水列表",notes = "文丰")
    @RequestMapping(value = "/dailyStoreProfitList", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreProfitItem>> getPeriodStoreProfitList(@Validated @RequestBody PeriodStoreProfitStatisticRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getStoreProfitPageList(request));
    }

    private PageList<StoreProfitItem> getStoreProfitPageList(PeriodStoreProfitStatisticRequest request){
        Page<PeriodStoreProfitStatistic> pages = periodStoreProfitStatisticService.getSettledStoreProfitStatistic(request);
        PageList<StoreProfitItem> resultList = getStoreProfitItemPageList(pages);
        Double totalAmount=periodStoreProfitStatisticService.getTotalAmount(request);
        resultList.setTotalAmount(NumberFormatUtil.format(totalAmount, Constant.SCALE_TWO, RoundingMode.DOWN));
        return  resultList;
    }

    private PageList<StoreProfitItem> getStoreProfitItemPageList(Page<PeriodStoreProfitStatistic> pages) {
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(pages, storeProfitStatistic->{
            StoreProfitItem item=ApiBeanUtils.copyProperties(storeProfitStatistic,StoreProfitItem.class);
            item.setShareAmount(NumberFormatUtil.format(storeProfitStatistic.getShareAmount(), Constant.SCALE_TWO, RoundingMode.DOWN));
            setStoreInfo(item,storeProfitStatistic.getStoreInfo(),areaMap);
            return item;
        });
    }

    private void setStoreInfo(StoreProfitItem item,StoreInfo storeInfo,Map<String,String> areaMap){
        item.setStoreName(storeInfo.getStoreName());
        item.setShopId(storeInfo.getStoreNo());
        item.setProvinceName(areaMap.get(storeInfo.getProvinceId()));
        item.setCityName(areaMap.get(storeInfo.getCityId()));
        item.setDeviceId(storeInfo.getDeviceId());
        item.setRegionName(areaMap.get(storeInfo.getRegionId()));
        item.setStoreAddress(storeInfo.getStoreAddress());
        item.setCanView(!storeInfo.isDeleted());
        item.setIsQualified(storeInfo.getIsQualified());
    }

    private void setStoreInfo(StoreProfitItem item,StoreProfitBean storeProfitBean,Map<String,String> areaMap){
        item.setProvinceName(areaMap.get(storeProfitBean.getProvinceId()));
        item.setCityName(areaMap.get(storeProfitBean.getCityId()));
        item.setRegionName(areaMap.get(storeProfitBean.getRegionId()));

    }

    @ApiOperation(value="门店分成列表导出",notes = "文丰")
    @RequestMapping(value = "/exportDailyStoreProfit", method = RequestMethod.POST)
    public InvokeResult export(@Validated @RequestBody PeriodStoreProfitStatisticRequest request, HttpServletResponse response, BindingResult result){
        if(result.hasErrors())
            ValidateFailResult(result);
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        Workbook wb=new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID", "是否达标","分成日期","分成金额(元)","是否结算"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId", "isQualified","settledMonth","shareAmount","settled"},
                CellStyle.ALIGN_CENTER
        );
        Integer index = 0;
        Integer totalPage;
        String filename="门店分成流水报表.xlsx";
        do{
            request.setPageIndex(index);
            PageList<StoreProfitItem> pageList = getStoreProfitPageList(request);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData = new ExcelData("第" + index + "页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("settledMonth",TimeFormatEnum.Month);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while( index<totalPage);
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();

    }

    @ApiOperation(value = "查询包含未结算门店收益的地区")
    @PostMapping("/profitAreas")
    public InvokeResult<List<AreaViewModel>>  getAreasOfUnsettledStoreProfit(@Validated @RequestBody BaseSettledStoreProfitRequest request,BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<Area>  areas=storeProfitStatisticService.getAreaOfUnsettledStoreProfit(request.getId());
        List<String>  checkedAreas=storeProfitStatisticService.getCheckedUnsettledAreaIds(request.getId());
        storeProfitStatisticService.addSpecialNode(areas);
        List<AreaViewModel> list = ApiBeanUtils.convertToAreaTreeListWithRootNode(areas, area -> {
            AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            if(!checkedAreas.isEmpty()){
                if(checkedAreas.contains(area.getId())){
                    areaViewModel.setChecked(Boolean.TRUE);
                }
                if(areaViewModel.getName().equals(Constant.AREA_ABNORMAL_NODE_NAME) && Linq4j.asEnumerable(checkedAreas).any(region -> region.isEmpty())) {
                    areaViewModel.setChecked(true);
                }
            }
            return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value="新建结算，查询门店分成月流水列表",notes = "文丰")
    @RequestMapping(value = "/unsettledPeriodStoreProfitList", method = RequestMethod.POST)
    private InvokeResult<PageList<StoreProfitItem>> getStoreProfitList(@Validated @RequestBody UnsettledPeriodStoreProfitListRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        if(request.getAreaIds().isEmpty())
            throw new BusinessException("请选择结算地区");
        Map<String,String> areaMap=areaService.getAllAreaNames();
        if(request.getAreaIds().contains(Constant.AREA_ABNORMAL_NODE_ID)){
            request.setHasAbnormalNode(Boolean.TRUE);
        }
        return InvokeResult.SuccessResult(getStoreProfitPageList(request,areaMap));
    }

    private PageList<StoreProfitItem> getStoreProfitPageList(UnsettledPeriodStoreProfitListRequest request, Map<String,String> areaMap){
        SettledStoreProfit settledStoreProfit=null;
        String id=request.getId();
        if(!StringUtils.isEmpty(id)) {
            settledStoreProfit = settledStoreProfitService.findSettledStoreProfitByID(id);
            //切换月份，清空结算
            if(request.getSettledMonth()!=null && settledStoreProfit.getSettledMonth().getTime()!=request.getSettledMonth().getTime()){
                settledStoreProfitService.emptyBySettledStoreProfitId(request.getId());
                request.setId("");
                settledStoreProfit=null;
            }else{
                request.setSettledMonth(settledStoreProfit.getSettledMonth());
            }
        }
        Page<StoreProfitBean> pages = storeProfitStatisticService.getUnsettledStoreProfitList(request);
        PageList<StoreProfitItem> resultList = getSettledStoreProfitItemPageList(areaMap, pages);
        if(settledStoreProfit!=null){
            resultList.setChooseCount(settledStoreProfit.getStoreCount());
            resultList.setTotalAmount(NumberFormatUtil.format(settledStoreProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.DOWN));
        }
        return  resultList;
    }

    @ApiOperation(value = "选择单个门店月流水",notes = "文丰")
    @RequestMapping(value = "/settleStoreProfit", method = RequestMethod.POST)
    private InvokeResult<SettleProfitViewModel> settleStoreProfit(@Validated @RequestBody SettledStoreProfitRequest request,BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        SettledStoreProfit settledStoreProfit=null;
        validateSettleMonth(request.getSettledMonth());
        if(StringUtils.isEmpty(request.getId())){
            settledStoreProfit= settledStoreProfitService.create(request.getPeriodStoreProfitStatisticId(),request.getSettledMonth());
        }else{
            settledStoreProfit=settledStoreProfitService.addPeriodStoreProfitStatistic(request.getId(),request.getPeriodStoreProfitStatisticId(),request.getSettledMonth());
        }
        if(settledStoreProfit==null)
            return InvokeResult.SuccessResult(new SettleProfitViewModel());
        return InvokeResult.SuccessResult(new SettleProfitViewModel(settledStoreProfit.getId(),NumberFormatUtil.format( settledStoreProfit.getSettledAmount(),Constant.SCALE_TWO, RoundingMode.DOWN),settledStoreProfit.getStoreCount()));
    }

    private PageList<StoreProfitItem> getSettledStoreProfitItemPageList(Map<String, String> areaMap, Page<StoreProfitBean> pages) {
        return ApiBeanUtils.convertToPageList(pages, storeProfitBean->{
            StoreProfitItem item=ApiBeanUtils.copyProperties(storeProfitBean,StoreProfitItem.class);
            item.setShareAmount(NumberFormatUtil.formatToString(new BigDecimal(storeProfitBean.getShareAmount())));
            setStoreInfo(item,storeProfitBean,areaMap);
            return item;
        });
    }

    @ApiOperation(value = "一键全选")
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    private InvokeResult<SettleProfitViewModel> selectAll(@Validated @RequestBody UnsettledPeriodStoreProfitListRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        validateSettleMonth(request.getSettledMonth());
        SettledStoreProfit settledStoreProfit =settledStoreProfitService.selectAll(request);
        return InvokeResult.SuccessResult(new SettleProfitViewModel(settledStoreProfit.getId(),NumberFormatUtil.format(settledStoreProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.DOWN),settledStoreProfit.getStoreCount()));
    }

    private void validateSettleMonth(Date settleMonth){
        if(settleMonth==null)
            throw new BusinessException("必须选择结算月份");
        Date firstSettleDate=settledStoreProfitService.getFirstSettleDate(settleMonth);
        if(firstSettleDate==null)
            throw new BusinessException("所选结算月份无待结算门店");
        if( firstSettleDate.getTime()!=settleMonth.getTime())
            throw new BusinessException("存在"+DateUtils.dateFormat(firstSettleDate,Constant.DATA_YM_CN)+"的未结算门店，请先结算"+DateUtils.dateFormat(firstSettleDate,Constant.DATA_YM_CN)+"的门店数据");
    }

    @ApiOperation(value="查询门店分成月流水列表,多月未结算展开项",notes = "文丰")
    @RequestMapping(value = "/subUnsettledPeriodStoreProfitList", method = RequestMethod.POST)
    private InvokeResult<List<StoreProfitItem>> getStoreProfitList(@Validated @RequestBody SubPeriodStoreProfitListRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return InvokeResult.SuccessResult(getSubPeriodStoreProfitPageList(request,areaMap));
    }

    private List<StoreProfitItem> getSubPeriodStoreProfitPageList(SubPeriodStoreProfitListRequest request, Map<String,String> areaMap){
        List<StoreProfitBean> list = storeInfoMapper.getSubPeriodStoreProfitPageList(request);
        List<StoreProfitItem>  resultList=new ArrayList<>();
        list.stream().forEach(storeProfitBean -> {
            StoreProfitItem item=ApiBeanUtils.copyProperties(storeProfitBean,StoreProfitItem.class);
            item.setShareAmount(NumberFormatUtil.formatToString(new BigDecimal(storeProfitBean.getShareAmount())));
            setStoreInfo(item,storeProfitBean,areaMap);
            resultList.add(item);
        });
        return  resultList;
    }

    @ApiOperation(value = "确认结算接口",notes = "文丰")
    @PostMapping(value = "settle/{id}")
    private InvokeResult<String> settle(@PathVariable("id") String id){
        settledStoreProfitService.settle(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "确认新建结算接口",notes = "文丰")
    @PostMapping(value = "settle/confirmCreate/{id}")
    private InvokeResult<String> confirmCreate(@PathVariable("id") String id){
        settledStoreProfitService.confirmCreate(id);
        return InvokeResult.SuccessResult();
    }
}
