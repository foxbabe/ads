package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2018/1/17.
 */
@ApiModel
public class BaseSettledStoreProfitRequest {
    @ApiModelProperty(value = "结算ID",required = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
