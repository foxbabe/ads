package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementTaskDetailViewModel {

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建人Id")
    private String creatorId;

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

    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "实际已投放天数")
    private Integer period;

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

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "维护人Id",hidden = true)
    private String ownerId;

    @ApiModelProperty(value = "客户头像")
    private String portrait;

    public String getPeriod() {
        return endTime==null?"":DateUtils.formateYmd(DateUtils.getIntervalDays(startTime,endTime));
    }
}
