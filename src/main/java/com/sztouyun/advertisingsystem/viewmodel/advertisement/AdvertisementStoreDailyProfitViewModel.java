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
public class AdvertisementStoreDailyProfitViewModel{

    @ApiModelProperty(value = "门店表结构ID")
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

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value="分成时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value="分成金额")
    private Double shareAmount;

    private Long date;

    public Date getProfitDate() {
        return new Date(date);
    }

    public String getShareAmount() {
        return NumberFormatUtil.format(shareAmount/100.0, Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
