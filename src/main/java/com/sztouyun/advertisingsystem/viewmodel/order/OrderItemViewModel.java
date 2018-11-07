package com.sztouyun.advertisingsystem.viewmodel.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.partnerMaterial.PartnerOrderMaterialInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class OrderItemViewModel{

    @ApiModelProperty(value = "订单编号")
    private String code;

    @ApiModelProperty(value = "订单名称")
    private String name;

    @ApiModelProperty(value = "合作方名称id")
    private String partnerId;

    @ApiModelProperty(value = "合作方名称")
    private String partnerName;

    @ApiModelProperty(value = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "投放门店总数量")
    private Integer totalStoreCount;

    @ApiModelProperty(value = "订单开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date startTime;

    @ApiModelProperty(value = "订单投放天数")
    private String totalDays;

    @ApiModelProperty(value = "订单金额")
    private Integer orderAmount;

    @ApiModelProperty(value = "订单状态(1:待上刊,2:待上刊审核,3:投放中,4:审核失败,5:待投放,10:已取消,11:已下架,12:已完成)")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单状态描述")
    private String orderStatusDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date createdTime;

    @ApiModelProperty(value = "订单结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date endTime;

    @ApiModelProperty(value = "订单素材信息")
    private List<PartnerOrderMaterialInfo> partnerOrderMaterialInfos;

    @ApiModelProperty(value = "开始投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "结束投放时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "实际投放天数")
    private String effectiveTotalDays;


}
