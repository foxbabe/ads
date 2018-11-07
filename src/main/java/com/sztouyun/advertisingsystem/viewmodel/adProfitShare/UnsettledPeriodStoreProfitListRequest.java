package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/1/12.
 */
@ApiModel
public class UnsettledPeriodStoreProfitListRequest extends BasePageInfo {
    @ApiModelProperty(value="结算月份")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date settledMonth;
    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "门店ID")
    private String shopId;
    @ApiModelProperty(value = "区域ID列表：逗号分隔")
    private String areaIds;
    @ApiModelProperty(value = "结算ID，用于回显结算")
    private String id;
    @ApiModelProperty(hidden = true)
    private Boolean hasAbnormalNode;

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<String> getAreaIds() {
        if(org.springframework.util.StringUtils.isEmpty(this.areaIds))
            return new ArrayList<>();
        return Arrays.asList(this.areaIds.split(Constant.SEPARATOR));
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasAbnormalNode() {
        return hasAbnormalNode;
    }

    public void setHasAbnormalNode(Boolean hasAbnormalNode) {
        this.hasAbnormalNode = hasAbnormalNode;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
