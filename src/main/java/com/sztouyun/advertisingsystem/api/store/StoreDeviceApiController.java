package com.sztouyun.advertisingsystem.api.store;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.store.StoreDeviceService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.DayRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkPeriodIntervalChartViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkProportionRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNetworkProportionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.storeDevice.OpeningTimeTrendRequest;
import com.sztouyun.advertisingsystem.viewmodel.storeDevice.StoreDeviceOpeningDurationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2018/8/8.
 */
@Api(value = "门店设备统计接口")
@RestController
@RequestMapping("/api/storeDevice")
public class StoreDeviceApiController  extends BaseApiController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreDeviceService storeDeviceService;

    @ApiOperation(value = "门店开关机时长分布", notes = "创建者：文丰")
    @PostMapping(value = "openingDurationStatistic")
    public InvokeResult<List<StoreDeviceOpeningDurationInfo>> openingDurationStatistic(@Validated @RequestBody DayRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(storeDeviceService.getStoreDeviceOpeningDurationInfo(request.getDate()));
    }

    @ApiOperation(value = "门店每日开机时间趋势", notes = "创建者：文丰")
    @PostMapping(value = "dailyOpeningTimeTrend")
    public InvokeResult<List<StoreDeviceOpeningDurationInfo>> getDailyOpeningTimeTrend(@Validated @RequestBody OpeningTimeTrendRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(storeDeviceService.getDailyOpeningTimeTrend(request));
    }

    @ApiOperation(value = "门店网络占比", notes = "创建人: 李海峰")
    @PostMapping("/storeNetworkProportion")
    public InvokeResult<StoreNetworkProportionViewModel> storeNetworkProportion(@Valid @RequestBody StoreNetworkProportionRequest request, BindingResult result) {
        if (result.hasErrors()) return ValidateFailResult(result);
        if (request.getDate().after(new Date())) throw new BusinessException("日期不能大于当前日期");

        long storeCount = storeService.getStoreCount(request.getDate());
        long onStoreTotal = storeDeviceService.getHasNetworkStoreTotal(request.getDate());
        storeCount = storeCount - onStoreTotal < 0 ? onStoreTotal : storeCount;// 对门店总数小于有网络门店数情况做特殊处理

        StoreNetworkProportionViewModel viewModel = new StoreNetworkProportionViewModel();
        viewModel.setOnStoreTotal(onStoreTotal);
        viewModel.setOffStoreTotal(storeCount - onStoreTotal);
        viewModel.setOnStoreProportion(calcProportion(viewModel.getOnStoreTotal(), storeCount));
        viewModel.setOffStoreProportion(calcProportion(viewModel.getOffStoreTotal(), storeCount));
        return InvokeResult.SuccessResult(viewModel);
    }

    /**
     * 计算占比
     */
    private String calcProportion(Long numerator, Long denominator) {
        return NumberFormatUtil.format(numerator, denominator, Constant.RATIO_PATTERN);
    }

    @ApiOperation(value = "门店网络时段分布")
    @PostMapping(value = "/storeNetworkPeriodIntervalChart")
    public InvokeResult<List<StoreNetworkPeriodIntervalChartViewModel>> storeNetworkPeriodIntervalChart(@Validated @RequestBody StoreNetworkPeriodIntervalChartRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(storeDeviceService.storeNetworkPeriodIntervalChart(request));
    }
}
