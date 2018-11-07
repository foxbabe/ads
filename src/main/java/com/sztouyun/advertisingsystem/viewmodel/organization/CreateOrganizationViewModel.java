package com.sztouyun.advertisingsystem.viewmodel.organization;

import com.sztouyun.advertisingsystem.model.system.Organization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Size;

@ApiModel
public class CreateOrganizationViewModel {

    @ApiModelProperty(value = "父亲节点id, 根节点请输入'0'", required = true)
    @Size(max = 36,message ="父亲节点id太长" )
    private String parentId;

    @ApiModelProperty(value = "组织机构名称", required = true)
    @Size(max = 128,message ="组织机构名称太长" )
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "负责人id", required = true)
    @NotBlank(message = "负责人Id不能为空")
    private String ownerId;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        if(StringUtils.isEmpty(parentId))
            this.parentId = Organization.ROOT_PARENT_ID;
        else
            this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
