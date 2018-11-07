package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.viewmodel.common.BaseOpenApiViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel
public class DeliveryAuditOrderViewModel extends BaseOpenApiViewModel {

    @ApiModelProperty(value = "素材ID")
    @NotBlank(message = "素材ID不能为空")
    private String materialId;

}
