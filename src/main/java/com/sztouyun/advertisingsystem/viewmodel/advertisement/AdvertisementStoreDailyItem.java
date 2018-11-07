package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.RoundingMode;
import java.util.Date;

@ApiModel
@Data
public class AdvertisementStoreDailyItem {

    @ApiModelProperty(value = "日收益表ID")
    private String id;

    @ApiModelProperty(value = "广告ID")
    private String advertisementId;

    @ApiModelProperty(value = "结算ID")
    private String settlementId;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "分成时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value = "分成金额")
    private Long shareAmount = 0L;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "分成日期Long")
    private Long date;

    private Boolean isCheck = false;

    public String getShareAmount() {
        return NumberFormatUtil.format(shareAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }

}
