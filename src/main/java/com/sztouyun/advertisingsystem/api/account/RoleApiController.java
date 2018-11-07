package com.sztouyun.advertisingsystem.api.account;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.ITreeList;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.account.Role;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.service.account.RoleService;
import com.sztouyun.advertisingsystem.service.system.MenuService;
import com.sztouyun.advertisingsystem.service.system.PermissionService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.account.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuTreeNodeTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.system.RoleMenusViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Enumerable;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "角色接口")
@RestController
@RequestMapping("/api/role")
public class RoleApiController extends BaseApiController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "新建角色", notes = "创建者：文丰")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public InvokeResult createRole(@Validated @RequestBody CreateRoleViewModel viewModel, BindingResult result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Role role = new Role();
        BeanUtils.copyProperties(viewModel, role);
        RoleTypeEnum roleType = EnumUtils.toEnum(viewModel.getRoleType(),RoleTypeEnum.class);
        if (null == roleType)
            return InvokeResult.Fail("角色类型不存在");
        role.setRoleType(viewModel.getRoleType());
        roleService.createRole(role);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "查询角色类型", notes = "创建者：文丰")
    @RequestMapping(value = "type", method = RequestMethod.GET)
    public InvokeResult<Map<Integer,String>> queryRoleTypeList() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return InvokeResult.SuccessResult(EnumUtils.toValueMap(RoleTypeEnum.class));
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "禁用启用角色", notes = "创建人：文丰")
    @PostMapping(value = "/{id}/toggledEnabled")
    public InvokeResult toggledEnable(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("ID不能为空");
        roleService.toggledEnable(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "删除角色", notes = "创建者：文丰")
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public InvokeResult deleteRole(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        roleService.deleteRole(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "修改角色", notes = "创建者：文丰")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public InvokeResult updateRole(@Validated @RequestBody UpdateRoleViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Role role = roleService.getRole(viewModel.getId());
        BeanUtils.copyProperties(viewModel, role);
        roleService.updateRole(role);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "查询角色列表", notes = "创建者：文丰")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<RoleDetailViewModel>> queryRoles(@Validated @RequestBody RolePageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<Role> pages = roleService.queryRoleList(viewModel.getRoleName(), pageable);
        PageList<RoleDetailViewModel> resultList = ApiBeanUtils.convertToPageList(pages, role ->
        {
            RoleDetailViewModel detailViewModel = new RoleDetailViewModel();
            BeanUtils.copyProperties(role, detailViewModel);
            detailViewModel.setCreator(getUserNickname(role.getCreatorId()));
            detailViewModel.setRoleType(role.getRoleType());
            detailViewModel.setRoleTypeName(EnumUtils.getDisplayName(role.getRoleType(),RoleTypeEnum.class));
            return detailViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "查询角色详情", notes = "创建者：文丰")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public InvokeResult<RoleDetailViewModel> queryRoleDetail(@PathVariable("id") String id) {
        RoleDetailViewModel viewModel = new RoleDetailViewModel();
        Role role=roleService.getRole(id);
        BeanUtils.copyProperties(role, viewModel);
        viewModel.setRoleTypeName(EnumUtils.getDisplayName(role.getRoleType(),RoleTypeEnum.class));
        viewModel.setCreator(getUserNickname(role.getCreatorId()));
        return InvokeResult.SuccessResult(viewModel);
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "分配菜单", notes = "创建者：陈化静")
    @RequestMapping(value = "/distribute", method = RequestMethod.POST)
    public InvokeResult distributeMenu(@Validated @RequestBody RoleMenuViewModel roleMenuViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        roleService.distributeMenu(roleMenuViewModel);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询全部角色", notes = "创建者：王英峰")
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public InvokeResult<List<UpdateRoleViewModel>> getAllRole(){
        List<Role> roles = roleService.getAccessibleRoles(getUser().getRoleId());
        List<UpdateRoleViewModel> roleAllViewModels = Linq4j.asEnumerable(roles).select(role -> ApiBeanUtils.copyProperties(role, UpdateRoleViewModel.class)).toList();
        return InvokeResult.SuccessResult(roleAllViewModels);
    }

    @PreAuthorize("hasRole('Admin')")
    @ApiOperation(value = "根据角色查询对应的权限菜单", notes = "修改人：王伟权")
    @RequestMapping(value = "/menus/{roleId}", method = RequestMethod.POST)
    public <T extends ITreeList<T>> InvokeResult<List<RoleMenusViewModel>> getMenusByRole(@PathVariable("roleId") String id){
        Role role = roleService.getRole(id);
        List<Menu> roleMenus = role.getMenus();
        List<Menu> menus = menuService.getAllMenu();
        List<Permission> allPermissions = permissionService.getAllPermission();
        List<Permission> rolePermissions = role.getPermissions();

        Enumerable<RoleMenusViewModel> roleMenusViewModels = Linq4j.asEnumerable(allPermissions).select(allPermission -> {
            RoleMenusViewModel roleMenusViewModel = ApiBeanUtils.copyProperties(allPermission, RoleMenusViewModel.class);
            roleMenusViewModel.setMenuName(allPermission.getPermissionName());
            roleMenusViewModel.setParentId(allPermission.getMenuId());
            roleMenusViewModel.setChecked(Linq4j.asEnumerable(rolePermissions).any(rolePermission -> (rolePermission.getId()).equals(allPermission.getId())));
            roleMenusViewModel.setNodeType(MenuTreeNodeTypeEnum.BUTTON.getValue());
            return roleMenusViewModel;
        });

        Map<String, List<RoleMenusViewModel>> roleMenuMap = roleMenusViewModels.toMap(a -> a.getParentId(), c -> roleMenusViewModels.where(b -> b.getParentId().equals(c.getParentId())).toList());

        List<RoleMenusViewModel> rootTreeList = ApiBeanUtils.convertToTreeList(menus, menu ->{
                    RoleMenusViewModel viewModel =ApiBeanUtils.copyProperties(menu, RoleMenusViewModel.class);
                    viewModel.setNodeType(MenuTreeNodeTypeEnum.MENU.getValue());
                    viewModel.setChildren(roleMenuMap.getOrDefault(menu.getId(), new ArrayList<>()));
                    viewModel.setChecked(Linq4j.asEnumerable(roleMenus).any(menuRole->(menuRole.getId()).equals(menu.getId())));
                    return viewModel;
                }, Menu.ROOT_PARENT_ID);
        return InvokeResult.SuccessResult(rootTreeList);
    }
}
