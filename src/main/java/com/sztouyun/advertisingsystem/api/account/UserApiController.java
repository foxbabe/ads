package com.sztouyun.advertisingsystem.api.account;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.Role;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.model.system.Permission;
import com.sztouyun.advertisingsystem.repository.account.RoleRepository;
import com.sztouyun.advertisingsystem.service.account.RoleService;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.system.MenuService;
import com.sztouyun.advertisingsystem.service.system.PermissionService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.ValidationUtils;
import com.sztouyun.advertisingsystem.viewmodel.account.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuTreeNodeTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.system.MenuViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.UserOrganizationViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(value = "用戶接口")
@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;



    @ApiOperation("修改密码")
    @PreAuthorize("permitAll")
    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    public InvokeResult modifyPassword(@Validated @RequestBody ModifyPasswordViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        userService.modifyPassword(viewModel.getPassword(), viewModel.getNewPassword());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "发送重置密码验证短信", notes = "创建者：毛向军")
    @PreAuthorize("permitAll")
    @RequestMapping(value = "resetPassword/{username}/sendVerificationCode", method = RequestMethod.POST)
    public InvokeResult sendResetPasswordVerificationCode(@PathVariable("username") String username) {
        userService.sendResetPasswordVerificationCode(username);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "验证短信验证码并重置密码", notes = "创建者：毛向军")
    @PreAuthorize("permitAll")
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public InvokeResult resetPassword(@Validated @RequestBody ResetPasswordViewModel resetPasswordViewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        userService.resetPassword(resetPasswordViewModel.getMobile(),resetPasswordViewModel.getVerificationCode(),resetPasswordViewModel.getNewPassword());
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "新建用户", notes = "创建者：文丰")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public InvokeResult createUser(@Validated @RequestBody CreateUserViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        validateRolePhone(viewModel.getPhoneNumber(), viewModel.getRoleId());
        if (!getAccessibleRoleIds().contains(viewModel.getRoleId()))
            return InvokeResult.Fail("请选择正确的角色");

        User user = new User();
        user.setUsername(viewModel.getPhoneNumber());
        user.setNickname(viewModel.getNickname());
        String organizationId = viewModel.getOrganizationId();
        if (!StringUtils.isEmpty(organizationId))
            user.setOrganizationId(organizationId);
        user.setRoleId(viewModel.getRoleId());
        user.setCustomerId(viewModel.getCustomerId());
        userService.createUser(user);
        return InvokeResult.SuccessResult();
    }

    private void validateRolePhone(@Validated @RequestBody String phoneNumber, String roleId) {
        Role role = roleRepository.findById(roleId);
        if (role==null)
            throw new BusinessException("该角色不存在");
        if(RoleTypeEnum.AdvertisementCustomer.getValue().equals(role.getRoleType())){
            if(!ValidationUtils.validateMobile(phoneNumber, Constant.REG_PHONE))
                throw new BusinessException("座机号不能作为登陆手机号，请修改广告客户联系电话");
        }else {
            if(!ValidationUtils.validateMobile(phoneNumber, Constant.REG_PHONE))
                throw new BusinessException("联系电话格式错误！");
        }
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "删除用户", notes = "创建人：文丰")
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public InvokeResult deleteUser(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        userService.deleteUser(id);
        return InvokeResult.SuccessResult();
    }



    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "禁用启用用户", notes = "创建人：文丰")
    @PostMapping(value = "/{id}/toggledEnabled")
    public InvokeResult toggledEnable(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("ID不能为空");
        userService.toggledEnable(id);
        return InvokeResult.SuccessResult();
    }

    /**
     * 获得当前登录用户所拥有的所有角色ID
     * @return
     */
    private List<String> getAccessibleRoleIds() {
        List<Role> roles = roleService.getAccessibleRoles(getUser().getRoleId());
        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "修改用户", notes = "创建人：文丰")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public InvokeResult updateUser(@Validated @RequestBody UpdateUserViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        validateRolePhone(viewModel.getPhoneNumber(), viewModel.getRoleId());
        if (!getAccessibleRoleIds().contains(viewModel.getRoleId()))
            return InvokeResult.Fail("请选择正确的角色");

        User user =ApiBeanUtils.copyProperties(viewModel,User.class);
        user.setUsername(viewModel.getPhoneNumber());
        userService.updateUser(user, viewModel.getRoleId());
        return InvokeResult.SuccessResult();
    }



    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询用户列表", notes = "创建人：文丰")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<UserDetailViewModel>> queryUsers(@Validated @RequestBody UserPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<User> pages = userService.queryUserList(viewModel.getNickname(), pageable);
        return InvokeResult.SuccessResult(convertToUserPageList(pages));
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询用户详情", notes = "创建人：文丰")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public InvokeResult<UserDetailViewModel> queryUserDetail(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        UserDetailViewModel viewModel = new UserDetailViewModel();
        User user = userService.getUser(id);
        BeanUtils.copyProperties(user, viewModel);
        Organization organization = user.getOrganization();
        if (organization != null) {
            viewModel.setOrganizationName(organization.getName());
            viewModel.setLeader(getUserNickname(getUserLeaderId(user)));
        }
        RoleTypeEnum roleTypeEnum = user.getRoleTypeEnum();
        viewModel.setRoleType(roleTypeEnum.getDisplayName());
        Role role = user.getRole();
        if (role == null) {
            viewModel.setRoleId("");
        } else {
            viewModel.setRoleId(role.getId());
        }
        viewModel.setCreator(getUserNickname(user.getCreatorId()));
        return InvokeResult.SuccessResult(viewModel);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "查询指定组织下的用户列表(支持按名称模糊搜素)", notes = "创建人：文丰")
    @RequestMapping(value = "/organization/{organizationId}", method = RequestMethod.POST)
    public InvokeResult<PageList<UserOrganizationViewModel>> queryUsersByOrganizationId(@PathVariable String organizationId, @Validated @RequestBody UserPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(organizationId))
            return InvokeResult.Fail("组织编号不能为空");
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<User> pages = userService.findAllByOrganizationIdAndUsername(organizationId, viewModel.getNickname(), pageable);
        ;
        PageList<UserOrganizationViewModel> resultList = ApiBeanUtils.convertToPageList(pages, user -> {
            UserOrganizationViewModel returnUserViewModel = new UserOrganizationViewModel();
            if (user.getOrganization() != null) {
                returnUserViewModel.setOrganizationName(user.getOrganization().getName());
                returnUserViewModel.setLeader(getUserNickname(getUserLeaderId(user)));
            }
            RoleTypeEnum roleTypeEnum=user.getRoleTypeEnum();
            BeanUtils.copyProperties(user, returnUserViewModel);
            returnUserViewModel.setCreator(getUserNickname(user.getCreatorId()));
            returnUserViewModel.setRoleType(roleTypeEnum.getDisplayName());
            returnUserViewModel.setRoleTypeNo(roleTypeEnum.getValue());
            returnUserViewModel.setPhoneNumber(user.getUsername());
            return returnUserViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    /**
     * 获取用户的上级领导ID
     * @param user 用户
     * @return
     */
    private String getUserLeaderId(User user){
        String leaderId = user.getOrganization().getOwnerId();
        //管理人员的上级为上级部门的负责人
        if (user.getRoleTypeEnum().equals(RoleTypeEnum.ManagerialStaff)
                && !user.getOrganization().getParentId().equals(Organization.ROOT_PARENT_ID)) {
            Organization parentOrganization = user.getOrganization().getParent();
            if (parentOrganization != null) {
                leaderId = parentOrganization.getOwnerId();
            }
        }
        return leaderId;
    }

    private PageList<UserDetailViewModel> convertToUserPageList(Page<User> pages) {
        PageList<UserDetailViewModel> resultList = ApiBeanUtils.convertToPageList(pages, user -> {
            UserDetailViewModel userDetailViewModel = new UserDetailViewModel();
            BeanUtils.copyProperties(user, userDetailViewModel);
            Organization organization = user.getOrganization();
            if (organization != null) {
                userDetailViewModel.setOrganizationName(organization.getName());
                userDetailViewModel.setLeader(getUserNickname(getUserLeaderId(user)));
            }
            RoleTypeEnum roleTypeEnum = user.getRoleTypeEnum();
            userDetailViewModel.setPhoneNumber(user.getUsername());
            userDetailViewModel.setRoleType(roleTypeEnum.getDisplayName());
            userDetailViewModel.setRoleTypeNo(roleTypeEnum.getValue());
            Role role = user.getRole();
            if (role == null) {
                userDetailViewModel.setRoleId("");
            } else {
                userDetailViewModel.setRoleId(role.getId());
                userDetailViewModel.setRoleName(role.getRoleName());
            }
            userDetailViewModel.setCreator(getUserNickname(user.getCreatorId()));
            return userDetailViewModel;
        });
        return resultList;
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "选择组织机构负责人列表(支持按名称模糊搜素)", notes = "王英峰")
    @RequestMapping(value = "/OrganizationOwners", method = RequestMethod.POST)
    public InvokeResult<PageList<UserDetailViewModel>> getOrganizationOwners(@Validated @RequestBody UserPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        Page<User> pages = userService.getOrganizationOwners(viewModel.getNickname(), pageable);
        PageList<UserDetailViewModel> resultList = ApiBeanUtils.convertToPageList(pages, user -> {
            UserDetailViewModel userDetailViewModel = new UserDetailViewModel();
            BeanUtils.copyProperties(user, userDetailViewModel);
            userDetailViewModel.setPhoneNumber(user.getUsername());
            userDetailViewModel.setCreator(getUserNickname(user.getCreatorId()));
            userDetailViewModel.setCreatedTime(user.getCreatedTime());
            return userDetailViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    /**
     * 【客户分配】可以根据组织结构或者用户名查询用户并统计用户维护客户数量
     */
    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "据组织结构或者用户名查询用户并统计用户维护客户数量", notes = "陈化静")
    @RequestMapping(value = "/userInfosStatistics/{organizationId}", method = RequestMethod.POST)
    public InvokeResult<PageList<UserInfoViewModel>> retrieveUserInfoByOrganizationIdOrUsername(
            @PathVariable String organizationId, @Validated @RequestBody UserPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(organizationId))
            return InvokeResult.Fail("组织编号不能为空");

        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize());
        String nickname = viewModel.getNickname() == null ? "" : viewModel.getNickname();
        Page<User> pages = userService.getUserInfoByOrganizationIdOrUsername(organizationId, nickname, pageable);

        PageList<UserInfoViewModel> resultList = ApiBeanUtils.convertToPageList(pages, user ->
        {
            UserInfoViewModel userInfoViewModel = new UserInfoViewModel();
            BeanUtils.copyProperties(user, userInfoViewModel);
            userInfoViewModel.setCreator(getUserNickname(user.getCreatorId()));
            userInfoViewModel.setPhoneNumber(user.getUsername());
            return userInfoViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("permitAll")
    @ApiOperation(value = "根据登录用户角色权限查询权限菜单信息", notes = "创建者：张瑞兵")
    @GetMapping(value = "/userInfoAndMenus")
    public InvokeResult<UserRolePermissionViewModel> getUserInfoAndMenus() {
        var user = getUser();
        UserRolePermissionViewModel userRolePermissionViewModel = ApiBeanUtils.copyProperties(user, UserRolePermissionViewModel.class);
        if (!user.isAdmin() && StringUtils.isEmpty(user.getRoleId()))
            return InvokeResult.SuccessResult(userRolePermissionViewModel);

        Role role = roleRepository.findById(user.getRoleId());
        List<Menu> menus;
        List<Permission> rolePermission;
        if (user.isAdmin()) {
            menus = menuService.getAllMenu();
            rolePermission = permissionService.getAllPermission();
        } else {
            menus = role.getMenus();
            rolePermission = role.getPermissions();
        }
        if (menus.size() > 0) {
            menus = Linq4j.asEnumerable(menus).orderBy(m->m.getSortNumber()).toList();

            Enumerable<MenuViewModel> roleMenusViewModels = Linq4j.asEnumerable(rolePermission).select(allPermission -> {
                MenuViewModel menuViewModel = ApiBeanUtils.copyProperties(allPermission, MenuViewModel.class);
                menuViewModel.setMenuName(allPermission.getPermissionName());
                menuViewModel.setParentId(allPermission.getMenuId());
                menuViewModel.setNodeType(MenuTreeNodeTypeEnum.BUTTON.getValue());
                return menuViewModel;
            });
            Map<String, List<MenuViewModel>> roleMenuMap = roleMenusViewModels.toMap(a -> a.getParentId(), c -> roleMenusViewModels.where(b -> b.getParentId().equals(c.getParentId())).toList());

            List<MenuViewModel> menuViewModels = ApiBeanUtils.convertToTreeList(menus, menu -> {
                MenuViewModel menuViewModel = ApiBeanUtils.copyProperties(menu, MenuViewModel.class);
                menuViewModel.setNodeType(MenuTreeNodeTypeEnum.MENU.getValue());
                menuViewModel.setChildren(roleMenuMap.getOrDefault(menu.getId(), new ArrayList<>()));
                return menuViewModel;
            }, Menu.ROOT_PARENT_ID);

            userRolePermissionViewModel.setMenus(menuViewModels);
            String roleName = user.isAdmin() ? RoleTypeEnum.Admin.getDisplayName() : role.getRoleName();
            userRolePermissionViewModel.setRoleName(roleName);
            Integer roleType = user.isAdmin() ? RoleTypeEnum.Admin.getValue() : role.getRoleType();
            userRolePermissionViewModel.setRoleType(roleType);
        }
        return InvokeResult.SuccessResult(userRolePermissionViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "从指定组织移除单个人员", notes = "创建者：文丰")
    @PostMapping(value = "/{userId}/organization/{organizationId}/delete")
    public InvokeResult removeUserFromOrganization(@PathVariable("userId") String userId, @PathVariable("organizationId") String organizationId) {
        if (StringUtils.isEmpty(userId))
            return InvokeResult.Fail("用户ID不能为空");
        if (StringUtils.isEmpty(organizationId))
            return InvokeResult.Fail("组织ID不能为空");
        return userService.removeUserFromOrganization(organizationId, userId);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "从指定组织批量移除人员", notes = "创建者：文丰")
    @PostMapping(value = "/userList/organization/{organizationId}/delete")
    public InvokeResult batchRemoveUserFromOrganization(@Validated @RequestBody BatchOperateUserViewModel viewModel, @PathVariable("organizationId") String organizationId, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(organizationId))
            return InvokeResult.Fail("组织ID不能为空");
        return userService.batchRemoveUserFromOrganization(organizationId, viewModel.getList());
    }


}
