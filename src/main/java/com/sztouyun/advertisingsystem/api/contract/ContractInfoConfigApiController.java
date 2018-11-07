package com.sztouyun.advertisingsystem.api.contract;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.contract.ContractSecondPartyInfoConfig;
import com.sztouyun.advertisingsystem.service.contract.ContractSecondPartyInfoConfigService;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractSecondPartyInfoConfigViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value = "合同信息配置")
@RestController
@RequestMapping(value = "/api/contractInfoConfig/")
public class ContractInfoConfigApiController extends BaseApiController {

    @Autowired
    private ContractSecondPartyInfoConfigService contractSecondPartyInfoConfigService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "创建或者更新乙方合同信息配置", notes = "创建人: 王伟权")
    @PostMapping(value = "updateSecondPartyContractInfo")
    public InvokeResult<String> updateSecondPartyContractInfoConfig(@Validated @RequestBody ContractSecondPartyInfoConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        ContractSecondPartyInfoConfig contractSecondPartyInfoConfig = contractSecondPartyInfoConfigService.getContractSecondPartyInfoConfig();
        if (null == contractSecondPartyInfoConfig) {
            contractSecondPartyInfoConfig = new ContractSecondPartyInfoConfig();
            contractSecondPartyInfoConfig.setCreatorId(getUser().getId());
            contractSecondPartyInfoConfig.setCreatedTime(new Date());
        }
        BeanUtils.copyProperties(viewModel, contractSecondPartyInfoConfig);
        return InvokeResult.SuccessResult(contractSecondPartyInfoConfigService.updateContractSecondPartyInfoConfig(contractSecondPartyInfoConfig));
    }


    @ApiOperation(value = "查询乙方合同配置信息", notes = "创建人: 王伟权")
    @GetMapping(value = "getSecondPartyContractInfo")
    public InvokeResult<ContractSecondPartyInfoConfigViewModel> getSecondPartyContractInfoConfig() {
        ContractSecondPartyInfoConfig contractSecondPartyInfoConfig = contractSecondPartyInfoConfigService.getContractSecondPartyInfoConfig();
        if (null == contractSecondPartyInfoConfig)
            return InvokeResult.SuccessResult();
        ContractSecondPartyInfoConfigViewModel viewModel = new ContractSecondPartyInfoConfigViewModel();
        BeanUtils.copyProperties(contractSecondPartyInfoConfig, viewModel);
        return InvokeResult.SuccessResult(viewModel);
    }
}
