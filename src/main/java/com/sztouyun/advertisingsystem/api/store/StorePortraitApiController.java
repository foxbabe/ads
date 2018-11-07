package com.sztouyun.advertisingsystem.api.store;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.store.EnvironmentTypeEnum;
import com.sztouyun.advertisingsystem.model.store.StorePortraitEnum;
import com.sztouyun.advertisingsystem.model.store.SurroundingsDistrictEnum;
import com.sztouyun.advertisingsystem.service.store.StoreInfoExtensionService;
import com.sztouyun.advertisingsystem.service.store.StorePortraitService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.utils.excel.MyWorkbook;
import com.sztouyun.advertisingsystem.utils.excel.SheetConfig;
import com.sztouyun.advertisingsystem.utils.excel.SheetHeader;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoUseCountStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "门店画像接口")
@RestController
@RequestMapping("/api/storePortrait")
public class StorePortraitApiController extends BaseApiController {

    @Autowired
    private StoreInfoExtensionService storeInfoExtensionService;
    @Autowired
    private StorePortraitService storePortraitService;
    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "门店画像统计图", notes = "创建者：毛向军")
    @PostMapping(value = "/chartStatistic")
    public InvokeResult<StorePortraitChartStatisticViewModel> chartStatistic(@Validated @RequestBody StorePortraitChartStatisticRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<StorePortraitChartStatisticInfo> list = storeInfoExtensionService.chartStatistic(request);
        StorePortraitChartStatisticViewModel storePortraitChartStatisticViewModel = new StorePortraitChartStatisticViewModel();
        storePortraitChartStatisticViewModel.setStorePortraitType(request.getStorePortraitType());
        Map<Integer,StorePortraitChartStatisticInfo> storePortraitChartStatisticInfoMap = Linq4j.asEnumerable(list).toMap(t->t.getItemValue(),t->t);
        List<StorePortraitChartStatisticInfo> storePortraitChartStatisticInfos = new ArrayList<>();
        EnumUtils.getAllItems(EnumUtils.toEnum(storePortraitChartStatisticViewModel.getStorePortraitType(),StorePortraitEnum.class).getValueEnum()).forEach(item ->{
            StorePortraitChartStatisticInfo storePortraitChartStatisticInfo = storePortraitChartStatisticInfoMap.get(item.getValue());
            if(storePortraitChartStatisticInfo ==null){
                storePortraitChartStatisticInfo = new StorePortraitChartStatisticInfo();
                storePortraitChartStatisticInfo.setStorePortraitType(storePortraitChartStatisticViewModel.getStorePortraitType());
                storePortraitChartStatisticInfo.setItemValue(item.getValue());
            }
            storePortraitChartStatisticInfos.add(storePortraitChartStatisticInfo);
        });
        storePortraitChartStatisticViewModel.setStorePortraitChartStatisticInfos(storePortraitChartStatisticInfos);
        return InvokeResult.SuccessResult(storePortraitChartStatisticViewModel);
    }

    @ApiOperation(value = "门店画像列表", notes = "创建者：毛向军")
    @PostMapping(value = "/storeList")
    public InvokeResult<PageList<StorePortraitListViewModel>> storeList(@Validated @RequestBody StorePortraitListRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Long count=storeInfoExtensionService.getStoreInfoExtensionCount(request);
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Map<String,String> areaMap=areaService.getAllAreaNames();
        List<StorePortraitListViewModel> items = getStorePortraitListViewModels(request, areaMap);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pageResult(items, pageable,count)));
    }

    private List<StorePortraitListViewModel> getStorePortraitListViewModels(StorePortraitListRequest request, Map<String, String> areaMap) {
        return storeInfoExtensionService.getStoreInfoExtensionInfo(request).stream().map(item->{
                item.setProvinceName(getAreaName(item.getProvinceId(),areaMap));
                item.setCityName(getAreaName(item.getCityId(),areaMap));
                item.setRegionName(getAreaName(item.getRegionId(),areaMap));
                if(!org.springframework.util.StringUtils.isEmpty(item.getSurroundingsDistrict())){
                    item.setSurroundingsDistrict(getJointName(StringUtils.stringToInts(item.getSurroundingsDistrict(), Constant.SEPARATOR), SurroundingsDistrictEnum.class));
                }
                item.setCanView(Boolean.TRUE);
                return item;
            }).collect(Collectors.toList());
    }

    @ApiOperation(value = "导出门店画像列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreList",method = RequestMethod.POST)
    public InvokeResult exportStoreList(@Validated  @RequestBody StorePortraitListRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","门店类型","日销售额","装修情况","周边环境","营业面积（m2）","订货比例"},
                        new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","storeFrontType","dailySales","decoration","surroundingsDistrict","commercialArea","orderRatio"},
                        CellStyle.ALIGN_CENTER
                )
        ));
        request.setPageSize(Constant.QUERY_RECORD_SIZE);
        Long count=storeInfoExtensionService.getStoreInfoExtensionCount(request);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        List<StorePortraitListViewModel> list =null;
        Integer index=0;
        do{
            request.setPageIndex(index);
            list=getStorePortraitListViewModels(request, areaMap);
            sheetConfig.setSheetNameTemplate("第%d页");
            myWorkbook.addData(list,sheetConfig);
        }while(count>(index++)*request.getPageSize());
        String filename="门店画像列表"+".xlsx";
        outputStream(filename, response, myWorkbook);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "门店画像列表筛选项", notes = "修改人 毛向军")
    @GetMapping(value = "/storeListFilterItem")
    public InvokeResult<List<StorePortraitListFilterItemViewModel>> storeListFilterItem() {
        List<StorePortraitListFilterItemViewModel> list = new ArrayList<>();
        EnumUtils.getAllItems(StorePortraitEnum.class).forEach(q->{
            StorePortraitListFilterItemViewModel viewModel = new StorePortraitListFilterItemViewModel(q.getValue(),q.getDisplayName(),q.name());
            viewModel.getFilterItemMap().putAll(EnumUtils.toValueMap(q.getValueEnum()));
            list.add(viewModel);
        });
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "统计门店画像是否使用总数", notes = "修改人 毛向军")
    @GetMapping(value = "allStorePortraitUseCountStatistic")
    public InvokeResult<StoreInfoUseCountStatistic> allStorePortraitUseCountStatistic() {
        return InvokeResult.SuccessResult(storeInfoExtensionService.storePortraitUseCountStatistic());
    }

    @ApiOperation(value = "门店周边环境分布", notes = "创建人: 李海峰")
    @GetMapping("/environmentTypeStoreStatistics")
    public InvokeResult<List<EnvironmentTypeStoreStatisticsViewModel>> environmentTypeStoreStatistics() {
        val map = storePortraitService.getEnvironmentTypeStoreStatistics();
        long storeTotal = storeService.getStoreCount(LocalDate.now().toDate());
        val list = Arrays.asList(EnvironmentTypeEnum.values()).stream().map(e -> {
            val environmentTypeStoreStatistics = map.get(e.getValue());
            EnvironmentTypeStoreStatisticsViewModel viewModel = new EnvironmentTypeStoreStatisticsViewModel(e.getDisplayName());
            viewModel.setStoreNum(Objects.isNull(environmentTypeStoreStatistics) ? 0 : environmentTypeStoreStatistics.getStoreNum());
            viewModel.setStoreProportion(NumberFormatUtil.format(viewModel.getStoreNum(), storeTotal, Constant.RATIO_PATTERN));
            return viewModel;
        }).collect(Collectors.toList());
        return InvokeResult.SuccessResult(list);
    }
}
