package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2017/11/15.
 */
@Data
@ApiModel
public class AdvertisementStoreInfoItem {
    @ApiModelProperty(value = "广告ID")
    private String id;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "广告客户")
    private String customerName;

    @ApiModelProperty(value = "投放平台")
    private String terminalNames;

    @ApiModelProperty(value = "广告类型")
    private String advertisementTypeName;

    @ApiModelProperty(value = "是否开启分成")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "广告状态")
    private String advertisementStatusName;

    @ApiModelProperty(value = "是否激活")
    private Boolean active;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "能否查看详情")
    private Boolean canView;
}
