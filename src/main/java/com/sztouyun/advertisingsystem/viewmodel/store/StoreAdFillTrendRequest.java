package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class StoreAdFillTrendRequest {
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    @NotNull(message = "开始日期不能为空")
    private Date beginDate;

    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD)
    @NotNull(message = "结束日期不能为空")
    private Date endDate;
}
