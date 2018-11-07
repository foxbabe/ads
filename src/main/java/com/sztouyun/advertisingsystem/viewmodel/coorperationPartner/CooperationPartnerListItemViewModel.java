package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class CooperationPartnerListItemViewModel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "合作方编号")
    private String code;

    @ApiModelProperty(value = "合作方名称")
    private String name;

    @ApiModelProperty(value = "市id")
    private String cityId;

    @ApiModelProperty(value = "所在城市")
    private String cityName;

    @ApiModelProperty(value = "维护人Id")
    private String ownerId;

    @ApiModelProperty(value = "维护人名称")
    private String ownerName;

    @ApiModelProperty(value = "创建人id")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "是否禁用")
    private boolean disabled;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date createdTime;

    @ApiModelProperty(value = "是否正在投放广告")
    private boolean advertisementDelivery;

    @ApiModelProperty(value = "累计投放广告总数量")
    private Long totalDeliveryQuantity=0L;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "合作模式")
    private String cooperationPattern;

    @ApiModelProperty(value = "合作模式时长")
    private String duration="";

    @ApiModelProperty(value = "合作方请求URL")
    private String apiUrl;
}
