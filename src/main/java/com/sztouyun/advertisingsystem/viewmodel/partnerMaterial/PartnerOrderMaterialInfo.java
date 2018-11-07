package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PartnerOrderMaterialInfo {

    @ApiModelProperty(value = "广告尺寸")
    private String materialSpecification;

    @ApiModelProperty(value = "广告时长")
    private Integer duration;

    @ApiModelProperty(value = "广告时长单位名称")
    private String durationUnitName="秒";

    @ApiModelProperty(value = "展示次数")
    private Integer  displayTimes;

    @ApiModelProperty(value = "展示次数单位名称")
    private String displayTimesUnitName="次/天";

    @ApiModelProperty(value = "投放素材")
    private String url;

    @ApiModelProperty(value = "投放位置(1:待机全屏广告,2:扫描支付页面,3:商家待机全屏,4:App开屏,5:Banner,6:商家Banner,")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "投放位置描述")
    private String advertisementPositionTypeDesc;

    @ApiModelProperty(value = "订单类型")
    @EnumValue(enumClass = MaterialTypeEnum.class,nullable = true)
    private Integer orderType;

    @ApiModelProperty(value = "订单类型描述")
    private String orderTypeDesc;
}
