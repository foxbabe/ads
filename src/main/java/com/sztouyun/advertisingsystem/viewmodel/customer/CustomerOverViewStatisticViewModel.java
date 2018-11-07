package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.RoundingMode;

@Data
@NoArgsConstructor
public class CustomerOverViewStatisticViewModel {

    @ApiModelProperty(value = "广告客户总数")
    private Long totalCustomerCount;

    @ApiModelProperty(value = "已签约客户总数")
    private Long signCount;

    @ApiModelProperty(value = "已投放广告客户总数")
    private Long deliveryCount;

    @ApiModelProperty(value = "总收益")
    private Double totalProfit;

    @ApiModelProperty(value = "签约客户比例")
    private String signRatio;

    @ApiModelProperty(value = "投放客户比例")
    private String deliveryRatio;

    public String getSignRatio() {
        return NumberFormatUtil.format(signCount,totalCustomerCount, Constant.RATIO_PATTERN);
    }

    public String getDeliveryRatio() {
        return NumberFormatUtil.format(deliveryCount,totalCustomerCount, Constant.RATIO_PATTERN);
    }

    public String getTotalProfit() {
        return NumberFormatUtil.format(totalProfit, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
