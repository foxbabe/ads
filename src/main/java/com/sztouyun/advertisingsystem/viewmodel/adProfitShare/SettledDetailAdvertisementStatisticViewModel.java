package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;

@Data
public class SettledDetailAdvertisementStatisticViewModel {

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "分成标准")
    private Double shareStandard;

    @ApiModelProperty(value = "分成标准单位")
    private Integer shareStandardUnit;

    @ApiModelProperty(value = "结算金额")
    private Double settledAmount;

    @ApiModelProperty(value = "总的结算金额")
    private Double totalSettledAmount;

    @ApiModelProperty(value = "结算金额占比")
    private String settledAmountRatio;

    public String getAdvertisementStatusName() {
        return EnumUtils.getDisplayName(advertisementStatus, AdvertisementStatusEnum.class);
    }

    public String getShareStandardUnit() {
        return EnumUtils.getDisplayName(shareStandardUnit, UnitEnum.class);
    }

    public String getSettledAmount() {
        return NumberFormatUtil.format(settledAmount, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getTotalSettledAmount() {
        return NumberFormatUtil.format(totalSettledAmount, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getSettledAmountRatio() {
        return NumberFormatUtil.format(settledAmount,totalSettledAmount, Constant.RATIO_PATTERN);
    }
}
