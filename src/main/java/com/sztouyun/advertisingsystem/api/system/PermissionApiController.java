package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.service.system.PermissionService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.CreatePermissionViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.PermissionPageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.PermissionViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
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

/**
 * Created by szty on 2017/7/28.
 */
@Api("权限接口")
@RestController
@RequestMapping("/api/permssion")
public class PermissionApiController extends BaseApiController {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "添加", notes = "修改人：王伟权")
    @PostMapping(value = "/create")
    public InvokeResult<String> createPermission(@Validated @RequestBody CreatePermissionViewModel createPermissionViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(permissionService.createPermission(ApiBeanUtils.copyProperties(createPermissionViewModel, Permission.class)));
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除", notes = "修改人: 王伟权")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public InvokeResult deletePermission(@PathVariable String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        permissionService.deletePermission(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改", notes = "修改人: 王伟权")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public InvokeResult updatePermission(@Validated @RequestBody PermissionViewModel permissionViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Permission permission = permissionService.getPermission(permissionViewModel.getId());
        BeanUtils.copyProperties(permissionViewModel, permission);
        permissionService.updatePermission(permission);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "详情查询", notes = "修改人: 王伟权")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public InvokeResult<PermissionViewModel> getPermission(@PathVariable String id){
        return InvokeResult.SuccessResult(ApiBeanUtils.copyProperties(permissionService.getPermission(id), PermissionViewModel.class));
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询所有", notes = "修改人: 王伟权")
    @GetMapping(value = "/all")
    public InvokeResult<List<PermissionViewModel>> getAllPermission() {
        List<PermissionViewModel> list = Linq4j.asEnumerable(permissionService.getAllPermission()).select(permission -> {
            return ApiBeanUtils.copyProperties(permission, PermissionViewModel.class);
        }).toList();
        return InvokeResult.SuccessResult(list);

    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "分页查询", notes = "修改人: 王伟权")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<PermissionViewModel>> getPermissionByPage(@Validated @RequestBody PermissionPageInfoViewModel permissionPageInfoViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(permissionPageInfoViewModel.getPageIndex(), permissionPageInfoViewModel.getPageSize());
        Page<Permission> pages = permissionService.findByPage(pageable);
        PageList<PermissionViewModel> resultList = ApiBeanUtils.convertToPageList(pages, permission ->
        {
            PermissionViewModel permissionViewModel = ApiBeanUtils.copyProperties(permission, PermissionViewModel.class);
            permissionViewModel.setMenuName(permission.getMenu().getMenuName());
            permissionViewModel.setCreatorName(getUserNickname(permission.getCreatorId()));
            return permissionViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

}
