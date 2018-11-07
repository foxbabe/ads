package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.model.system.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by szty on 2017/7/26.
 */
@ApiModel
public class CreateMenuViewModel {

    @ApiModelProperty(value = "菜单名称",required = true)
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 128,message = "菜单名称太长")
    private String menuName;
    @ApiModelProperty(value = "父菜单id")
    private String parentId;
    @ApiModelProperty(value = "菜单地址",required = true)
    @NotBlank(message = "菜单地址不能为空")
    @Size(max = 255,message = "菜单地址太长")
    private String url;
    @ApiModelProperty(value = "菜单序号",required = true)
    @Min(value = 0)
    private Double sortNumber;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        if(StringUtils.isEmpty(parentId))
            this.parentId = Menu.ROOT_PARENT_ID;
        else
            this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Double sortNumber) {
        this.sortNumber = sortNumber;
    }
}
