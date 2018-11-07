package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.ITreeList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szty on 2017/7/26.
 */
@ApiModel
public class MenuViewModel extends CreateMenuViewModel implements ITreeList<MenuViewModel> {

    @ApiModelProperty(value = "菜单id",required = true)
    @NotBlank(message = "菜单id不能为空")
    private String id;

    @ApiModelProperty(value = "子菜单集合",hidden = true)
    private List<MenuViewModel> children = new ArrayList<>();

    @ApiModelProperty(value = "节点类型, 1: 按钮, 2: 菜单 ")
    private Integer nodeType;

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MenuViewModel> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<MenuViewModel> children) {
        this.children = children;
    }
}
