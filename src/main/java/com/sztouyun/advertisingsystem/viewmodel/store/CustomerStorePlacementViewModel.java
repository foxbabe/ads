package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/5/17.
 */
@Data
@ApiModel
public class CustomerStorePlacementViewModel extends StorePlacementViewModel {
    @ApiModelProperty(value = "是否选中")
    private Boolean isChoose;

}
