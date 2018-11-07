package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.AuditReasonConfig;
import com.sztouyun.advertisingsystem.service.system.AuditReasonConfigService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "问题分类接口")
@RestController
@RequestMapping("/api/auditReasonConfig")
public class AuditReasonConfigController extends BaseApiController {

    @Autowired
    private AuditReasonConfigService auditReasonConfigService;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "添加问题分类", notes = "创建者：杨浩")
    @RequestMapping(method = RequestMethod.POST)
    InvokeResult createAuditReasonConfig(@Validated @RequestBody AuditReasonConfigViewModel auditReasonConfigViewModel,
                                         BindingResult result) {
        if(result.hasErrors()){
            return ValidateFailResult(result);
        }
        AuditReasonConfig auditReasonConfig = new AuditReasonConfig();
        BeanUtils.copyProperties(auditReasonConfigViewModel, auditReasonConfig);
        auditReasonConfigService.createAuditReasonConfig(auditReasonConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "添加子分类", notes = "创建者：杨浩")
    @RequestMapping(value = "/create/sub", method = RequestMethod.POST)
    InvokeResult createSubAuditReasonConfig(@Validated @RequestBody SubAuditReasonConfigViewModel subAuditReasonConfigViewModel,
                                            BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        AuditReasonConfig auditReasonConfig = new AuditReasonConfig();
        BeanUtils.copyProperties(subAuditReasonConfigViewModel, auditReasonConfig);
        auditReasonConfigService.createSubAuditReasonConfig(auditReasonConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "查询问题分类列表", notes = "创建人:杨浩")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public InvokeResult<PageList<AuditReasonConfigListItemViewModel>> queryAuditReasonConfig(@Validated @RequestBody BasePageInfo pageInfo,BindingResult result) {
        if(result.hasErrors()){
            return ValidateFailResult(result);
        }
        Pageable pageable = new MyPageRequest(pageInfo.getPageIndex(), pageInfo.getPageSize());
        Page<AuditReasonConfig> pages = auditReasonConfigService
            .queryAuditReasonConfigList(pageable);
        PageList<AuditReasonConfigListItemViewModel> pageList = ApiBeanUtils
            .convertToPageList(pages, auditReasonConfig -> {
                AuditReasonConfigListItemViewModel auditReasonConfigListItemViewModel = new AuditReasonConfigListItemViewModel();
                BeanUtils.copyProperties(auditReasonConfig, auditReasonConfigListItemViewModel);
                if(!StringUtils.isEmpty(auditReasonConfig.getParentId())){
                    AuditReasonConfig parentAuditReasonConfig = auditReasonConfigService.AuditReasonConfigById(auditReasonConfig.getParentId());
                    auditReasonConfigListItemViewModel.setId(parentAuditReasonConfig.getId());
                    auditReasonConfigListItemViewModel.setName(parentAuditReasonConfig.getName());
                    auditReasonConfigListItemViewModel.setSubId(auditReasonConfig.getId());
                    auditReasonConfigListItemViewModel.setSubName(auditReasonConfig.getName());
                }else{
                    auditReasonConfigListItemViewModel.setParent(true);
                }
                auditReasonConfigListItemViewModel.setCreator(getUserNickname(auditReasonConfig.getCreatorId()));
                return auditReasonConfigListItemViewModel;
            });

        return InvokeResult.SuccessResult(pageList);
    }



    @ApiOperation(value = "查询所有问题分类及子类", notes = "创建人:杨浩")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public InvokeResult<List<AuditReasonConfigTreeViewModel>> queryAllAuditReasonConfig() {
        List<AuditReasonConfigTreeViewModel> auditReasonConfigTreeViewModels = auditReasonConfigService
            .findAllParentByCount().stream().map(r -> {
                AuditReasonConfigTreeViewModel auditReasonConfigTreeViewModel = new AuditReasonConfigTreeViewModel(
                    r.getId(), r.getName());
                List<AuditReasonConfig> children = auditReasonConfigService
                    .queryAuditReasonConfigByParentIdAndCountDesc(r.getId());
                auditReasonConfigTreeViewModel.setChildren(children.stream()
                    .map(r1 -> new AuditReasonConfigTreeViewModel(r1.getId(), r1.getName()))
                    .collect(Collectors.toList()));
                return auditReasonConfigTreeViewModel;
            }).collect(Collectors.toList());
        return InvokeResult.SuccessResult(auditReasonConfigTreeViewModels);
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "查询所有问题分类", notes = "创建人:杨浩")
    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public InvokeResult<List<AuditReasonConfigTreeViewModel>> queryAllParentAuditReasonConfig() {
        List<AuditReasonConfigTreeViewModel> auditReasonConfigTreeViewModels = auditReasonConfigService
                .findAllParent().stream().map(r -> new AuditReasonConfigTreeViewModel(r.getId(), r.getName())).collect(Collectors.toList());
        return InvokeResult.SuccessResult(auditReasonConfigTreeViewModels);
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "删除问题分类", notes = "创建者：杨浩")
    @RequestMapping(value = "/{id}/delete",method = RequestMethod.POST)
    public InvokeResult deleteAuditReasonConfig(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        auditReasonConfigService.deleteAuditReasonConfigById(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "更新问题分类", notes = "创建者：杨浩")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public InvokeResult updateAuditReasonConfig(@Validated @RequestBody UpdateAuditReasonConfigViewModel updateAuditReasonConfigViewModel,
                                         BindingResult result) {
        if(result.hasErrors()){
            return ValidateFailResult(result);
        }
        AuditReasonConfig auditReasonConfig = new AuditReasonConfig();
        if(StringUtils.isEmpty(updateAuditReasonConfigViewModel.getSubId())){
            auditReasonConfig.setId(updateAuditReasonConfigViewModel.getParentId());
            auditReasonConfig.setName(updateAuditReasonConfigViewModel.getParentName());
        }else{
            auditReasonConfig.setId(updateAuditReasonConfigViewModel.getSubId());
            auditReasonConfig.setName(updateAuditReasonConfigViewModel.getSubName());
            auditReasonConfig.setParentId(updateAuditReasonConfigViewModel.getParentId());
        }
        auditReasonConfigService.updateAuditReasonConfig(auditReasonConfig);
        return InvokeResult.SuccessResult();
    }

}
