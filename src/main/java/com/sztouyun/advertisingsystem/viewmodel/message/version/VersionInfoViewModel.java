package com.sztouyun.advertisingsystem.viewmodel.message.version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class VersionInfoViewModel {
    @ApiModelProperty(value = "版本信息ID")
    private String id;

    @ApiModelProperty(value = "版本号")
    private String versionNumber;

    @ApiModelProperty(value = "版本内容")
    private String versionContent;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean isTip;

    @JsonFormat(pattern = Constant.DATE_TIME_CN)
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;
}
