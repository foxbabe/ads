package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class ProfitOverviewRequest {
    @ApiModelProperty(value = "开始年月(yyyy-MM)", required = true)
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM)", required = true)
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    @NotNull(message = "结束日期不能为空")
    private Date endDate;
}
