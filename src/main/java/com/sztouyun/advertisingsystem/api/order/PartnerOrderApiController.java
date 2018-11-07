package com.sztouyun.advertisingsystem.api.order;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.order.*;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.order.OrderOperationService;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.order.*;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.PartnerOrderMaterialInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api("合作方订单接口")
@RestController
@RequestMapping("/api/partnerOrder")
public class PartnerOrderApiController extends BaseApiController {

    @Autowired
    private OrderOperationService orderOperationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;

    @ApiOperation(value="审核订单",notes = "创建人:毛向军")
    @PostMapping(value="/audit")
    public InvokeResult auditOrder(@Validated @RequestBody AuditOrderRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        OrderOperationLog orderOperationLog = ApiBeanUtils.copyProperties(request, OrderOperationLog.class);
        orderOperationLog.setOperation(OrderOperationEnum.DeliveryAuditing.getValue());
        orderOperationService.operate(orderOperationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="下架订单",notes = "创建人:毛向军")
    @RequestMapping(value="/takeOff",method = RequestMethod.POST)
    public InvokeResult takeOffOrder(@Validated @RequestBody AuditOrderRequest viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        OrderOperationLog orderOperationLog = ApiBeanUtils.copyProperties(viewModel, OrderOperationLog.class);
        orderOperationLog.setOperation(OrderOperationEnum.TakeOff.getValue());
        orderOperationService.operate(orderOperationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="订单详情日投放门店流水列表接口",notes = "创建人:文丰")
    @RequestMapping(value="/dailyDeliveryStoreStatistic",method = RequestMethod.POST)
    public InvokeResult<PageList<DailyStoreCountItem>> getDeliveryStoreStatistic(@Validated @RequestBody DailyDeliveryStoreStatisticRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        var page=orderService.getDailyStoreCount(request);
        PageList<DailyStoreCountItem> pageList=ApiBeanUtils.convertToPageList(page,a->a);
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "广告订单列表", notes = "创建人:杨浩")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public InvokeResult<PageList<OrderListItemViewModel>> queryOrderList(@Validated @RequestBody OrderBasePageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<OrderInfo> pages = orderService.queryOrderList(viewModel);
        PageList<OrderListItemViewModel> pageList = ApiBeanUtils.convertToPageList(pages,
                orderInfo -> {
                    OrderListItemViewModel item = ApiBeanUtils.copyProperties(orderInfo,OrderListItemViewModel.class);
                    item.setPartnerName(getPartnerName(item.getPartnerId()));
                    item.setAdvertisementPositionType(orderInfo.getOrderMaterials().get(0).getAdvertisementPositionType());
                    AdvertisementPositionTypeEnum advertisementPositionTypeEnum = EnumUtils.toEnum(orderInfo.getOrderMaterials().get(0).getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class);
                    item.setAdvertisementPositionTypeDesc(advertisementPositionTypeEnum.getDisplayName());
                    OrderStatusEnum orderStatusEnum = EnumUtils
                            .toEnum(item.getOrderStatus(), OrderStatusEnum.class);
                    item.setOrderStatusDesc(orderStatusEnum.getDisplayName());
                    return item;
                });

        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "广告订单统计信息", notes = "创建人:杨浩")
    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public InvokeResult<List<OrderStatisticsViewModel>> getOrderStatistics(@Validated @RequestBody OrderBasePageInfoViewModel viewModel,
                                                                           BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        List<OrderStatisticsViewModel> resultList = new ArrayList<>();
        List<OrderStatisticsViewModel> resultDb = orderService.getOrderStatistics(viewModel);
        Map<Integer,Long> itemsMap = resultDb.stream().collect(Collectors.toMap(OrderStatisticsViewModel::getStatus,OrderStatisticsViewModel::getCount));
        resultList.add(new OrderStatisticsViewModel(null, itemsMap.values().stream().mapToLong(r -> r).sum()));
        for(OrderStatusEnum orderStatusEnum:OrderStatusEnum.values()){
            resultList.add(new OrderStatisticsViewModel(orderStatusEnum.getValue(), itemsMap.get(orderStatusEnum.getValue())));
        }
        return InvokeResult.SuccessResult(resultList);

    }

    @ApiOperation(value = "查询订单详情", notes = "创建人: 毛向军")
    @GetMapping(value = "/{id}")
    public InvokeResult<OrderItemViewModel> getOrderDetails(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            throw  new BusinessException("订单ID不能为空");
        List<OrderMaterial> orderDetails = orderService.getOrderDetails(id);
        OrderItemViewModel orderItemViewModel = new OrderItemViewModel();
        List<PartnerOrderMaterialInfo> partnerOrderMaterialInfos = Linq4j.asEnumerable(orderDetails).select(q -> {
            BeanUtils.copyProperties(q.getOrderInfo(), orderItemViewModel);
            PartnerOrderMaterialInfo partnerOrderMaterialInfo = ApiBeanUtils.copyProperties(q, PartnerOrderMaterialInfo.class);
            PartnerMaterial partnerMaterial = q.getPartnerMaterial();
            if(partnerMaterial!=null) {
                BeanUtils.copyProperties(partnerMaterial, partnerOrderMaterialInfo);
                partnerOrderMaterialInfo.setOrderType(partnerMaterial.getMaterialType());
                partnerOrderMaterialInfo.setOrderTypeDesc(EnumUtils.getDisplayName(partnerMaterial.getMaterialType(), MaterialTypeEnum.class));
            }
            partnerOrderMaterialInfo.setAdvertisementPositionTypeDesc(EnumUtils.getDisplayName(partnerOrderMaterialInfo.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class));
            orderItemViewModel.setTotalDays(DateUtils.formateYmd(q.getOrderInfo().getTotalDays()));
            orderItemViewModel.setEffectiveTotalDays(DateUtils.formateYmd(q.getOrderInfo().getEffectiveTotalDays()));
            return partnerOrderMaterialInfo;
        }).toList();
        orderItemViewModel.setPartnerOrderMaterialInfos(partnerOrderMaterialInfos);
        orderItemViewModel.setOrderStatusDesc(EnumUtils.getDisplayName(orderItemViewModel.getOrderStatus(),OrderStatusEnum.class));
        CooperationPartner cooperationPartner = cooperationPartnerService.findCooperationPartnerById(orderItemViewModel.getPartnerId());
        if(cooperationPartner!=null){
            orderItemViewModel.setHeadPortrait(cooperationPartner.getHeadPortrait());
            orderItemViewModel.setPartnerName(cooperationPartner.getName());
        }
        return InvokeResult.SuccessResult(orderItemViewModel);
    }

    @ApiOperation(value = "查询订单操作记录",notes = "文丰")
    @PostMapping(value = "operationLogs")
    public InvokeResult<PageList<OrderOperationViewModel>> getOperationLogs(@Validated @RequestBody OrderOperationLogsRequest request,BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        var pages=orderService.getOrderOperationLog(request);
        List<Integer> nonBlankOperators=Arrays.asList(OrderOperationStatusEnum.RejectAuditing.getOperation(),OrderOperationStatusEnum.PendingDelivery.getOperation(),OrderOperationStatusEnum.TakeOff.getOperation());
        PageList<OrderOperationViewModel> pageList=ApiBeanUtils.convertToPageList(pages,orderOperationLog -> {
            OrderOperationViewModel item=ApiBeanUtils.copyProperties(orderOperationLog,OrderOperationViewModel.class);
            item.setOperationStatus(orderOperationLog.getOperation(),orderOperationLog.isSuccessed());
            item.setOperateTime(orderOperationLog.getCreatedTime());
            if(nonBlankOperators.contains(orderOperationLog.getOperation())){
                item.setOperator(getUserNickname(orderOperationLog.getCreatorId()));
            }else{
                item.setOperator("");
            }
            return item;
        });
        return InvokeResult.SuccessResult(pageList);
    }

}
