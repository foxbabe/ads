package com.sztouyun.advertisingsystem.viewmodel.adPosition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class UpdateAdPositionViewModel extends CreateAdPositionViewModel {

    @ApiModelProperty(value = "类别广告位配置ID", required = true)
    @NotBlank(message = "类别广告位配置ID不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
