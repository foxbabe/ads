package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementMonitorDetailInfo {

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "广告Id")
    private String thirdPartId;

    @ApiModelProperty(value = "合作方")
    private String partnerName;

    @ApiModelProperty(value = "合作模式")
    private Integer cooperationPattern;

    @ApiModelProperty(value = "合作模式名称")
    private String cooperationPatternName;

    @ApiModelProperty(value = "合作模式时长")
    private String duration="";

    @ApiModelProperty(value = "投放门店数量")
    private Long storeCount;

    @ApiModelProperty(value = "监控状态名称")
    private String statusName;

    @ApiModelProperty(value = "监控状态")
    private Integer status;

    @ApiModelProperty(value = "监控开始时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "监控结束时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "监控周期")
    private String monitorPeriod;

    @ApiModelProperty(value = "展示次数")
    private Long totalDisplayTimes = 0L;

    @ApiModelProperty(value = "有效次数")
    private Long totalValidDisplayTimes = 0L;

    @ApiModelProperty(value = "请求次数")
    private Long requestTimes = 0L;

    @ApiModelProperty(value = "有效比例")
    private String totalValidDisplayTimesRatio;

    @ApiModelProperty(value = "维护人")
    private String ownerName;

    @ApiModelProperty(value = "维护人Id")
    private String ownerId;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    public String getTotalValidDisplayTimesRatio() {
        return NumberFormatUtil.format(totalValidDisplayTimes,requestTimes, Constant.RATIO_PATTERN);
    }
}
