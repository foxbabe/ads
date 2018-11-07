package com.sztouyun.advertisingsystem.viewmodel;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2018/1/12.
 */
@ApiModel
public class AreaRequest extends BasePageInfo {
    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    public String getProvinceId() {
        if("0".equals(provinceId))
            return "";
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        if("0".equals(cityId))
            return "";
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getRegionId() {
        if("0".equals(regionId))
            return "";
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
