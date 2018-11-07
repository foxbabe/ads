package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.viewmodel.common.BaseOpenApiViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class CreateOrderViewModel extends BaseOpenApiViewModel {

    @ApiModelProperty(value = "订单素材列表", required = true)
    @NotNull(message = "订单素材不能为空")
    @Valid
    private OrderMaterialViewModel orderMaterial;

    @ApiModelProperty(value = "订单投放明细", required = true)
    @NotNull(message = "订单投放明细不能为空")
    @Valid
    private List<OrderDetailViewModel> orderDetails;
}
