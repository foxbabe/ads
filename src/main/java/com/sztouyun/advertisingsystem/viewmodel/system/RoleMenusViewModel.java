package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szty on 2017/8/1.
 */
@ApiModel
public class RoleMenusViewModel extends CreateMenuViewModel implements ITreeList<RoleMenusViewModel> {
    @ApiModelProperty(value = "是否选中")
    private boolean checked;

    @ApiModelProperty(value = "菜单id", required = true)
    @NotBlank(message = "菜单id不能为空")
    private String id;

    @ApiModelProperty(value = "子菜单集合", hidden = true)
    private List<RoleMenusViewModel> children = new ArrayList<>();

    @ApiModelProperty(value = "节点类型, 1: 按钮, 2: 菜单 ")
    private Integer nodeType;

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RoleMenusViewModel> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<RoleMenusViewModel> children) {
        this.children = children;
    }
}
