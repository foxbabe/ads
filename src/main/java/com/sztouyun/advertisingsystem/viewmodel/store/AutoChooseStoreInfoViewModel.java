package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class AutoChooseStoreInfoViewModel extends StoreInfoPageInfoViewModel {
    @ApiModelProperty(value = "自动选择记录数", required = true)
    @Min(value=1,message = "自动选择记录数不能小于0")
    @NotNull(message = "自动选择记录数不能为空")
    private Integer recordCount;

}
