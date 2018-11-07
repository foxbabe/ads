package com.sztouyun.advertisingsystem.api.contract;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.contract.ContractReceiptInfo;
import com.sztouyun.advertisingsystem.service.contract.ContractReceiptInfoService;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractReceiptInfoViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value = "合同收款信息配置")
@RestController
@RequestMapping("/api/contractReceiptInfo")
public class ContractReceiptInfoApiController extends BaseApiController {
    @Autowired
    private ContractReceiptInfoService contractReceiptInfoService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "编辑合同收款信息", notes = "创建人: 张瑞兵")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public InvokeResult updateReceiptInfo(@Validated @RequestBody ContractReceiptInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        ContractReceiptInfo receiptInfo = contractReceiptInfoService.getContractReceiptInfo();
        if (null == receiptInfo) {
            receiptInfo = new ContractReceiptInfo();
            receiptInfo.setCreatorId(getUser().getId());
            receiptInfo.setCreatedTime(new Date());
        }
        BeanUtils.copyProperties(viewModel, receiptInfo);
        contractReceiptInfoService.updateReceipt(receiptInfo);
        return InvokeResult.SuccessResult();
    }
    
    @ApiOperation(value = "查询合同收款信息", notes = "创建人: 张瑞兵")
    @PostMapping
    public InvokeResult<ContractReceiptInfoViewModel> queryReceiptInfo() {
        ContractReceiptInfo receiptInfo = contractReceiptInfoService.getContractReceiptInfo();
        if (null == receiptInfo)
            return InvokeResult.SuccessResult();
        ContractReceiptInfoViewModel viewModel = new ContractReceiptInfoViewModel();
        BeanUtils.copyProperties(receiptInfo, viewModel);
        return InvokeResult.SuccessResult(viewModel);
    }

}
