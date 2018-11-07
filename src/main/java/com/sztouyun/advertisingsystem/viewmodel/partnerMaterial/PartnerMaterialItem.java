package com.sztouyun.advertisingsystem.viewmodel.partnerMaterial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BaseOpenApiViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/1/30.
 */
@Data
@ApiModel
public class PartnerMaterialItem extends BaseOpenApiViewModel {
    @ApiModelProperty(value = "素材ID")
    private String id;
    @ApiModelProperty(value = "素材编号")
    private String materialCode;
    @ApiModelProperty(value = "素材状态名称")
    private String partnerMaterialStatusName;
    @ApiModelProperty(value = "素材状态")
    private Integer partnerMaterialStatus;
    @ApiModelProperty(value = "素材类型")
    private Integer materialType;
    @ApiModelProperty(value = "广告位置")
    private Integer advertisementPositionType;
    @ApiModelProperty(value = "第三方原始的素材URL")
    private String originalUrl;
    @ApiModelProperty(value = "ADS素材URL")
    private String url;
    @ApiModelProperty(value = "广告位置名称")
    private String advertisementPositionTypeName;
    @ApiModelProperty(value = "素材类型名称")
    private String materialTypeName;
    @ApiModelProperty(value = "合作方名称")
    private String partnerName;
    @ApiModelProperty(value = "素材宽高")
    private String materialSpecification;
    @ApiModelProperty(value = "素材大小")
    private String materialSize;
    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date uploadTime;
    @ApiModelProperty(value = "审核时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date auditTime;
    @ApiModelProperty(value = "审核人")
    private String auditor;
}
