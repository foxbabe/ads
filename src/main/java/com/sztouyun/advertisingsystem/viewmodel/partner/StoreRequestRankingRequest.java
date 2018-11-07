package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class StoreRequestRankingRequest {
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date beginDate;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date endDate;
}
