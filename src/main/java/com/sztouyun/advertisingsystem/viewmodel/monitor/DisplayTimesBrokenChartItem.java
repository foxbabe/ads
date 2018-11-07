package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class DisplayTimesBrokenChartItem {
    @ApiModelProperty("日期(yyyy-MM-dd)")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty("展示次数")
    private Long displayTimes;

    @ApiModelProperty("展示门店数量")
    private Long storeNum;

    @ApiModelProperty("收银机展示次数")
    private Long cashRegisterDisplayTimes;
    @ApiModelProperty("收银机展示占比")
    private String cashRegisterProportion;

    @ApiModelProperty("iOS展示次数")
    private Long iosDisplayTimes;
    @ApiModelProperty("iOS展示占比")
    private String iosProportion;

    @ApiModelProperty("Android展示次数")
    private Long androidDisplayTimes;
    @ApiModelProperty("Android展示占比")
    private String androidProportion;

    @ApiModelProperty("待机全屏广告展示次数")
    private Long fullScreenDisplayTimes;
    @ApiModelProperty("待机全屏广告展示占比")
    private String fullScreenProportion;

    @ApiModelProperty("扫描支付页面展示次数")
    private Long scanPayDisplayTimes;
    @ApiModelProperty("扫描支付页面展示占比")
    private String scanPayProportion;

    @ApiModelProperty("商家待机全屏展示次数")
    private Long sellerFullScreenDisplayTimes;
    @ApiModelProperty("商家待机全屏展示占比")
    private String sellerFullScreenProportion;

    @ApiModelProperty("商家Banner展示次数")
    private Long businessBannerDisplayTimes;
    @ApiModelProperty("商家Banner展示占比")
    private String businessBannerProportion;

    @ApiModelProperty("Android端App开屏展示次数")
    private Long androidAppStartingUpDisplayTimes;
    @ApiModelProperty("Android端App开屏展示占比")
    private String androidAppStartingUpProportion;

    @ApiModelProperty("Android端Banner展示次数")
    private Long androidAppBannerDisplayTimes;
    @ApiModelProperty("Android端Banner展示占比")
    private String androidAppBannerProportion;

    @ApiModelProperty("iOS端App开屏展示次数")
    private Long iosAppStartingUpDisplayTimes;
    @ApiModelProperty("iOS端App开屏展示占比")
    private String iosAppStartingUpProportion;

    @ApiModelProperty("iOS端AppBanner展示次数")
    private Long iosAppBannerDisplayTimes;
    @ApiModelProperty("iOS端AppBanner展示占比")
    private String iosAppBannerProportion;
}
