package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.service.system.OrganizationService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.organization.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(value = "组织结构接口")
@RestController
@RequestMapping("/api/organization")
public class OrganizationApiController extends BaseApiController {

    @Autowired
    private OrganizationService organizationService;

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询所有组织结构信息组装成树结构", notes = "创建人: 王伟权")
    @PostMapping(value="/all")
    public InvokeResult<List<OrganizationTreeViewModel>> getOrganizationsTree() {
        Iterable<Organization> organizations = organizationService.getAllOrganization();
        String rootId = getUser().isAdmin()?Organization.ROOT_PARENT_ID:getUser().getOrganizationId();
        List<OrganizationTreeViewModel> organizationTreeList = ApiBeanUtils.convertToTreeList(organizations, organization -> {
            OrganizationTreeViewModel treeViewModel = ApiBeanUtils.copyProperties(organization, OrganizationTreeViewModel.class);
            treeViewModel.setOwnerName(getUserNickname(organization.getOwnerId()));

            if (StringUtils.isEmpty(organization.getParentId()) || Organization.ROOT_PARENT_ID.equals(organization.getParentId())) {
                treeViewModel.setParentOrganizationName("");
            } else {
                treeViewModel.setParentOrganizationName(organization.getParent().getName());
            }
            return treeViewModel;
        }, rootId);
        return InvokeResult.SuccessResult(organizationTreeList);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "分页查询所有组织机构信息, 列表展示", notes = "创建人: 王伟权")
    @PostMapping(value = "")
    public InvokeResult<PageList<OrganizationListViewModel>> getAllOrganizations(@Validated @RequestBody OrganizationPageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        MyPageRequest pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<Organization> pages = organizationService.getAllOrganizations(viewModel.getName(), viewModel.getCurrentId(), pageable);
        PageList<OrganizationListViewModel> organizationList = ApiBeanUtils.convertToPageList(pages, organization -> {
            OrganizationListViewModel organizationListViewModel = new OrganizationListViewModel();

            if (StringUtils.isEmpty(organization.getParentId()) || Organization.ROOT_PARENT_ID.equals(organization.getParentId())) {
                organizationListViewModel.setParentOrganizationName("");
            } else {
                organizationListViewModel.setParentOrganizationName(organization.getParent().getName());
            }

            organizationListViewModel.setId(organization.getId());
            organizationListViewModel.setCreatedTime(organization.getCreatedTime());
            organizationListViewModel.setCreatorName(getUserNickname(organization.getCreatorId()));
            organizationListViewModel.setName(organization.getName());
            organizationListViewModel.setOwnerName(getUserNickname(organization.getOwnerId()));

            return organizationListViewModel;
        });

        return InvokeResult.SuccessResult(organizationList);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询组织机构详情", notes = "创建人: 王伟权")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InvokeResult<OrganizationViewModel> getOrganization(@PathVariable String id) throws InstantiationException, IllegalAccessException {
        Organization organization = organizationService.getOrganization(id);
        return InvokeResult.SuccessResult(ApiBeanUtils.copyProperties(organization, OrganizationViewModel.class));
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "添加组织结构信息", notes = "创建人: 王伟权  code = -2: 组织结构已经存在")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public InvokeResult createOrganization(@Validated @RequestBody CreateOrganizationViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Organization organization = new Organization();
        BeanUtils.copyProperties(viewModel, organization);
        if(StringUtils.isEmpty(organization.getParentId()))
            organization.setParentId(Organization.ROOT_PARENT_ID);
        organizationService.createOrganization(organization);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "修改组织结构信息", notes = "创建人: 王伟权")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public InvokeResult updateOrganization(@Validated @RequestBody UpdateOrganizationViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        Organization organization =organizationService.getOrganization(viewModel.getId());
        Organization newOrganization = ApiBeanUtils.copyProperties(organization, Organization.class);
        BeanUtils.copyProperties(viewModel, newOrganization);

        organizationService.updateOrganization(newOrganization);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "删除组织结构信息", notes = "创建人: 王伟权")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public InvokeResult deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return InvokeResult.SuccessResult();
    }
}
