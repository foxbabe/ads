package com.sztouyun.advertisingsystem.openapi.order;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.order.OrderOperationEnum;
import com.sztouyun.advertisingsystem.model.order.OrderOperationLog;
import com.sztouyun.advertisingsystem.openapi.BaseOpenApiController;
import com.sztouyun.advertisingsystem.service.order.OrderOperationService;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BaseOpenApiViewModel;
import com.sztouyun.advertisingsystem.viewmodel.order.CreateOrderViewModel;
import com.sztouyun.advertisingsystem.viewmodel.order.DeliveryAuditOrderViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "OpenApi接口")
@RestController
@RequestMapping("/open/api/order")
public class OrderController extends BaseOpenApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderOperationService orderOperationService;

    @ApiOperation(value="创建订单",notes = "修改人: 李川")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public InvokeResult createOrder(@Validated @RequestBody CreateOrderViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        orderService.createOrder(viewModel);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value="取消订单",notes = "创建人:毛向军")
    @RequestMapping(value="/cancel",method = RequestMethod.POST)
    public InvokeResult cancelOrder(@Validated @RequestBody BaseOpenApiViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        OrderOperationLog orderOperationLog = ApiBeanUtils.copyProperties(viewModel, OrderOperationLog.class);
        orderOperationLog.setOperation(OrderOperationEnum.Cancel.getValue());
        orderOperationLog.setRemark("合作方取消订单");
        orderOperationService.operate(orderOperationLog);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="提交广告订单投放审核",notes = "创建人:毛向军")
    @RequestMapping(value="/deliveryAudit",method = RequestMethod.POST)
    public InvokeResult deliveryAuditOrder(@Validated @RequestBody DeliveryAuditOrderViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        OrderOperationLog orderOperationLog = ApiBeanUtils.copyProperties(viewModel, OrderOperationLog.class);
        orderOperationLog.setThirdPartMaterialId(viewModel.getMaterialId());
        orderOperationLog.setOperation(OrderOperationEnum.SubmitDeliveryAuditing.getValue());
        orderOperationService.operate(orderOperationLog);
        return InvokeResult.SuccessResult();
    }
}
