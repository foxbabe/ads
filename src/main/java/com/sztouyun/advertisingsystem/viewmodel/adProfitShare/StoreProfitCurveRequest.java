package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class StoreProfitCurveRequest {
    @ApiModelProperty(value = "门店ID", required = true)
    @NotBlank(message = "门店ID不能为空")
    private String storeId;

    @ApiModelProperty(value = "开始日期(yyyy-MM-dd)", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM-dd)", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "结束日期不能为空")
    private Date endDate;
}
