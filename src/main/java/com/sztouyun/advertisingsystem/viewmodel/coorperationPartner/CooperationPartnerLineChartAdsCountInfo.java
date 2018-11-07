package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerLineChartAdsCountInfo {

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty("请求广告数量")
    private long requestAdvertisementCount=0;

    @ApiModelProperty("已展示广告数量")
    private long displayAdvertisementCount=0;

    @ApiModelProperty("已展示广告占比")
    private String displayAdvertisementRatio;

    @ApiModelProperty("未展示广告数量")
    private long noDisplayAdvertisementCount=0;

    @ApiModelProperty("未展示广告占比")
    private String noDisplayAdvertisementRatio;

    public long getNoDisplayAdvertisementCount() {
        return getRequestAdvertisementCount()-getDisplayAdvertisementCount();
    }

    public String getDisplayAdvertisementRatio() {
        return NumberFormatUtil.format(getDisplayAdvertisementCount(),getRequestAdvertisementCount(),Constant.RATIO_PATTERN);
    }

    public String getNoDisplayAdvertisementRatio() {
        return NumberFormatUtil.format(getNoDisplayAdvertisementCount(),getRequestAdvertisementCount(),Constant.RATIO_PATTERN);
    }

    public CooperationPartnerLineChartAdsCountInfo(Date date) {
        this.date = date;
    }

    public CooperationPartnerLineChartAdsCountInfo() {
    }
}
