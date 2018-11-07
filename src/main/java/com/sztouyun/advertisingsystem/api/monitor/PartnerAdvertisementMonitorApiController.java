package com.sztouyun.advertisingsystem.api.monitor;


import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.monitor.OrderDailyStoreMonitorStatic;
import com.sztouyun.advertisingsystem.model.monitor.PartnerAdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.OrderMaterial;
import com.sztouyun.advertisingsystem.model.order.OrderStatusEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.material.OrderMaterialService;
import com.sztouyun.advertisingsystem.service.monitor.OrderDailyStoreMonitorStaticService;
import com.sztouyun.advertisingsystem.service.monitor.PartnerAdvertisementMonitorStatisticService;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.monitor.*;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.LongFunction1;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(value = "合作方广告监控接口")
@RestController
@RequestMapping("/api/partnerAdvertisementMonitor")
public class PartnerAdvertisementMonitorApiController extends BaseApiController {
    @Autowired
    private PartnerAdvertisementMonitorStatisticService partnerAdvertisementMonitorStatisticService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDailyStoreMonitorStaticService orderDailyStoreMonitorStaticService;
    @Autowired
    private OrderMaterialService orderMaterialService;
    @Autowired
    protected AreaService areaService;

    @ApiOperation(value = "合作方广告监控列表", notes = "创建人: 王伟权")
    @PostMapping(value = "getPartnerAdvertisementMonitors")
    public InvokeResult<PageList<PartnerAdvertisementMonitorStatisticItem>> getPartnerAdvertisementMonitors(@Validated @RequestBody PartnerAdvertisementMonitorStatisticRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        Page<PartnerAdvertisementMonitorStatistic> page = partnerAdvertisementMonitorStatisticService.getPartnerAdvertisementMonitors(request);
        PageList<PartnerAdvertisementMonitorStatisticItem> resultList = ApiBeanUtils.convertToPageList(page, statistic -> copyProperties(statistic,PartnerAdvertisementMonitorStatisticItem.class));
        return InvokeResult.SuccessResult(resultList);
    }

