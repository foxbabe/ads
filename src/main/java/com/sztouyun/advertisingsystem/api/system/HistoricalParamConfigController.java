package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.sztouyun.advertisingsystem.service.system.SystemOperationLogService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.system.GroupConfigRequest;
import com.sztouyun.advertisingsystem.viewmodel.system.HistoricalParamConfigInfo;
import com.sztouyun.advertisingsystem.viewmodel.system.HistoricalParamConfigViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "带历史记录的参数配置")
@RestController
@RequestMapping("/api/historicalParam")
public class HistoricalParamConfigController extends BaseApiController {

    @Autowired
    private HistoricalParamConfigService historicalParamConfigService;
    @Autowired
    private SystemOperationLogService systemOperationLogService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "创建配置", notes = "创建人: 李川")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InvokeResult createHistoricalParamConfig(@Validated @RequestBody HistoricalParamConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        HistoricalParamConfig historicalParamConfig = new HistoricalParamConfig();
        BeanUtils.copyProperties(viewModel, historicalParamConfig);
        historicalParamConfigService.createHistoricalParamConfig(historicalParamConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "批量获取一组配置", notes = "创建人: 李川")
    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public InvokeResult<HistoricalParamConfigInfo> getProfitShareConfig(@Validated @ModelAttribute GroupConfigRequest request) {
        HistoricalParamConfigGroupEnum historicalParamConfigGroupEnum = EnumUtils.toEnum(request.getGroup(),HistoricalParamConfigGroupEnum.class);
        if(historicalParamConfigGroupEnum ==null)
            throw new BusinessException("配置类型不存在！");

        List<HistoricalParamConfig> HistoricalParamConfigs = historicalParamConfigService.getHistoricalParamConfigGroup(historicalParamConfigGroupEnum,new Date(),request.getObjectId());
        List<HistoricalParamConfigViewModel> resultList =  HistoricalParamConfigs.stream().map(r -> new HistoricalParamConfigViewModel(r.getId(),r.getType(), r.getComparisonType(), r.getUnit(), r.getValue(),r.getObjectId())).collect(Collectors.toList());
        if(historicalParamConfigGroupEnum.equals(HistoricalParamConfigGroupEnum.Task)){
            SystemOperationLog systemOperationLog = systemOperationLogService.findOne(SystemOperationEnum.TaskConfig.getValue());
            if(systemOperationLog==null)
                return InvokeResult.SuccessResult(new HistoricalParamConfigInfo(null,null,resultList));
            return InvokeResult.SuccessResult(new HistoricalParamConfigInfo(systemOperationLog.getUpdatedTime(),getUserNickname(systemOperationLog.getCreatorId()),resultList));
        }
        return InvokeResult.SuccessResult(new HistoricalParamConfigInfo(null,null,resultList));
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "批量创建配置", notes = "创建人: 李川")
    @RequestMapping(value = "/batch/create", method = RequestMethod.POST)
    public InvokeResult createProfitShareConfigs(@Validated @RequestBody List<HistoricalParamConfigViewModel> viewModels, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        viewModels.forEach(r ->{
            HistoricalParamConfig historicalParamConfig = new HistoricalParamConfig();
            BeanUtils.copyProperties(r, historicalParamConfig);
            historicalParamConfigService.createHistoricalParamConfig(historicalParamConfig);
        });
       return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除配置", notes = "创建人: 文丰")
    @PostMapping("delete/{id}")
    public InvokeResult deleteConfig(@PathVariable("id") String id){
        historicalParamConfigService.delete(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新配置", notes = "创建人: 文丰")
    @PostMapping("update")
    public InvokeResult updateHistoricalParamConfig(@Validated @RequestBody HistoricalParamConfigViewModel viewModel, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        HistoricalParamConfig historicalParamConfig = new HistoricalParamConfig();
        BeanUtils.copyProperties(viewModel, historicalParamConfig);
        if(org.springframework.util.StringUtils.isEmpty(viewModel.getConfigId())){
            historicalParamConfigService.createHistoricalParamConfig(historicalParamConfig);
        }else{
            historicalParamConfig.setId(viewModel.getConfigId());
            historicalParamConfigService.updateHistoricalParamConfig(historicalParamConfig);
        }
        return InvokeResult.SuccessResult();
    }

}
