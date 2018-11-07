package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class AdvertisementOperationViewModel {

    @ApiModelProperty(value = "广告Id", required = true)
    @NotBlank(message = "广告ID不能为空！")
    private String advertisementId;

    @ApiModelProperty(value = "操作（1：提交 ，2：审核，3： 投放，4:完成（失败表示下架，成功表示投放完成））", required = true)
    @EnumValue(enumClass = AdvertisementStatusEnum.class,message = "操作类型不正确！")
    @NotNull(message = "操作不能为空！")
    private Integer operation;

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean successed;

    @ApiModelProperty(value = "是否合同广告投放完成")
    private boolean finishContract = false;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000,message ="备注太长" )
    private String remark;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public boolean isFinishContract() {
        return finishContract;
    }

    public void setFinishContract(boolean finishContract) {
        this.finishContract = finishContract;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
