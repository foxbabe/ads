package com.sztouyun.advertisingsystem.viewmodel.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 * Created by wenfeng on 2018/4/19.
 */
@ApiModel
@Data
public class RecommendedStoreRequest {
    @ApiModelProperty(value ="地址",required = true)
    private String address;

    @ApiModelProperty(value = "推荐展示个数")
    private Integer count;
}
