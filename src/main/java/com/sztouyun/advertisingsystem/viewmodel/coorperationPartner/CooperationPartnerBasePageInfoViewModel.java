package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CooperationPartnerBasePageInfoViewModel extends BasePageInfo {

    @ApiModelProperty(value = "合作方编号")
    private String code;

    @ApiModelProperty(value = "合作方名称")
    private String name;

    @ApiModelProperty(value = "业务员")
    private String nickname;

    @ApiModelProperty(value = "合作模式")
    @EnumValue(enumClass = CooperationPatternEnum.class,message = "合作模式不匹配",nullable = true)
    private Integer cooperationPattern;

}
