package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;
import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerLineChartProfitTrendInfo {

    public CooperationPartnerLineChartProfitTrendInfo() {
    }

    public CooperationPartnerLineChartProfitTrendInfo(Date date) {
        this.date = date;
    }

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty("有效次数")
    private long validDisplayTimes=0;

    @ApiModelProperty("收益金额")
    private long profitAmount;

    @ApiModelProperty("待机全屏收益金额")
    private long fullScreenProfitAmount;

    @ApiModelProperty("扫面支付收益金额")
    private long scanPayProfitAmount;

    public String getProfitAmount() {
        return NumberFormatUtil.format(profitAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    @JsonIgnore
    public long getProfitAmountLong() {
        return profitAmount;
    }

    public String getFullScreenProfitAmount() {
        return NumberFormatUtil.format(fullScreenProfitAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

    public String getScanPayProfitAmount() {
        return NumberFormatUtil.format(scanPayProfitAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
