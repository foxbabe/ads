package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2017/11/15.
 */
@ApiModel
@Data
public class AdvertisementStoreInfoStatisticViewModel {
    @ApiModelProperty(value = "广告总数")
    private Integer totalCount = 0;

    @ApiModelProperty(value = "激活展示数")
    private Integer activedCount = 0;

    @ApiModelProperty(value = "未激活展示数")
    private Integer inactivedCount = 0;

    public Integer getInactivedCount() {
        return totalCount - activedCount ;
    }
}
