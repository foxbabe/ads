package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AdPositionProportion {
    @ApiModelProperty("广告位置枚举值: 1-待机全屏广告, 2-扫描支付页面, 3-商家待机全屏, 4-商家Banner, 5-Android端App开屏, 6-Android端Banner, 7-iOS端App开屏, 8-iOS端AppBanner")
    private Integer advertisementPositionCategory;
    @ApiModelProperty("广告位名称")
    private String adPositionName;
    @ApiModelProperty("已展示次数")
    private Integer displayTimes;
    @ApiModelProperty("已展示次数占比")
    private String proportion;// 占比 = 单个广告位已展示次数 / 所有广告位已展示次数之和
}
