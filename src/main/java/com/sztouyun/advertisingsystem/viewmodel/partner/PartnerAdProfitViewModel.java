package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;

@Data
@ApiModel
public class PartnerAdProfitViewModel {

    @ApiModelProperty("广告位")
    private String adPositionName;

    @ApiModelProperty("有效次数")
    private Long validTimes;

    @ApiModelProperty(hidden = true)
    private Long profitAmountDigit;

    @ApiModelProperty(value = "广告位置种类", hidden = true)
    private Integer advertisementPositionCategory;

    @ApiModelProperty("收益金额")
    public String getProfitAmount() {
        return NumberFormatUtil.format(profitAmountDigit.doubleValue() / 100D, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

}
