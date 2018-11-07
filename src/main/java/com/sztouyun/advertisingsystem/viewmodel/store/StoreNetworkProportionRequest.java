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
public class StoreNetworkProportionRequest {
    @ApiModelProperty(value = "日期, 格式: yyyy-MM-dd", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD)
    @NotNull(message = "日期不能为空")
    private Date date;
}
