package com.sztouyun.advertisingsystem.viewmodel.organization;

import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class OrganizationTreeViewModel extends OrganizationViewModel implements ITreeList<OrganizationTreeViewModel> {

    @ApiModelProperty(value = "下级组织结构")
    private List<OrganizationTreeViewModel> children = new ArrayList<>();

    @ApiModelProperty(value = "负责人名称")
    private String ownerName;

    @ApiModelProperty(value = "父亲节点名称")
    private String parentOrganizationName;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getParentOrganizationName() {
        return parentOrganizationName;
    }

    public void setParentOrganizationName(String parentOrganizationName) {
        this.parentOrganizationName = parentOrganizationName;
    }

    public List<OrganizationTreeViewModel> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationTreeViewModel> children) {
        this.children = children;
    }
}
