package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementTaskViewModel {

    @ApiModelProperty(value = "广告ID")
    private String id;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "广告类型")
    private Integer advertisementType;

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告状态",hidden = true)
    private Integer advertisementStatus;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "投放门店数量")
    private Integer storeCount;

    @ApiModelProperty(value = "是否开启分成")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "广告客户")
    private String customerName;

    @ApiModelProperty(value = "客户Id",hidden = true)
    private String customerId;

    @ApiModelProperty(value = "是否异常")
    private boolean abnormal;

    @ApiModelProperty(value = "广告任务数量")
    private Integer taskCount;

    @ApiModelProperty(value = "完成任务数量")
    private Integer finishedTaskCount;

    @ApiModelProperty(value = "完成任务比例")
    private String finishedTaskCountRatio;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "维护人Id",hidden = true)
    private String ownerId;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    public String getFinishedTaskCountRatio() {
        return NumberFormatUtil.format(finishedTaskCount.longValue(), taskCount.longValue(), Constant.RATIO_PATTERN);
    }
}
