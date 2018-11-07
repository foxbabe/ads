package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerPieChartRequest {

    @ApiModelProperty(value = "合作方Id")
    @NotBlank(message = "合作方Id不能为空")
    private String cooperationPartnerId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;
}
