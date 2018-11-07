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
public class PartnerAdvertisementMonitorStoreListViewModel {

    @ApiModelProperty(value = "合作方广告门店id")
    private String id;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省份")
    private String provinceName;

    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "地区")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "投放位置")
    private String advertisementPositionTypeName;

    @ApiModelProperty(value = "价格(元/次)")
    private Integer price=0;

    @ApiModelProperty(value = "广告请求时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "广告结束时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "门店广告状态")
    private String advertisementStoreStatusName;

    @ApiModelProperty(value = "已展示次数")
    private Long displayTimes;

    @ApiModelProperty(value = "有效展示次数")
    private Long validDisplayTimes;

    @ApiModelProperty(value = "有效比例")
    private String validDisplayTimesRatio;

    @ApiModelProperty(value = "是否有效")
    private boolean valid;

    public String getValidDisplayTimesRatio() {
        return NumberFormatUtil.format(validDisplayTimes,displayTimes, Constant.RATIO_PATTERN);
    }
}
