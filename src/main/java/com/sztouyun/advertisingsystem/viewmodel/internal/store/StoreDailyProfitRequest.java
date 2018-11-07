package com.sztouyun.advertisingsystem.viewmodel.internal.store;

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
public class StoreDailyProfitRequest {

    @ApiModelProperty(value = "门店编号",required = true)
    @NotBlank(message = "门店编号不能为空")
    private String storeNo ;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;
}
