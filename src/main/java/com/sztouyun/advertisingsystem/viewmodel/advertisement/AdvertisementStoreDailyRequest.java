package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class AdvertisementStoreDailyRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String advertisementId;

    @ApiModelProperty(value = "结算ID", required = true)
    @NotBlank(message = "结算ID不能为空")
    private String settlementId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "区域ID列表：逗号分隔")
    private String areaIds;

    @ApiModelProperty(hidden = true)
    private List<String> storeIds;
}
