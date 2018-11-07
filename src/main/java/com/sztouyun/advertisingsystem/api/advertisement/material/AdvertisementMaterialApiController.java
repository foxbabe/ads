package com.sztouyun.advertisingsystem.api.advertisement.material;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.material.Material;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementPositionConfigRepository;
import com.sztouyun.advertisingsystem.service.material.MaterialService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialStatisticsViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.DetailAdvertisementMaterialViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.MaterialCountRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("广告素材接口")
@RestController
@RequestMapping("/api/advertisement/material")
public class AdvertisementMaterialApiController extends BaseApiController {

    @Autowired
    private MaterialService advertisementMaterialService;

    @Autowired
    private ContractAdvertisementPositionConfigRepository contractAdvertisementPositionConfigRepository;

    @ApiOperation(value = "上传素材, 包括文本内容", notes = "创建人: 王伟权")
    @PostMapping(value = "/upload")
    public InvokeResult<DetailAdvertisementMaterialViewModel> uploadMaterial(@RequestParam String positionId,@RequestParam String contractId, @RequestParam(required = true) String customerId, @RequestParam(required = true) Integer materialType,@RequestParam(required = true) String resolution, MultipartFile file) {

        if (StringUtils.isEmpty(customerId))
            return InvokeResult.Fail("客户信息不能为空");
        MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(materialType, MaterialTypeEnum.class);
        if (materialTypeEnum == null || materialTypeEnum.equals(MaterialTypeEnum.Text))
            return InvokeResult.Fail("请输入有效的媒体类型");

        Material advertisementMaterial = advertisementMaterialService.uploadMaterial(contractId,positionId, customerId, materialType, file,resolution);
        return InvokeResult.SuccessResult(ApiBeanUtils.copyProperties(advertisementMaterial, DetailAdvertisementMaterialViewModel.class));
    }

    @ApiOperation(value = "查看素材", notes = "创建人: 王伟权")
    @PostMapping(value = "")
    public InvokeResult<PageList<DetailAdvertisementMaterialViewModel>> getAllMaterials(@Validated @RequestBody AdvertisementMaterialPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(viewModel.getMaterialType(), MaterialTypeEnum.class);
        if (materialTypeEnum == null)
            return InvokeResult.Fail("请输入有效的媒体类型");

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<Material> pages = advertisementMaterialService.getAllMaterials(pageable, viewModel.getCustomerId(), viewModel.getMaterialType(),viewModel.getPositionId());
        PageList<DetailAdvertisementMaterialViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementMaterial -> {
            return ApiBeanUtils.copyProperties(advertisementMaterial, DetailAdvertisementMaterialViewModel.class);
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "查看素材统计信息", notes = "创建人: 王伟权")
    @PostMapping(value = "/materialStatistic")
    public InvokeResult<List<AdvertisementMaterialStatisticsViewModel>> getMaterialStatistics(@Validated @RequestBody MaterialCountRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        String positionId=request.getPositionId();
        if(!StringUtils.isEmpty(positionId) && !contractAdvertisementPositionConfigRepository.exists(positionId))
            throw new BusinessException("广告尺寸配置ID无效");
        return InvokeResult.SuccessResult(advertisementMaterialService.getMaterialStatistics(request.getCustomerId(),positionId));
    }
}
