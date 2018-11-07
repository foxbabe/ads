package com.sztouyun.advertisingsystem.viewmodel.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.order.OrderOperationStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by wenfeng on 2018/2/8.
 */
@ApiModel
@Data
public class OrderOperationLogsRequest extends BasePageInfo{
    @ApiModelProperty(value = "订单ID")
    @NotBlank(message = "订单ID不能为空")
    private String id;

    @ApiModelProperty(value = "操作作态")
    @EnumValue(enumClass = OrderOperationStatusEnum.class)
    private Integer operationStatus;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

}
