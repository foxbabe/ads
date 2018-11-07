package com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class CustomerVisitListItemViewModel extends UpdateCustomerVisitViewModel{

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "能否编辑")
    private Boolean canEdit=true;

    @ApiModelProperty(value = "拜访人")
    private String visitor;

    @ApiModelProperty(value = "客户拜访创建人")
    private String creator;

    @ApiModelProperty(value = "拜访时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date visitTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public Date getVisitTime() {
        return visitTime;
    }

    @Override
    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }
}
