package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementMonitorCountStatisticRequest {

    @ApiModelProperty(value = "合作方ID, 选择 '全部' 则不要传入这个字段")
    private String cooperationPartnerId;

    @ApiModelProperty(value = "合作模式")
    @EnumValue(enumClass = CooperationPatternEnum.class,message = "合作模式不匹配",nullable = true)
    private Integer cooperationPattern;

    @ApiModelProperty(value = "广告类型")
    @EnumValue(enumClass = MaterialTypeEnum.class,message = "广告类型不匹配",nullable = true)
    private Integer materialType;

    @ApiModelProperty(value = "开始监控时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束监控时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "广告Id")
    private String thirdPartId;

    @ApiModelProperty(value = "业务员")
    private String nickname;

    @ApiModelProperty(value = "权限过滤",hidden = true)
    private String authenticationSql;
}
