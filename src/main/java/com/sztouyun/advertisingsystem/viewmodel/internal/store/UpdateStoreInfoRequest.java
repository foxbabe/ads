package com.sztouyun.advertisingsystem.viewmodel.internal.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2018/3/12.
 */
@Data
@ApiModel
public class UpdateStoreInfoRequest {
    @ApiModelProperty(value = "旧门店编号",required = true)
    @NotBlank(message = "旧门店编号不能为空")
    private String oldStoreNo ;
    @ApiModelProperty(value = "新门店编号",required = true)
    @NotBlank(message = "新门店编号不能为空")
    private String newStoreNo;
}
