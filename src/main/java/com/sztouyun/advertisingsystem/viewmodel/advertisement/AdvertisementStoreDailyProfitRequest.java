package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class AdvertisementStoreDailyProfitRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告ID",required = true)
    private String id;

    @ApiModelProperty(hidden = true)
    private String contractId;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(hidden = true)
    private List<String> storeIds;

    public boolean hasStoreFilter(){
        return !StringUtils.isAllEmptyString(provinceId,cityId,regionId,storeName,deviceId,shopId);
    }

}
