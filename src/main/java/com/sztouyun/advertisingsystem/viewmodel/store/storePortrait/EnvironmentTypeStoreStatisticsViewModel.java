package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@NoArgsConstructor
public class EnvironmentTypeStoreStatisticsViewModel {
    @ApiModelProperty("画像维度名称")
    private String environmentTypeName;

    @ApiModelProperty("门店数量")
    private Long storeNum;

    @ApiModelProperty("门店占比")
    private String storeProportion;

    public EnvironmentTypeStoreStatisticsViewModel(String environmentTypeName) {
        this.environmentTypeName = environmentTypeName;
    }
}
