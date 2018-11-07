package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by szty on 2018/5/23.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportViewModel {
    @ApiModelProperty(value = "新的客户选点ID")
    private String tempCustomerStorePlanId;
    @ApiModelProperty(value = "总数")
    private Integer total=0;
    @ApiModelProperty(value = "不可用数量")
    private Integer unAvailable=0;
    @ApiModelProperty(value = "不匹配数量")
    private Integer unmatched=0;

    public ExcelImportViewModel(String tempCustomerStorePlanId) {
        this.tempCustomerStorePlanId = tempCustomerStorePlanId;
    }
}
