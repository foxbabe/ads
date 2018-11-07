package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/7/30.
 */
@Data
public class RequestStoreRankBaseInfo {
    @JsonIgnore
    protected String cityId;
    @ApiModelProperty(value = "门店资源数量")
    protected Long configStoreCount;
}
