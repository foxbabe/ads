package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@ApiModel
@Data
public class CooperationPartnerConfigUpdateRequest {

    @ApiModelProperty(value = "选中的合作方Id")
    @NotBlank(message = "选中的合作方Id不能为空")
    private String checkedId;

    @ApiModelProperty(value = "替换的合作方优先级(ps:上移一层=checkedPriority-1,下移一层=checkedPriority+1,置于顶层=1,置于底层=totalElement)")
    @Min(value = 0,message = "优先级不能小于0")
    private Integer replacedPriority;
}
