package com.sztouyun.advertisingsystem.viewmodel.internal.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class SupplierRequest extends BaseCommodityRequest{
    @ApiModelProperty(value = "供应商code",required =true)
    @NotEmpty(message = "供应商code不允许为空")
    private String code;
}
