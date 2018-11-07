package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.adProfitShare.SettledStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import java.util.Date;

/**
 *
 */
@ApiModel
public class SettledStoreManageRequest extends BasePageInfo {

    @ApiModelProperty(value = "结算月份")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date selDate;
    @ApiModelProperty(value = "总金额-上区间")
    @Min(value = 0,message = "上区间不能小于0")
    private Double settledAmountUp;
    @ApiModelProperty(value = "总金额-下区间")
    private Double settledAmountDown;
    @ApiModelProperty(value = "结算状态(1：全部、2：待结算、3：已结)")
    @EnumValue(enumClass = SettledStatusEnum.class,nullable = true,message = "请选择正确的结算状态")
    private Integer settleStatus;

    public Date getSelDate() {
        return selDate;
    }

    public void setSelDate(Date selDate) {
        this.selDate = selDate;
    }

    public Double getSettledAmountUp() {
        return settledAmountUp;
    }

    public void setSettledAmountUp(Double settledAmountUp) {
        this.settledAmountUp = settledAmountUp;
    }

    public Double getSettledAmountDown() {
        return settledAmountDown;
    }

    public void setSettledAmountDown(Double settledAmountDown) {
        this.settledAmountDown = settledAmountDown;
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }
}
