package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class ClickOrScanTimesTrendRequest {
    @NotNull(message = "广告ID不能为空")
    @ApiModelProperty(value = "广告ID", required = true)
    private String advertisementId;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date beginDate;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endDate;
}
