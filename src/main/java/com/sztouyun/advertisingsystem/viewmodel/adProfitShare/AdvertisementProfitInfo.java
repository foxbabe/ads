package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class AdvertisementProfitInfo {

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "实际开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "实际结束投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "分成标准金额")
    private Double profitShareStandardAmount;

    @ApiModelProperty(value = "分成标准金额单位")
    private Integer profitShareStandardAmountUnit;

    @ApiModelProperty(value = "分成标准金额单位名称")
    private String profitShareStandardAmountUnitName;

    @ApiModelProperty(value = "广告分成金额")
    private String shareAmount;

    @ApiModelProperty(value = "广告分成金额单位名称")
    private String shareAmountUnit="元";

}
