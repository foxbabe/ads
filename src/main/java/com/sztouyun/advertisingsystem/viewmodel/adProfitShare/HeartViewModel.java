package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class HeartViewModel extends BasePageInfo {

    @ApiModelProperty(value = "收益流水id", required = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
