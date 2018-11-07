package com.sztouyun.advertisingsystem.api.monitor;


import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerAdvertisementListViewModel;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorListRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "第三方广告监控管理接口")
@RestController
@RequestMapping("/api/partnerAdvertisementMonitor")
public class ThirdPartnerAdvertisementMonitorApiController extends BaseApiController {

    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;

    @ApiOperation(value = "第三方广告监控列表", notes = "创建人: 毛向军")
    @PostMapping(value = "queryPartnerAdvertisementMonitors")
    public InvokeResult<PageList<PartnerAdvertisementListViewModel>> queryPartnerAdvertisementMonitors(@Validated @RequestBody PartnerAdvertisementMonitorListRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        Page<PartnerAdvertisementListViewModel> pages =  partnerAdvertisementService.queryPartnerAdvertisementMonitors(request);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pages, viewModel->{
            viewModel.setMaterialTypeName(EnumUtils.getDisplayName(viewModel.getMaterialType(), MaterialTypeEnum.class));
            viewModel.setCooperationPatternName(EnumUtils.getDisplayName(viewModel.getCooperationPattern(), CooperationPatternEnum.class));
            viewModel.setStatusName(EnumUtils.getDisplayName(viewModel.getStatus(), MonitorStatusEnum.class));
            viewModel.setOwnerName(getUserNickname(viewModel.getOwnerId()));
            return viewModel;
        }));
    }

    @ApiOperation(value = "第三方广告监控状态数量统计", notes = "创建人: 毛向军")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<PartnerAdvertisementMonitorCountStatisticInfo> getPartnerAdvertisementMonitorStatusStatistics(@Validated @RequestBody PartnerAdvertisementMonitorCountStatisticRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(partnerAdvertisementService.getPartnerAdvertisementMonitorStatusStatistics(request));
    }
}