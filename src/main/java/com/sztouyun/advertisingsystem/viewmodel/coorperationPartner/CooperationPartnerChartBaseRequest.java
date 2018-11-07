package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerChartBaseRequest {

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "排序",required = true)
    @EnumValue(enumClass = CooperationPartnerHistogramSortEnum.class,message = "排序类型不对")
    private Integer sortNumber;
}
