package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class UpdateAdvertisementDisplayTimesConfigViewModel extends BaseAdvertisementDisplayTimesConfigViewModel {
    @ApiModelProperty(value = "类别价格配置ID", required = true)
    @NotBlank(message = "类别价格配置ID不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
