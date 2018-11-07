package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
@ApiModel
public class ContractOperationLogRequest extends BasePageInfo{
    @ApiModelProperty(value = "合同Id", required = true)
    @NotBlank(message = "合同ID不能为空！")
    private String id;

    @ApiModelProperty(value = "操作（0:全部状态,1：提交审核 ，2：审核通过，3：审核驳回，4，签约成功，5: 签约失败，6：执行中，7:待执行,8:提交终止审核,9:终止合同,10.终止驳回，11.合同完成,12:待提交",required = true)
    @EnumValue(enumClass = ContractOperationStatusEnum.class)
    private Integer operationStatus;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;
}
