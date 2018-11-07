package com.sztouyun.advertisingsystem.internalapi.store;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.internalapi.BaseInternalApiController;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.StoreDailyProfitRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.StoreDailyProfitViewModel;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.UpdateStoreInfoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wenfeng on 2018/3/12.
 */
@Api(value = "门店接口")
@RestController
@RequestMapping("/internal/api/store")
public class StoreInternalController extends BaseInternalApiController {
    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "门店迁移接口")
    @PostMapping(value = "/move")
    public InvokeResult moveStore(@Validated  @RequestBody UpdateStoreInfoRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        storeService.moveStore(request);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "周期端内的每日门店分成")
    @PostMapping(value = "/storeDailyProfit")
    public InvokeResult<List<StoreDailyProfitViewModel>> storeDailyProfitList(@Validated  @RequestBody StoreDailyProfitRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        String storeId=storeService.getStoreIdByStoreNo(request.getStoreNo());
        if(StringUtils.isEmpty(storeId))
            return InvokeResult.Fail("门店不存在");
        return InvokeResult.SuccessResult(storeService.storeDailyProfitList(storeId,request.getStartTime(),request.getEndTime()));
    }

}
