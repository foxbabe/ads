package com.sztouyun.advertisingsystem.viewmodel.commodity;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierOptionRequest extends BasePageInfo {

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
}
