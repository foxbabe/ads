package com.sztouyun.advertisingsystem.viewmodel.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
@Data
public class UpdateContractViewModel extends BaseContractViewModel {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "已经选择的A类门店数量")
    private Integer storeACountChecked;

    @ApiModelProperty(value = "已经选择的B类门店数量")
    private Integer storeBCountChecked;

    @ApiModelProperty(value = "已经选择的C类门店数量")
    private Integer storeCCountChecked;

    @ApiModelProperty(value = "已经选择的C类门店数量")
    private Integer storeDCountChecked;

    @ApiModelProperty(value = "选点记录id")
    private String customerStorePlanId;

    @ApiModelProperty(value = "选点门店总数")
    private Integer customerChooseStoreCount=0;

    @ApiModelProperty(value = "选点记录编号")
    private String  code="";

}
