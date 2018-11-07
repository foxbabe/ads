package com.sztouyun.advertisingsystem.viewmodel.internal.commodity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class BaseCommodityRequest {
    @ApiModelProperty(value = "ID",required = true)
    @NotNull(message = "ID不允许为空")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;
}
