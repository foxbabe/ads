package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationStatusEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.var;

import java.util.Date;

@ApiModel
public class AdvertisementOperationInfoViewModel {

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = Constant.TIME_HOUR, timezone = "GMT+8")
    private Date operateTime;

    @ApiModelProperty(value = "操作状态(1:提交审核,2:审核通过,3:审核驳回,4:投放中,5:已下架,6:广告完成,7:待提交)")
    @EnumValue(enumClass = AdvertisementOperationStatusEnum.class,message = "广告操作状态不匹配")
    private Integer operationStatus;

    @ApiModelProperty(value = "操作状态名称")
    private String operationStatusName;

    public void setOperationStatus(Integer operation,boolean successed) {
        var advertisementOperationStatusEnums = EnumUtils.getAllItems( AdvertisementOperationStatusEnum.class);
        for(AdvertisementOperationStatusEnum operationStatusEnum:advertisementOperationStatusEnums){
            if(operation.equals(operationStatusEnum.getOperation())){
                if(operationStatusEnum.getSuccessed()==successed) {
                    this.operationStatus = operationStatusEnum.getValue();
                    setOperationStatusName(operationStatusEnum.getDisplayName());
                    return;
                }
            }
        }
    }

    public String getOperationStatusName() {
        return operationStatusName;
    }

    public void setOperationStatusName(String operationStatusName) {
        this.operationStatusName = operationStatusName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }
}
