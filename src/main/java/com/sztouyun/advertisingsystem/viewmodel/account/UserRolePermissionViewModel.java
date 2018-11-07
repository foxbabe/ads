package com.sztouyun.advertisingsystem.viewmodel.account;

import com.sztouyun.advertisingsystem.viewmodel.system.MenuViewModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created by szty on 2017/8/4.
 */
public class UserRolePermissionViewModel {
    @ApiModelProperty(value = "昵称", required = true)
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    @ApiModelProperty(value = "用户头像")
    private String headPortrait;

    @ApiModelProperty(value = "角色类型")
    private int roleType ;

    @ApiModelProperty(value = "有权限的菜单树形集合")
    private List<MenuViewModel> menus;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public List<MenuViewModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuViewModel> menus) {
        this.menus = menus;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }
}