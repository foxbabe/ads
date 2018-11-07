package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.DateRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wenfeng on 2018/3/8.
 */
@ApiModel
@Data
public class PartnerOrderDailyDeliveryStatisticRequest {
    @ApiModelProperty(value = "订单ID",required = true)
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @ApiModelProperty(value = "开始时间",required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "开始时间不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束时间",required = true)
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    @NotNull(message = "结束时间不能为空")
    private Date endDate;
}
