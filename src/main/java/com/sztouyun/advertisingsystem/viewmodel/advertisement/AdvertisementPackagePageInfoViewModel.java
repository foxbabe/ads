package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class AdvertisementPackagePageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "套餐名称)")
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
