package com.sztouyun.advertisingsystem.api.coorperationPartner;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerConfigRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerConfigUpdateRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerConfigViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "合作方投放策略接口")
@RestController
@RequestMapping("/api/cooperationPartnerConfig")
public class CooperationPartnerConfigController extends BaseApiController {

    @Autowired
    private CooperationPartnerService cooperationPartnerService;


    @ApiOperation(value = "查询合作方投放策略列表", notes = "创建人:毛向军")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<CooperationPartnerConfigViewModel>> queryCooperationPartnerConfigList(@Validated @RequestBody CooperationPartnerConfigRequest request,
                                                                                             BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<CooperationPartnerConfigViewModel> pages = cooperationPartnerService.queryCooperationPartnerConfigList(request);

        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pages,viewModel->{
            viewModel.setCooperationPatternName(EnumUtils.getDisplayName(viewModel.getCooperationPattern(), CooperationPatternEnum.class));
            return viewModel;
        }));
    }

    @ApiOperation(value = "更新合作方优先级", notes = "创建人:毛向军")
    @RequestMapping(value = "updatePriority", method = RequestMethod.POST)
    public InvokeResult updatePriority(@Validated @RequestBody CooperationPartnerConfigUpdateRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        cooperationPartnerService.updatePriority(request.getCheckedId(),request.getReplacedPriority());
        return InvokeResult.SuccessResult();
    }



}
