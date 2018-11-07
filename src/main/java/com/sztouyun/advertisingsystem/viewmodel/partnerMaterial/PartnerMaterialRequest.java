package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialStatusEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/30.
 */
@Data
public class PartnerMaterialRequest extends BasePageInfo{
    @ApiModelProperty(value = "素材类型")
    @EnumValue(enumClass = MaterialTypeEnum.class,nullable = true)
    private Integer materialType;

    @ApiModelProperty(value = "广告位置")
    @EnumValue(enumClass = AdvertisementPositionTypeEnum.class,nullable = true,message = "不支持此广告位置")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "素材审核状态，1：待审核，2：审核通过，3：审核失败")
    @EnumValue(enumClass= PartnerMaterialStatusEnum.class,nullable = true,message = "不支持此状态")
    private Integer partnerMaterialStatus;

    @ApiModelProperty(value = "合作方")
    private String partnerId;

    @ApiModelProperty(value = "素材编号")
    private String materialCode;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;


}
