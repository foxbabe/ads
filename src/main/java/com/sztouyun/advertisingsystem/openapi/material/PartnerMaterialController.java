package com.sztouyun.advertisingsystem.openapi.material;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.openapi.BaseOpenApiController;
import com.sztouyun.advertisingsystem.service.material.PartnerMaterialService;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.CreateMaterialRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "合作方素材管理接口")
@RestController
@RequestMapping("/open/api/partnerMaterial")
public class PartnerMaterialController extends BaseOpenApiController {
    @Autowired
    private PartnerMaterialService partnerMaterialService;
    @ApiOperation(value="提交素材",notes = "创建人：文丰")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public InvokeResult createMaterial(@Validated @RequestBody CreateMaterialRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        partnerMaterialService.create(request);
        return InvokeResult.SuccessResult();
    }

}
