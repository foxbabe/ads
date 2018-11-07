package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class UpdateAdvertisementSizeConfigViewModel extends BaseAdvertisementSizeConfigViewModel{

    @ApiModelProperty(value = "广告尺寸id", required = true)
    @NotBlank(message = "广告尺寸id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
