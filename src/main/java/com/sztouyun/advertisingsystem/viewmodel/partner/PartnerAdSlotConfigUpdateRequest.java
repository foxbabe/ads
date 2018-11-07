package com.sztouyun.advertisingsystem.viewmodel.partner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@ApiModel
@Data
public class PartnerAdSlotConfigUpdateRequest {

    @ApiModelProperty(value = "选中的合作方广告位Id")
    @Min(value = 1,message = "选中的合作方广告位Id不能小于1")
    private long checkedId;

    @ApiModelProperty(value = "替换的合作方广告位优先级(ps:上移一层=checkedPriority-1,下移一层=checkedPriority+1,置于顶层=1,置于底层=totalElement)")
    @Min(value = 0,message = "优先级不能小于0")
    private Integer replacedPriority;
}
