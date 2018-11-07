package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2018/2/5.
 */
@ApiModel
@Data
public class DailyDeliveryStoreStatisticRequest extends BasePageInfo {
    @ApiModelProperty(value = "订单ID",required = true)
    @NotBlank(message = "订单ID不能为空")
    private String id;
}
