package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.account.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> getAccessibleRoles(String roleId);
}
