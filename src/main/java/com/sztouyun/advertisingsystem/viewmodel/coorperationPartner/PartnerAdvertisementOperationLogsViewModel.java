package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementOperationLogsViewModel{

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = Constant.TIME_SECOND, timezone = "GMT+8")
    private Date operateTime;

    @ApiModelProperty(value = "操作状态名称")
    private String operationStatusName;

    @ApiModelProperty(value = "操作状态")
    private Integer operationStatus;


}