    public <T extends PartnerAdvertisementMonitorStatisticItem > T copyProperties(PartnerAdvertisementMonitorStatistic statistic,Class<T> clazz) {
        T item= ApiBeanUtils.copyProperties(statistic, clazz);
        item.setCode(statistic.getOrderInfo().getCode());
        item.setName(statistic.getOrderInfo().getName());
        item.setPartnerName(getPartnerName(statistic.getPartnerId()));
        item.setTotalStoreCount(statistic.getOrderInfo().getTotalStoreCount());
        item.setAdvertisementPositionTypeName(EnumUtils.toEnum(statistic.getOrderInfo().getOrderMaterials().get(0).getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName());
        item.setEffectiveStartTime(statistic.getOrderInfo().getEffectiveStartTime());
        item.setRatio(NumberFormatUtil.format(statistic.getDisplayTimes().longValue(), statistic.getTotalDisplayTimes().longValue(), Constant.RATIO_PATTERN));
        item.setStatusName(getMonitorStatusEnum(statistic.getOrderInfo().getOrderStatusEnum().getValue()).getDisplayName());
        return item;
    }

    @ApiOperation(value="合作方广告监控详情",notes = "创建人：文丰")
    @GetMapping(value = "{id}")
    public InvokeResult<PartnerAdvertisementMonitorDetailViewModel> getPartnerAdvertisementMonitorDetail(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("监控ID不能为空");
        PartnerAdvertisementMonitorStatistic partnerAdvertisementMonitorStatistic=partnerAdvertisementMonitorStatisticService.findPartnerAdvertisementMonitorStatisticById(id);
        PartnerAdvertisementMonitorDetailViewModel viewModel= copyProperties(partnerAdvertisementMonitorStatistic,PartnerAdvertisementMonitorDetailViewModel.class);
        OrderInfo orderInfo=partnerAdvertisementMonitorStatistic.getOrderInfo();
        setOrderInfo(viewModel,orderInfo);
        viewModel.setId(partnerAdvertisementMonitorStatistic.getId());
        viewModel.setMonitorEndTime(orderInfo.getEffectiveEndTime());
        viewModel.setMonitorStartTime(orderInfo.getEffectiveStartTime());
        viewModel.setPortrait(orderInfo.getPartner().getHeadPortrait());
        if(orderInfo.getEffectiveEndTime()!=null){
            Integer datePeriod=DateUtils.getIntervalDays(orderInfo.getEffectiveStartTime(),orderInfo.getEffectiveEndTime());
            viewModel.setMonitorPeriod(datePeriod);
            viewModel.setEffectiveTotalDays(datePeriod);
        }else{
            viewModel.setEffectiveTotalDays(null);
        }
        return InvokeResult.SuccessResult(viewModel);
    }

    private void setOrderInfo(PartnerAdvertisementMonitorDetailViewModel viewModel,OrderInfo orderInfo){
        OrderStatusEnum orderStatusEnum=EnumUtils.toEnum(orderInfo.getOrderStatus(),OrderStatusEnum.class);
        BeanUtils.copyProperties(orderInfo,viewModel);
        viewModel.setOrderStatusName(orderStatusEnum.getDisplayName());
    }

	 @ApiOperation(value = "合作方广告监控列表统计", notes = "创建人: 王伟权")
    @PostMapping(value = "getPartnerAdvertisementMonitorStatistic")
    public InvokeResult<List<MonitorStatusStatistic>> getPartnerAdvertisementMonitorStatistic(@Validated @RequestBody PartnerAdvertisementMonitorStatisticRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        List<MonitorStatusStatistic> list = partnerAdvertisementMonitorStatisticService.getPartnerAdvertisementMonitorStatistic(request);
        Map<Integer,Long> map =Linq4j.asEnumerable(list).groupBy(c->getMonitorStatusEnum(c.getStatus()).getValue())
                .toMap(m->m.getKey(),k->k.sum((LongFunction1<MonitorStatusStatistic>) MonitorStatusStatistic::getCount));
        List<MonitorStatusStatistic> resultList=new ArrayList<>();
        EnumUtils.toValueMap(MonitorStatusEnum.class).forEach((key,value)->{
            if(key.equals(MonitorStatusEnum.All.getValue())){
                resultList.add(new MonitorStatusStatistic(key,Linq4j.asEnumerable(map.values()).sum((LongFunction1<Long>) aLong -> aLong)));
            }else {
                resultList.add(new MonitorStatusStatistic(key,map.getOrDefault(key,0L)));
            }
        });
        return InvokeResult.SuccessResult(resultList);
    }

    private MonitorStatusEnum getMonitorStatusEnum(Integer orderStatus){
        if(orderStatus.equals(OrderStatusEnum.Delivering.getValue())){
            return MonitorStatusEnum.OnWatching;
        }else if(orderStatus.equals(OrderStatusEnum.Finished.getValue()) || orderStatus.equals(OrderStatusEnum.TakeOff.getValue())){
            return MonitorStatusEnum.Finished;
        }
        return null;
    }

    @ApiOperation(value = "每日广告投放门店状况曲线图(激活，可用状况)",notes = "创建人：文丰")
    @PostMapping(value = "partnerOrderDailyDeliveryStoreStatistic")
    public InvokeResult<List<PartnerOrderDailyStoreMonitorStatisticItem>> getPartnerOrderDailyDeliveryStoreStatistic(@Validated @RequestBody PartnerOrderDailyDeliveryStatisticRequest request, BindingResult result){
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        List<Date> dateList = DateUtils.getDateList(request.getStartDate(), request.getEndDate(), Calendar.DAY_OF_MONTH);
        if(request.getEndDate().getTime()==new LocalDate().toDateTimeAtStartOfDay().toDate().getTime()){
            dateList.remove(dateList.size()-1);
        }
        List<PartnerOrderDailyStoreMonitorStatisticItem> resultList=new ArrayList<>();
        Map<Long,OrderDailyStoreMonitorStatic> orderDailyStoreMonitorStaticMap=Linq4j.asEnumerable(orderDailyStoreMonitorStaticService.getOrderDailyStoreMonitorStaticList(request)).toMap(a->Long.valueOf(a.getDate().getTime()),a->a);
        dateList.stream().forEach(date -> {
            OrderDailyStoreMonitorStatic orderDailyStoreMonitorStatic=orderDailyStoreMonitorStaticMap.get(date.getTime());
            if(orderDailyStoreMonitorStatic!=null){
                PartnerOrderDailyStoreMonitorStatisticItem item = ApiBeanUtils.copyProperties(orderDailyStoreMonitorStatic, PartnerOrderDailyStoreMonitorStatisticItem.class);
                Integer totalStoreCount=orderDailyStoreMonitorStatic.getDeliveryStoreCount();
                item.setUnavailableStoreCount(totalStoreCount - orderDailyStoreMonitorStatic.getAvailableStoreCount());
                item.setInactiveStoreCount(totalStoreCount - orderDailyStoreMonitorStatic.getActiveStoreCount());
                item.setTotalStoreCount(totalStoreCount);
                resultList.add(item);
            }else{
                resultList.add(new PartnerOrderDailyStoreMonitorStatisticItem(date));
            }
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value="合作方广告监控详情曲线图（投放状况）",notes = "创建人：文丰")
    @PostMapping(value = "partnerOrderDetailDailyDeliveryStatistic")
    public InvokeResult<PartnerOrderDetailDailyDeliveryStatisticViewModel> getPartnerOrderDetailDailyDeliveryStatistic(@Validated @RequestBody PartnerOrderDailyDeliveryStatisticRequest request, BindingResult result){
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        OrderMaterial orderMaterial=orderMaterialService.findOrderMaterialByOrderId(request.getOrderId());
        List<Date> dateList = DateUtils.getDateList(request.getStartDate(), request.getEndDate(), Calendar.DAY_OF_MONTH);
        Map<Long,OrderDailyStoreMonitorStatic> orderDailyStoreMonitorStaticMap=Linq4j.asEnumerable(orderDailyStoreMonitorStaticService.getOrderDailyStoreMonitorStaticList(request)).toMap(a->Long.valueOf(a.getDate().getTime()),a->a);
        List<OrderDailyStoreMonitorStatic> orderDailyStoreMonitorStaticList=orderDailyStoreMonitorStaticService.getOrderDailyStoreMonitorStaticList(request);
         Integer totalDisplayTimes=Linq4j.asEnumerable(orderDailyStoreMonitorStaticList).select(a->a.getDisplayTimes()).sum(Integer::intValue);
        List<PartnerOrderDetailDailyDeliveryStatisticItem> resultList=new ArrayList<>();
        dateList.stream().forEach(date -> {
            OrderDailyStoreMonitorStatic orderDailyStoreMonitorStatic=orderDailyStoreMonitorStaticMap.get(date.getTime());
            if(orderDailyStoreMonitorStatic!=null){
                PartnerOrderDetailDailyDeliveryStatisticItem item=ApiBeanUtils.copyProperties(orderDailyStoreMonitorStatic,PartnerOrderDetailDailyDeliveryStatisticItem.class);
                item.setTotalStoreCount(orderDailyStoreMonitorStatic.getDeliveryStoreCount());
                item.setTotalDisplayTimes(orderDailyStoreMonitorStatic.getDeliveryStoreCount()*orderMaterial.getDisplayTimes());
                item.setDisplayStandard(orderMaterial.getDisplayTimes());
                resultList.add(item);
            }else{
                resultList.add(new PartnerOrderDetailDailyDeliveryStatisticItem(date));
            }
        });
        PartnerOrderDetailDailyDeliveryStatisticViewModel viewModel= new PartnerOrderDetailDailyDeliveryStatisticViewModel();
        viewModel.setList(resultList);
        viewModel.setTotalDisplayTimes(totalDisplayTimes);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "合作方监控详情门店列表", notes = "创建人: 王伟权")
    @PostMapping(value = "getPartnerOrderStoreMonitorItems")
    public InvokeResult<PageList<PartnerOrderStoreMonitorItem>> getPartnerOrderStoreMonitorItems(@Validated @RequestBody PartnerOrderStoreMonitorRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        return InvokeResult.SuccessResult(getPartnerOrderStoreMonitors(request));
    }

    private PageList<PartnerOrderStoreMonitorItem> getPartnerOrderStoreMonitors(PartnerOrderStoreMonitorRequest request) {
        OrderInfo orderInfo = orderService.getOrder(request.getOrderId());
        setValidEndTime(request, orderInfo.getEffectiveEndTime());
        request.setEffectiveStartTime(new LocalDate(orderInfo.getEffectiveStartTime()).toDate());
        request.setEffectiveEndTime(new LocalDate(orderInfo.getEffectiveEndTime()).toDate());
        Page<PartnerOrderStoreMonitorItem> page = partnerAdvertisementMonitorStatisticService.getPartnerOrderStoreMonitorItems(request);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(page, item -> {
            item.setProvinceName(getAreaName(item.getProvinceId(),areaMap));
            item.setCityName(getAreaName(item.getCityId(),areaMap));
            item.setRegionName(getAreaName(item.getRegionId(),areaMap));
            if (item.getUpdatedTime() == null) {
                item.setUpdatedTime(orderInfo.getEffectiveStartTime());
            }
            if(item.getDisplayTimes() > 0) {
                item.setIsDisplay(Boolean.TRUE);
            }
            return item;
        });
    }

    private void setValidEndTime(PartnerOrderStoreMonitorRequest request, Date effectiveEndTime) {
        if(effectiveEndTime == null) {// 订单还没有结束投放
            if(request.getEndTime() == null) {//如果没有选择结束时间, 因为storeDailyStatistic表存的是前一天的数据, 所以取前一天
                request.setEndTime(LocalDate.now().toDate());
            } else {
                if(new LocalDate(request.getEndTime()).isAfter(LocalDate.now()) || new LocalDate(request.getEndTime()).isEqual(LocalDate.now())) {
                    request.setEndTime(LocalDate.now().toDate());
                } else {
                    request.setEndTime(new LocalDate(request.getEndTime()).toDate());
                }
            }
        } else {// 订单已经结束投放
            if(request.getEndTime() == null) {
                request.setEndTime(new LocalDate(effectiveEndTime).toDate());
            } else {
                if(new LocalDate(request.getEndTime()).isAfter(LocalDate.now()) || new LocalDate(request.getEndTime()).isEqual(LocalDate.now())) {
                    request.setEndTime(new LocalDate(effectiveEndTime).toDate());
                } else {
                    request.setEndTime(new LocalDate(request.getEndTime()).toDate());
                }
            }
        }
    }

    @ApiOperation(value="合作方监控详情门店列表导出",notes = "创建人:王伟权")
    @RequestMapping(value = "/exportPartnerOrderStoreMonitorItems", method = RequestMethod.POST)
    private InvokeResult exportPartnerOrderStoreMonitorItems(@Validated @RequestBody PartnerOrderStoreMonitorRequest request, HttpServletResponse response, BindingResult result) {
        if(result.hasErrors())
            ValidateFailResult(result);
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<PartnerOrderStoreMonitorItem> pageList;
        Workbook wb=new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID","是否可用","是否激活", "广告是否展示", "已展示次数", "更新时间"},
                new String[]{"storeNo","storeName","provinceName","cityName","regionName","storeAddress","deviceId","available","active", "isDisplay", "displayTimes", "updatedTime"},
                CellStyle.ALIGN_CENTER
        );
        Integer index=0;
        Integer totalPage=0;
        String filename="合作方监控门店列表.xlsx";
        do{
            request.setPageIndex(index);
            pageList=getPartnerOrderStoreMonitors(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header).addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                put("updatedTime",TimeFormatEnum.Default);
            }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while( index<totalPage);
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "获取合作方监控门店有效地区", notes = "创建人:王伟权")
    @GetMapping("/{orderId}/areas")
    public InvokeResult<List<AreaViewModel>> getValidAreaInPartnerStoreMonitor(@PathVariable String orderId) {
        List<Area> areas = partnerAdvertisementMonitorStatisticService.getValidAreaInPartnerStoreMonitor(orderId);
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

}