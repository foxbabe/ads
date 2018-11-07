package com.sztouyun.advertisingsystem.viewmodel.account;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleViewModel {
    private String id;

    private String roleName;

    private String description;

    private boolean enableFlag;

    private Integer roleType;

    private List<String> permissionUrls = new ArrayList<>();
}
