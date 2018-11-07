package com.sztouyun.advertisingsystem.api.adPosition;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.adPosition.AdPosition;
import com.sztouyun.advertisingsystem.model.adPosition.QAdPosition;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.service.adPosition.AdPositionService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.adPosition.AdPositionListViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adPosition.CreateAdPositionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adPosition.UpdateAdPositionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Api(value = "类别广告位配置接口")
@RestController
@RequestMapping("/api/adPosition")
public class AdPositionApiController extends BaseApiController {
    @Autowired
    private AdPositionService adPositionService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询类别广告位配置列表",notes = "创建人：王英峰")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public InvokeResult<PageList<AdPositionListViewModel>> findAdsPositionList(@Validated  @RequestBody BasePageInfo viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(),viewModel.getPageSize(),new QSort(QAdPosition.adPosition.storeType.asc()));
        Page<AdPosition> pages = adPositionService.findByPage(pageable);
        PageList<AdPositionListViewModel> pageList = ApiBeanUtils.convertToPageList(pages, adsPosition->{
            AdPositionListViewModel adsPositionListViewModel = new AdPositionListViewModel();
            BeanUtils.copyProperties(adsPosition,adsPositionListViewModel);

            StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(adsPosition.getStoreType(), StoreTypeEnum.class);
            adsPositionListViewModel.setStoreTypeName(storeTypeEnum.getDisplayName());
            adsPositionListViewModel.setCreator(getUserNickname(adsPosition.getCreatorId()));
            return adsPositionListViewModel;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value="新建类别广告位",notes = "创建人：王英峰")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public InvokeResult createAdsPosition(@Validated @RequestBody CreateAdPositionViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");

        AdPosition adPosition = new AdPosition();
        BeanUtils.copyProperties(viewModel, adPosition);
        adPositionService.createAdsPosition(adPosition);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value="修改类别广告位",notes = "创建人：王英峰")
    @RequestMapping(value="/update",method = RequestMethod.POST)
    public InvokeResult updateAdsPosition(@Validated @RequestBody UpdateAdPositionViewModel viewModel, BindingResult result){
        if(result.hasErrors())
        return ValidateFailResult(result);
        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            return InvokeResult.Fail("请输入正确的门店类型");

        AdPosition adPosition =  adPositionService.getAdsPosition(viewModel.getId());
        BeanUtils.copyProperties(viewModel, adPosition);
        adPositionService.updateAdsPosition(adPosition);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除类别广告位", notes = "创建人：王英峰")
    @RequestMapping(value="{id}/delete",method = RequestMethod.POST)
    public InvokeResult deleteCustomer(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        adPositionService.deleteAdsPosition(id);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value = "获取不同类型广告位总数", notes = "创建人: 王伟权")
    @GetMapping(value = "/getAdvertisementPositionsWithType")
    public InvokeResult<List<DistributionStatisticDto>> getAdvertisementPositionsWithType(@RequestParam(required = false) String contractId) {
        return InvokeResult.SuccessResult(adPositionService.getAdvertisementPositionWithType(contractId));
    }


}
