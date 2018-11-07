package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class AdvertisementSettlementSelectAllRequest extends BaseAdvertisementSettlementRequest {

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店ID")
    private String shopId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "区域ID列表：逗号分隔")
    private String areaIds;

    @ApiModelProperty(value = "是否含有无省市区节点", hidden = true)
    private boolean hasAbnormalNode;

    @ApiModelProperty(value = "是否全选", hidden = true)
    private boolean selectAll;

    @ApiModelProperty(value = "区域ID的list", hidden = true)
    private List<String> regionIds = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    private Integer start;

    @ApiModelProperty(hidden = true)
    private Integer pageSize;
}
