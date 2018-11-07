package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;
import java.util.Date;

@ApiModel
@Data
public class AdvertisementSettlementListViewModel{

    @ApiModelProperty(value = "广告结算ID")
    private String id;

    @ApiModelProperty(value = "门店数量")
    private Integer storeCount;

    @ApiModelProperty(value = "分成金额")
    private Long shareAmount=0L;

    @ApiModelProperty(value = "是否结算")
    private boolean settled;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "结算时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date settledTime;

    @ApiModelProperty(value = "结算人")
    private String operator;

    public String getShareAmount() {
        return NumberFormatUtil.format(shareAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
