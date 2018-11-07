package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;
import java.util.Date;


@Data
@ApiModel
public class PartnerProfitStreamViewModel {

    @ApiModelProperty("收益时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty("有效次数")
    private Long validTimes;

    @ApiModelProperty("计费模式")
    private String chargingPatternName;

    @ApiModelProperty("计费单价")
    private String unitPrice;

    @ApiModelProperty(hidden = true)
    private Double unitPriceDigit;

    @ApiModelProperty(hidden = true)
    private Long profitAmountDigit;

    @ApiModelProperty(hidden = true)
    private String partnerId;


    @ApiModelProperty("计费单位")
    private Integer unit;

    @ApiModelProperty("计费单位名称")
    private String unitName;

    @ApiModelProperty("收益金额")
    public String getProfitAmount() {
        return NumberFormatUtil.format(profitAmountDigit.doubleValue() / 100D, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getUnitPrice() {
        return NumberFormatUtil.format(unitPriceDigit, Constant.SCALE_TWO);
    }


}
