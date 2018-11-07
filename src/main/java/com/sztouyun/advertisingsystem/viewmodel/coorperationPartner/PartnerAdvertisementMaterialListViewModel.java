package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PartnerAdvertisementMaterialListViewModel{

    @ApiModelProperty(value = "素材ID")
    private String id;

    @ApiModelProperty(value = "ADS素材URL")
    private String url;

    @ApiModelProperty(value = "素材类型")
    private Integer materialType;

    @ApiModelProperty(value = "素材类型名称")
    private String materialTypeName;

    @ApiModelProperty(value = "素材尺寸")
    private String materialSpecification;

    @ApiModelProperty(value = "素材时长")
    private Integer duration;

    @ApiModelProperty(value = "素材大小")
    private String materialSize;

    @ApiModelProperty(value = "素材时长单位")
    private String durationUnitName;

}
