package com.sztouyun.advertisingsystem.openapi.partner;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "第三方广告管理")
@RestController
@RequestMapping("/open/api/partner/partnerAdvertisement")
public class PartnerAdvertisementOpenController extends BaseApiController {

    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;

    @ApiOperation(value = "保存第三方广告数据", notes = "创建人: 王伟权")
    @PostMapping(value = "/save")
    public InvokeResult<List<PartnerAdvertisementDeliveryRecord>> savePartnerAdvertisement(@RequestBody StoreInfoRequest storeInfoRequest) {
        AuthenticationService.setAdminLogin();
        return InvokeResult.SuccessResult(partnerAdvertisementService.requestPartnerAdvertisement(storeInfoRequest));
    }
}
