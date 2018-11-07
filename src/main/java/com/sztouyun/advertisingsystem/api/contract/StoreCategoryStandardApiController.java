package com.sztouyun.advertisingsystem.api.contract;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.QStoreCategoryStandard;
import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandard;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.service.contract.StoreCategoryStandardService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.contract.CreateStoreCategoryStandardViewModel;
import com.sztouyun.advertisingsystem.viewmodel.contract.StoreCategoryStandardDetailViewModel;
import com.sztouyun.advertisingsystem.viewmodel.contract.StoreCategoryStandardPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.contract.UpdateStoreCategoryStandardViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(value = "门店类别标准配置接口")
@RestController
@RequestMapping("/api/storeCategoryStandard")
public class StoreCategoryStandardApiController extends BaseApiController {

    @Autowired
    private StoreCategoryStandardService storeCategoryStandardService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "创建门店类别标准", notes = "修改人: 王伟权")
    @PostMapping(value = "create")
    public InvokeResult<String> createStoreCategoryStandard(@Validated @RequestBody CreateStoreCategoryStandardViewModel viewModel, BindingResult result) {

        if(result.hasErrors())
            return ValidateFailResult(result);
        validateStoreCategoryStandardViewModel(viewModel);
        StoreCategoryStandard storeCategoryStandard = new StoreCategoryStandard();
        BeanUtils.copyProperties(viewModel, storeCategoryStandard);
        storeCategoryStandardService.createStoreCategoryStandard(storeCategoryStandard);
        return InvokeResult.SuccessResult(storeCategoryStandard.getId());
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新门店类别标准", notes = "创建人: 张瑞兵")
    @PostMapping(value = "/update")
    public InvokeResult updateStoreCategoryStandard(@Validated @RequestBody UpdateStoreCategoryStandardViewModel viewModel, BindingResult result) {

        if (result.hasErrors())
            return ValidateFailResult(result);
        validateStoreCategoryStandardViewModel(viewModel);
        if(StringUtils.isEmpty(viewModel.getId()))
            return InvokeResult.Fail("查询不到该门店类别标准");
        StoreCategoryStandard storeCategoryStandard = storeCategoryStandardService.getStoreCategoryStandard(viewModel.getId());
        BeanUtils.copyProperties(viewModel, storeCategoryStandard);
        storeCategoryStandardService.updateStoreCategoryStandard(storeCategoryStandard);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除门店类别标准", notes = "创建人：张瑞兵")
    @PostMapping(value="{id}/delete")
    public InvokeResult deleteStoreCategoryStandard(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("门店类别标准id不能为空");

        storeCategoryStandardService.deleteStoreCategoryStandard(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询门店类别标准列表", notes = "创建人：张瑞兵")
    @PostMapping(value="")
    public InvokeResult<PageList<StoreCategoryStandardDetailViewModel>> queryStoreCategoryStandards(@Validated @RequestBody StoreCategoryStandardPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(),new QSort(QStoreCategoryStandard.storeCategoryStandard.storeType.asc()));
        Page<StoreCategoryStandard> pages = storeCategoryStandardService.queryStoreCategoryStandardList( pageable);
        PageList<StoreCategoryStandardDetailViewModel> resultList = ApiBeanUtils.convertToPageList(pages, storeCategoryStandard -> {
            StoreCategoryStandardDetailViewModel detailViewModel = new StoreCategoryStandardDetailViewModel();
            BeanUtils.copyProperties(storeCategoryStandard, detailViewModel);
            StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(storeCategoryStandard.getStoreType(), StoreTypeEnum.class);
            detailViewModel.setStoreTypeName(storeTypeEnum.getDisplayName());
            detailViewModel.setCreatorName(getUserNickname(storeCategoryStandard.getCreatorId()));
            return detailViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    private void validateStoreCategoryStandardViewModel(CreateStoreCategoryStandardViewModel viewModel){
        if(viewModel.getAvgDailyTradingAmountMin()==null && viewModel.getAvgDailyTradingAmountMax()==null)
            throw new BusinessException("平均每日交易订单数最小值和最大值不能都为空！");
        if(viewModel.getAvgDailyTradingAmountMin()!=null && viewModel.getAvgDailyTradingAmountMax()!=null && viewModel.getAvgDailyTradingAmountMin()>=viewModel.getAvgDailyTradingAmountMax())
            throw new BusinessException("平均每日交易订单数最大值不能小于等于最小值！");

        StoreTypeEnum storeTypeEnum = EnumUtils.toEnum(viewModel.getStoreType(), StoreTypeEnum.class);
        if(storeTypeEnum == null)
            throw new BusinessException("请输入正确的门店类型！");
    }


}
