package com.sztouyun.advertisingsystem.api.advertisement;


import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.AdvertisementDeliveryStrategy;
import com.sztouyun.advertisingsystem.model.system.SystemOperationEnum;
import com.sztouyun.advertisingsystem.model.system.SystemOperationLog;
import com.sztouyun.advertisingsystem.model.system.SystemParamConfig;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementDeliveryStrategyService;
import com.sztouyun.advertisingsystem.service.system.SystemOperationLogService;
import com.sztouyun.advertisingsystem.service.system.SystemParamConfigService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementDeliveryStrategyViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.TerminalAndAdvertisementPositionInfo;
import com.sztouyun.advertisingsystem.viewmodel.system.TerminalAndAdvertisementPositionRequest;
import com.sztouyun.advertisingsystem.viewmodel.system.TerminalAndAdvertisementPositionViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("广告投放策略")
@RestController
@RequestMapping("/api/advertisementDeliveryStrategy")
public class AdvertisementDeliveryStrategyApiController extends BaseApiController{
    @Autowired
    private AdvertisementDeliveryStrategyService advertisementDeliveryStrategyService;

    @Autowired
    private SystemParamConfigService systemParamConfigService;
    @Autowired
    private SystemOperationLogService systemOperationLogService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "获取展示次数", notes = "创建人: 王伟权")
    @GetMapping(value = "getDeliveryStrategy")
    public InvokeResult<AdvertisementDeliveryStrategyViewModel> getDeliveryStrategy() {
        AdvertisementDeliveryStrategy advertisementDeliveryStrategy = advertisementDeliveryStrategyService.getAdvertisementDeliveryStrategy();
        AdvertisementDeliveryStrategyViewModel advertisementDeliveryStrategyViewModel = new AdvertisementDeliveryStrategyViewModel();
        BeanUtils.copyProperties(advertisementDeliveryStrategy, advertisementDeliveryStrategyViewModel);
        advertisementDeliveryStrategyViewModel.setUpdater(getUserNickname(advertisementDeliveryStrategy.getUpdaterId()));

        return InvokeResult.SuccessResult(advertisementDeliveryStrategyViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新展示次数", notes = "创建人: 王伟权")
    @GetMapping(value = "updateDeliveryStrategy")
    public InvokeResult<String> updateDeliveryStrategy() {
        AdvertisementDeliveryStrategy advertisementDeliveryStrategy = advertisementDeliveryStrategyService.getAdvertisementDeliveryStrategy();
        if (advertisementDeliveryStrategy == null) {
            advertisementDeliveryStrategy = new AdvertisementDeliveryStrategy();
            advertisementDeliveryStrategy.setControlDisplayTimes(Boolean.TRUE);
        }
        advertisementDeliveryStrategy.setControlDisplayTimes(!advertisementDeliveryStrategy.getControlDisplayTimes());
        return InvokeResult.SuccessResult(advertisementDeliveryStrategyService.updateDeliveryStrategy(advertisementDeliveryStrategy));
    }

    @ApiOperation(value = "获取终端类型和广告位配置", notes = "创建人: 毛向军")
    @GetMapping(value = "getTerminalTypeAndAdvertisementPositionType")
    public InvokeResult<TerminalAndAdvertisementPositionViewModel> getTerminalTypeAndAdvertisementPositionType() {
        List<TerminalAndAdvertisementPositionInfo> list = systemParamConfigService.getTerminalAndAdvertisementPositionInfos();
        TerminalAndAdvertisementPositionViewModel viewModel = new TerminalAndAdvertisementPositionViewModel();
        SystemOperationLog systemOperationLog = systemOperationLogService.findOne(SystemOperationEnum.TerminalAndAdvertisementPosition.getValue());
        viewModel.setList(list);
        viewModel.setUpdatedTime(systemOperationLog.getUpdatedTime());
        viewModel.setUpdater(getUserNickname(systemOperationLog.getCreatorId()));

        return InvokeResult.SuccessResult(viewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新终端类型和广告位配置", notes = "创建人: 毛向军")
    @PostMapping(value = "updateTerminalTypeAndAdvertisementPositionType")
    public InvokeResult updateTerminalTypeAndAdvertisementPositionType(@Validated @RequestBody TerminalAndAdvertisementPositionRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        systemParamConfigService.updateSystemParamConfigService(request.getIds());
        return InvokeResult.SuccessResult();
    }



}
