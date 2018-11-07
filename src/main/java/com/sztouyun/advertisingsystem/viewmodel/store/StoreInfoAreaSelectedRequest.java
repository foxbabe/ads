package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.viewmodel.commodity.CommodityOptionViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@NoArgsConstructor
@Data
public class StoreInfoAreaSelectedRequest extends StoreInfoBaseCondition{
    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "附近的距离长度, 单位千米")
    private Double distance;

    @ApiModelProperty(hidden = true)
    private StoreDataMapInfo storeDataMapInfo;

    private List<String> regionIds = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    private Boolean hasAbnormalNode;

    @ApiModelProperty(hidden = true)
    private Boolean hasTestNode;

    @ApiModelProperty(value = "是否使用坐标搜索", required = true)
    private Boolean isWithCoordinate = Boolean.FALSE;

    @ApiModelProperty(value = "地区ID 字符串(以英文逗号间隔)")
    private String areaIds = "";
}
