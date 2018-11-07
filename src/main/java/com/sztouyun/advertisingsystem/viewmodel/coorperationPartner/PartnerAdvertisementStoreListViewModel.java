package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementStoreListViewModel{

    @ApiModelProperty(value = "合作方广告门店id")
    private String id;

    @ApiModelProperty(value = "门店主键")
    private String storeId;

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

    @ApiModelProperty(value = "广告时长")
    private Integer duration;

    @ApiModelProperty(value = "广告请求时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date requestDateTime;

    @ApiModelProperty(value = "广告完成时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "门店广告状态名称")
    private String advertisementStoreStatusName;

    @ApiModelProperty(value = "门店广告状态")
    private Integer advertisementStoreStatus;

    @ApiModelProperty(value = "是否有效")
    private boolean valid;

    @ApiModelProperty(value = "展示次数")
    private Integer displayTimes=0;

    @ApiModelProperty(value = "有效展示次数")
    private Integer validDisplayTimes=0;

    @ApiModelProperty(hidden = true)
    private String provinceId;

    @ApiModelProperty(hidden = true)
    private String cityId;

    @ApiModelProperty(hidden = true)
    private String regionId;

    @ApiModelProperty(hidden = true)
    private Integer advertisementPositionCategory;

    @ApiModelProperty(hidden = true)
    private Long requestTime;

    @ApiModelProperty(hidden = true)
    private Long finishTime;

    @ApiModelProperty(hidden = true)
    private Long takeOffTime;

    @ApiModelProperty(value = "开始播放时间Long型", hidden = true)
    private Long startTime;

    @ApiModelProperty(value = "开始播放时间Date型")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date startPlayTime;


}
