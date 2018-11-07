package com.sztouyun.advertisingsystem.viewmodel.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.web.security.UrlGrantedAuthority;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserViewModel implements UserDetails {
    private String id;

    private String username;

    private String nickname;

    private String password;

    private boolean enableFlag;

    private boolean isAdmin;

    protected String organizationId;

    private String customerId;

    private String headPortrait;

    private String roleId;

    private RoleViewModel role;

    @JsonIgnore
    public RoleTypeEnum getRoleTypeEnum() {
        if (isAdmin())
            return RoleTypeEnum.Admin;

        RoleViewModel role = getRole();
        if (role == null)
            return RoleTypeEnum.SaleMan;
        return EnumUtils.toEnum(role.getRoleType(), RoleTypeEnum.class);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        } else if(role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + getRoleTypeEnum().toString()));
            for (String permissionUrl:role.getPermissionUrls()){
                authorities.add(new UrlGrantedAuthority(permissionUrl));
            }
        }
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnableFlag();
    }
}
