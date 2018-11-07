package com.sztouyun.advertisingsystem.openapi.store;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.openapi.BaseOpenApiController;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.viewmodel.store.openapi.QueryUnavailableStoreRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "OpenApi接口")
@RestController
@RequestMapping("/open/api/store")
public class StoreController extends BaseOpenApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;

    @ApiOperation(value="获取哪些门店的哪些天的广告位满了(key为0，表示非法的门店ID)",notes = "修改人: 李川")
    @RequestMapping(value="/query/daily",method = RequestMethod.POST)
    public InvokeResult<Map<Long,List<String>>> getUnavailableStoreDailyUsage(@Validated @RequestBody QueryUnavailableStoreRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<String> storeIds =request.getStoreIds();
        List<String> availableStoreIds = storeService.getAvailableStoreIds(storeIds);
        var dailyMap = orderService.getUnavailableStoreDailyUsage(request.getAdvertisementPositionCategory(),
                date->date.between(request.getStartTime(),request.getEntTime()),availableStoreIds);
        storeIds.removeAll(availableStoreIds);
        dailyMap.put(0L,storeIds);
        return InvokeResult.SuccessResult(dailyMap);
    }
}
