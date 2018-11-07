package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerDetailViewModel {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "合作方编号")
    private String code;

    @ApiModelProperty(value = "合作方名称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系人电话")
    private String contactNumber;

    @ApiModelProperty(value = "邮箱")
    private String mailAddress;

    @ApiModelProperty(value = "省id")
    private String provinceId;

    @ApiModelProperty(value = "市id")
    private String cityId;

    @ApiModelProperty(value = "区id")
    private String regionId;

    @ApiModelProperty(value = "所在省份")
    private String provinceName;

    @ApiModelProperty(value = "所在城市")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "详细地址")
    private String addressDetail;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "维护人Id")
    private String ownerId;

    @ApiModelProperty(value = "维护人名称")
    private String ownerName;

    @ApiModelProperty(value = "创建人id")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "是否禁用")
    private boolean disabled;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "是否正在投放广告")
    private boolean advertisementDelivery;

    @ApiModelProperty(value = "累计投放广告总数量")
    private Long totalDeliveryQuantity=0L;

    @ApiModelProperty(value = "合作模式")
    private Integer cooperationPattern;

    @ApiModelProperty(value = "合作模式名称")
    private String cooperationPatternName;

    @ApiModelProperty(value = "合作模式时长")
    private Integer duration;

    @ApiModelProperty(value = "合作模式时长单位", required = true)
    private Integer durationUnit;

    @ApiModelProperty(value = "合作模式时长单位名称", required = true)
    private String durationUnitName;

    @ApiModelProperty(value = "合作方请求URL")
    private String apiUrl;

}
