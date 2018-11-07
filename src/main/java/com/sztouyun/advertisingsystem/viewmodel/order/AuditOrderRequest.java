package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.order.OrderOperationEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 *
 */
@Data
@ApiModel
public class AuditOrderRequest {
    @ApiModelProperty(value = "订单ID",required = true)
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @ApiModelProperty(value = "是否成功", required = true)
    private boolean successed=true;

    @ApiModelProperty(value = "审核备注")
    @Size(max = 2000, message = "备注太长")
    private String remark;

}
