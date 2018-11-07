package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
public class ContractOperationLogItem {
    @ApiModelProperty(value = "操作(1：提交审核 ，2：审核通过，3：审核驳回，4，签约成功，5: 签约失败，6：执行中，7:待执行,8:提交终止审核,9:终止合同,10.终止驳回，11.合同完成，12:待提交")
    private Integer operationStatus;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = Constant.TIME_HOUR, timezone = "GMT+8")
    private Date operateTime;

    @ApiModelProperty(value = "操作状态名称")
    private String operationStatusName;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "备注")
    private String remark;



    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
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

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getOperationStatusName() {
        return operationStatusName;
    }

    public void setOperationStatusName(String operationStatusName) {
        this.operationStatusName = operationStatusName;
    }
}
