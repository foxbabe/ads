package com.sztouyun.advertisingsystem.viewmodel.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class OrderBasePageInfoViewModel extends BasePageInfo {

    @ApiModelProperty(value = "订单编号")
    private String code;

    @ApiModelProperty(value = "订单名称")
    private String name;

    @ApiModelProperty(value = "订单状态(1:待上刊,2:待上刊审核,3:投放中,4:审核失败,5:待投放,10:已取消,11:已下架,12:已完成)")
    private Integer orderStatus;

    @ApiModelProperty(value = "投放位置(1:待机全屏广告,2:扫描支付页面,3:商家待机全屏,4:App开屏,5:Banner,6:商家Banner")
    private Integer advertisementPositionType;

    @ApiModelProperty(value = "合作方名称id")
    private String partnerId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD)
    private Date endTime;

}
