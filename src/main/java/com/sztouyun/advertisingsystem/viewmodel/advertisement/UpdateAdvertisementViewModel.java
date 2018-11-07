package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2017/8/4.
 */
@Data
@ApiModel
public class UpdateAdvertisementViewModel extends CreateAdvertisementViewModel {
    @ApiModelProperty(value = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String id;
}
