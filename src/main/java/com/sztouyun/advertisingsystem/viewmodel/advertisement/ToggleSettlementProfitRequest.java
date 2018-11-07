package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class ToggleSettlementProfitRequest extends BaseAdvertisementSettlementRequest{

    @ApiModelProperty(value = "日收益表ID", required = true)
    @NotBlank(message = "日收益ID不能为空")
    private String id;
}
