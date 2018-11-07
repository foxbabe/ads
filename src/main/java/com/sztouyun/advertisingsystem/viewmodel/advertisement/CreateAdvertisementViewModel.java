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
public class CreateAdvertisementViewModel extends BaseAdvertisementViewModel {
    @ApiModelProperty(value = "合同ID)")
    @NotBlank(message = "合同ID不允许为空")
    private String contractId;
}
