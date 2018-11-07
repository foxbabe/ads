package com.sztouyun.advertisingsystem.viewmodel.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class OrderStatisticsViewModel {

    @ApiModelProperty(value = "订单状态(0:全部,1:待上刊,2:待上刊审核,3:投放中,4:审核失败,5:待投放,10:已取消,11:已下架,12:已完成)")
    private Integer status;
    @ApiModelProperty(value = "数量")
    private Long count;


    public OrderStatisticsViewModel(Integer status, Long count) {
        this.status = status;
        this.count = count;
    }

    public OrderStatisticsViewModel(){}
}
