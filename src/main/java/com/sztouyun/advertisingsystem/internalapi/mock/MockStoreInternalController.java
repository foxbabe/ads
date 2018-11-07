package com.sztouyun.advertisingsystem.internalapi.mock;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.internalapi.BaseInternalApiController;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.viewmodel.internal.store.UpdateStoreInfoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"local","dev","test","stage"})
@Api(value = "模拟门店接口")
@RestController
@RequestMapping("/internal/api/mock/store")
public class MockStoreInternalController extends BaseInternalApiController {
    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "模拟替换门店编号")
    @PostMapping(value = "/replace")
    public InvokeResult replaceStore(@Validated  @RequestBody UpdateStoreInfoRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        storeService.replaceStore(request);
        return InvokeResult.SuccessResult();
    }
}
