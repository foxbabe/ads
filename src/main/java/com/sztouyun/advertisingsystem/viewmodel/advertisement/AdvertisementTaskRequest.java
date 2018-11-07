package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementTaskRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告状态(4:投放中 5:已下架 6:已完成)")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = Constant.DATETIME,timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = Constant.DATETIME,timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "广告名称)")
    private String advertisementName;

    @ApiModelProperty(value = "业务员名称")
    private String nickname;

    @ApiModelProperty(value = "是否异常")
    private Boolean abnormal;

    @ApiModelProperty(value = "权限过滤",hidden = true)
    private String authenticationSql;

}
