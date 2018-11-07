package com.sztouyun.advertisingsystem.viewmodel.organization;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrganizationPageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "模糊查询组织机构名称")
    private String name;

    @ApiModelProperty(value = "当前选中的组织机构id, 用在编辑组织机构显示所有组织机构列表中", required = false)
    private String currentId;

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
