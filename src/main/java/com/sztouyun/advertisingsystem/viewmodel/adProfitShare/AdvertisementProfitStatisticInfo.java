package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AdvertisementProfitStatisticInfo {

    @ApiModelProperty(value = "广告ID")
    private String advertisementId;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "广告类型")
    private Integer advertisementType;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "实际投放开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "实际投放截止时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "是否开启分成")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "维护人id")
    private String ownerId;

    @ApiModelProperty(value = "投放平台")
    private String terminalNames;

    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "创建者ID")
    private String creatorId;

}
