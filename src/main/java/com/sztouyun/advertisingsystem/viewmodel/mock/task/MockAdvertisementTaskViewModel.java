package com.sztouyun.advertisingsystem.viewmodel.mock.task;

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
public class MockAdvertisementTaskViewModel {
    @ApiModelProperty(name = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;

    @ApiModelProperty(name = "开始时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(name = "结束时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;
}
