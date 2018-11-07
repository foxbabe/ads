package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class MaterialLinkMonitorViewModel implements Comparable<MaterialLinkMonitorViewModel> {

    @ApiModelProperty(value = "用于导出手机号的广告素材链接步骤ID")
    private String materialUrlStepId;

    @ApiModelProperty(value = "广告投放位置; 1:待机全屏广告; 2:扫描支付页面; 3:商家待机全屏; 4:商家Banne;")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "广告投放位置; 1:待机全屏广告; 2:扫描支付页面; 3:商家待机全屏; 4:商家Banne;")
    private String advertisementPositionTypeName;

    @ApiModelProperty(value = "链接方式; 1:Url点击; 2:二维码;")
    private Integer linkType;

    @ApiModelProperty(value = "链接方式; 1:Url点击; 2:二维码;")
    private String linkTypeName;

    @ApiModelProperty(value = "Url点击次数")
    private Long urlClickTimes=0L;

    @ApiModelProperty(value = "二维码扫码次数")
    private Long qRCodeTimes=0L;

    @ApiModelProperty(value = "成功打开手机号页面次数")
    private Long phonePageSucceedTimes=0L;

    @ApiModelProperty(value = "成功打开推广页面次数")
    private Long promotionPageSucceedTimes=0L;

    @ApiModelProperty(value = "手机号数量")
    private Integer phoneCount=0;

    @ApiModelProperty(value = "页面转化率")
    private String pageConversionRatio="0%";

    @ApiModelProperty(value = "手机号转化率")
    private String phonePageConversionRatio="0%";

    @Override
    public int compareTo(MaterialLinkMonitorViewModel o) {
        int i = this.getAdvertisementPositionType() - o.getAdvertisementPositionType();
        if(i==0)
            return this.getLinkType()-o.getLinkType();
        return i;
    }
}