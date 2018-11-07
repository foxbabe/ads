package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel
public class AdvertisementProfitListViewModel {

    @ApiModelProperty(value = "广告ID")
    private String id;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "投放平台")
    private String terminalNames;

    @ApiModelProperty(value = "广告类型")
    private Integer advertisementType;

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "实际开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "实际已投放天数")
    private String period;

    @ApiModelProperty(value = "是否开启分成")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "分成标准金额")
    private Double profitShareStandardAmount;

    @ApiModelProperty(value = "分成标准金额单位")
    private Integer profitShareStandardAmountUnit;

    @ApiModelProperty(value = "分成标准金额单位名称")
    private String profitShareStandardAmountUnitName;

    @ApiModelProperty(value = "分成金额")
    private String shareAmount;

    @ApiModelProperty(value = "已结算金额")
    private String settledAmount;

    @ApiModelProperty(value = "未结算金额")
    private String unsettledAmount;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "导出的分成标准",hidden = true)
    private String exportProfitShareStandardAmount;

    public String getExportProfitShareStandardAmount() {
        if(profitShareStandardAmount==null)
            return "";
        return profitShareStandardAmount+profitShareStandardAmountUnitName;
    }
}
