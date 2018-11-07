package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
@ApiModel
public class PartnerAdvertisementOperationLogsRequest extends BasePageInfo {
    @ApiModelProperty(value = "广告ID")
    @NotBlank(message = "广告ID不能为空")
    private String id;

    @ApiModelProperty(value = "操作作态")
    private Integer operationStatus;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;
}
