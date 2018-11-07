package com.sztouyun.advertisingsystem.api.contract;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.service.contract.ContractTemplateService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractTemplateViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "合同模板接口")
@RestController
@RequestMapping("/api/contractTemplate")
public class ContractTemplateApiController extends BaseApiController {

    @Autowired
    private ContractTemplateService contractTemplateService;

    @ApiOperation(value = "获取最新合同模板信息", notes = "创建人: 王伟权")
    @GetMapping(value = "getAllTypeTemplates")
    public InvokeResult<List<ContractTemplateViewModel>> getAllTypeTemplates() {
        List<ContractTemplateViewModel> resultList = new ArrayList<>();
        contractTemplateService.getAllTypeTemplates().stream().forEach(contractTemplate -> {
            resultList.add(ApiBeanUtils.copyProperties(contractTemplate, ContractTemplateViewModel.class));
        });
        return InvokeResult.SuccessResult(resultList);
    }


}
