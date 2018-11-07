package com.sztouyun.advertisingsystem.api.advertisement.material;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialOperationLog;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialStatusEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.service.material.PartnerMaterialOperationLogService;
import com.sztouyun.advertisingsystem.service.material.PartnerMaterialService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.LongFunction1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api("合作方广告素材接口")
@RestController
@RequestMapping("/api/partnerMaterial")
public class  PartnerAdvertisementMaterialApiController extends BaseApiController {
    @Autowired
    private PartnerMaterialService partnerMaterialService;
    @Autowired
    private PartnerMaterialOperationLogService partnerMaterialOperationLogService;

    @ApiOperation(value="查询第三方素材列表",notes = "创建人：文丰")
    @PostMapping(value="")
    public InvokeResult<PageList<PartnerMaterialItem>> getPartnerMaterials(@Validated @RequestBody PartnerMaterialRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<PartnerMaterial> page=partnerMaterialService.getPartnerMaterials(request);
        PageList<PartnerMaterialItem> pageList= ApiBeanUtils.convertToPageList(page,item->{
            PartnerMaterialStatusEnum statusEnum=EnumUtils.toEnum(item.getMaterialStatus(), PartnerMaterialStatusEnum.class);
            PartnerMaterialItem partnerMaterialItem=ApiBeanUtils.copyProperties(item,PartnerMaterialItem.class);
            partnerMaterialItem.setPartnerName(getPartnerName(item.getPartnerId()));
            partnerMaterialItem.setPartnerMaterialStatusName(statusEnum.getDisplayName());
            partnerMaterialItem.setAdvertisementPositionTypeName(EnumUtils.toEnum(item.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName());
            partnerMaterialItem.setMaterialTypeName(EnumUtils.toEnum(item.getMaterialType(), MaterialTypeEnum.class).getDisplayName());
            partnerMaterialItem.setUploadTime(item.getCreatedTime());
            partnerMaterialItem.setPartnerMaterialStatus(item.getMaterialStatus());
            partnerMaterialItem.setAuditor(getUserNickname(item.getAuditorId()));
            return  partnerMaterialItem;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value="审核素材",notes = "创建人：：文丰")
    @PostMapping(value="/audit")
    public InvokeResult<String> auditMaterial(@Validated @RequestBody AuditMaterialRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        partnerMaterialService.auditMaterial(request);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="查询第三方素材状态",notes = "创建人：文丰")
    @PostMapping(value="statusStatistic")
    public InvokeResult<List<MaterialStatusStatisticViewModel>> statusStatistic(@Validated @RequestBody PartnerMaterialRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        request.setPartnerMaterialStatus(null);
        List<MaterialStatusStatisticViewModel> list=partnerMaterialService.statusStatistic(request);
        MaterialStatusStatisticViewModel all=new MaterialStatusStatisticViewModel();
        Map<Integer,Long> map= Linq4j.asEnumerable(list).toMap(a->a.getMaterialStatus(), a->a.getMaterialCount());
        all.setMaterialCount(Linq4j.asEnumerable(map.values()).sum((LongFunction1<Long>) aLong -> aLong));
        EnumUtils.toValueMap(PartnerMaterialStatusEnum.class).keySet().forEach(key->{
            if(!map.keySet().contains(key)){
                list.add(new MaterialStatusStatisticViewModel(key,0L));
            }
        });
        list.add(all);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value="查询第三方素材详情",notes = "创建人：文丰")
    @PostMapping(value="{id}")
    public InvokeResult<PartnerMaterialDetailViewModel> getPartnerMaterialDetailInfo(@PathVariable("id") String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("素材ID不能为空");
        PartnerMaterial partnerMaterial=partnerMaterialService.findById(id);
        PartnerMaterialDetailViewModel partnerMaterialDetailViewModel=new PartnerMaterialDetailViewModel();
        partnerMaterialDetailViewModel.setUrl(partnerMaterial.getUrl());
        PartnerMaterialOperationLog partnerMaterialOperationLog=partnerMaterialOperationLogService.findAuditLog(id);
        if(partnerMaterialOperationLog!=null){
            partnerMaterialDetailViewModel.setAuditRemark(partnerMaterialOperationLog.getRemark());
            partnerMaterialDetailViewModel.setReason(getAuditReasonFromCache(partnerMaterialOperationLog.getReasonId()));
            partnerMaterialDetailViewModel.setSubReason(getAuditReasonFromCache(partnerMaterialOperationLog.getSubReasonId()));
        }
        return  InvokeResult.SuccessResult(partnerMaterialDetailViewModel);
    }

}
