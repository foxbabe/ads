package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/7/30.
 */
@Data
public class BasePartnerInfo {
    @ApiModelProperty(value = "合作方ID")
    private String partnerId;
    @ApiModelProperty(value = "合作方名称")
    private String name;
}
