package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 收益流水(返回)
 * @author guangpu.yan
 * @create 2018-01-11 9:43
 **/
@ApiModel
public class StoreProfitStreamViewDetailModel extends BaseStoreProfitStreamViewModel{

    @ApiModelProperty(value = "收益广告比例")
    private Double proportion;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;


    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value = "开机时长单位")
    private Integer openingTimeUnit;

    @ApiModelProperty(value = "开机时长单位")
    private String openingTimeUnitName;

    @ApiModelProperty(value = "订单数量单位")
    private Integer orderUnit;

    @ApiModelProperty(value = "订单数量单位")
    private String orderUnitName;

    @ApiModelProperty(value = "开机时长比较类型")
    private Integer openingTimeComparisonType;

    @ApiModelProperty(value = "开机时长比较类型")
    private String openingTimeComparisonTypeName;

    @ApiModelProperty(value = "订单数量比较类型")
    private Integer orderComparisonType;

    @ApiModelProperty(value = "订单数量比较类型")
    private String orderComparisonTypeName;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "开店天数标准值")
    private Double storeCreatedTimeStandard;

    @ApiModelProperty(value = "开店天数")
    private Integer storeCreatedTime;

    @ApiModelProperty(value = "开店天数单位")
    private Integer storeCreatedTimeUnit;

    @ApiModelProperty(value = "开店天数单位")
    private String storeCreatedTimeUnitName;

    @ApiModelProperty(value = "开店天数比较类型")
    private Integer storeCreatedTimeComparisonType;

    @ApiModelProperty(value = "开店天数比较类型")
    private String storeCreatedTimeComparisonTypeName;

    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    @ApiModelProperty(value = "开机时间点")
    private Date openingTimeBegin;

    @ApiModelProperty(value = "关机时间点")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date openingTimeEnd;

    @ApiModelProperty(value = "开店时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty(value = "月平均交易订单开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date cycleBegin;

    @ApiModelProperty(value = "月平均交易订单结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date cycleEnd;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Date getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(Date profitDate) {
        this.profitDate = profitDate;
    }

    public Integer getOpeningTimeUnit() {
        return openingTimeUnit;
    }

    public void setOpeningTimeUnit(Integer openingTimeUnit) {
        this.openingTimeUnit = openingTimeUnit;
    }

    public Integer getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(Integer orderUnit) {
        this.orderUnit = orderUnit;
    }

    public Integer getOpeningTimeComparisonType() {
        return openingTimeComparisonType;
    }

    public void setOpeningTimeComparisonType(Integer openingTimeComparisonType) {
        this.openingTimeComparisonType = openingTimeComparisonType;
    }

    public Integer getOrderComparisonType() {
        return orderComparisonType;
    }

    public void setOrderComparisonType(Integer orderComparisonType) {
        this.orderComparisonType = orderComparisonType;
    }

    public String getOpeningTimeUnitName() {
        return openingTimeUnit==null?null:EnumUtils.toEnum(this.openingTimeUnit, UnitEnum.class).getDisplayName();
    }

    public void setOpeningTimeUnitName(String openingTimeUnitName) {
        this.openingTimeUnitName = openingTimeUnitName;
    }

    public String getOrderUnitName() {
        return orderUnit==null?null:EnumUtils.toEnum(this.orderUnit, UnitEnum.class).getDisplayName();
    }

    public void setOrderUnitName(String orderUnitName) {
        this.orderUnitName = orderUnitName;
    }

    public String getOpeningTimeComparisonTypeName() {
        return openingTimeComparisonType==null?null:EnumUtils.toEnum(this.openingTimeComparisonType, ComparisonTypeEnum.class).getDisplayName();
    }

    public void setOpeningTimeComparisonTypeName(String openingTimeComparisonTypeName) {
        this.openingTimeComparisonTypeName = openingTimeComparisonTypeName;
    }

    public String getOrderComparisonTypeName() {
        return orderComparisonType==null?null:EnumUtils.toEnum(this.orderComparisonType, ComparisonTypeEnum.class).getDisplayName();
    }

    public void setOrderComparisonTypeName(String orderComparisonTypeName) {
        this.orderComparisonTypeName = orderComparisonTypeName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getStoreCreatedTimeStandard() {
        return storeCreatedTimeStandard;
    }

    public void setStoreCreatedTimeStandard(Double storeCreatedTimeStandard) {
        this.storeCreatedTimeStandard = storeCreatedTimeStandard;
    }

    public Integer getStoreCreatedTime() {
        return storeCreatedTime;
    }

    public void setStoreCreatedTime(Integer storeCreatedTime) {
        this.storeCreatedTime = storeCreatedTime;
    }

    public Integer getStoreCreatedTimeUnit() {
        return storeCreatedTimeUnit;
    }

    public void setStoreCreatedTimeUnit(Integer storeCreatedTimeUnit) {
        this.storeCreatedTimeUnit = storeCreatedTimeUnit;
    }

    public String getStoreCreatedTimeUnitName() {
        return storeCreatedTimeUnit==null?null:EnumUtils.toEnum(this.storeCreatedTimeUnit, UnitEnum.class).getDisplayName();
    }

    public void setStoreCreatedTimeUnitName(String storeCreatedTimeUnitName) {
        this.storeCreatedTimeUnitName = storeCreatedTimeUnitName;
    }

    public Integer getStoreCreatedTimeComparisonType() {
        return storeCreatedTimeComparisonType;
    }

    public void setStoreCreatedTimeComparisonType(Integer storeCreatedTimeComparisonType) {
        this.storeCreatedTimeComparisonType = storeCreatedTimeComparisonType;
    }

    public String getStoreCreatedTimeComparisonTypeName() {
        return storeCreatedTimeComparisonType==null?null:EnumUtils.toEnum(this.storeCreatedTimeComparisonType, ComparisonTypeEnum.class).getDisplayName();
    }

    public void setStoreCreatedTimeComparisonTypeName(String storeCreatedTimeComparisonTypeName) {
        this.storeCreatedTimeComparisonTypeName = storeCreatedTimeComparisonTypeName;
    }

    public Date getOpeningTimeBegin() {
        return openingTimeBegin;
    }

    public void setOpeningTimeBegin(Date openingTimeBegin) {
        this.openingTimeBegin = openingTimeBegin;
    }

    public Date getOpeningTimeEnd() {
        return openingTimeEnd;
    }

    public void setOpeningTimeEnd(Date openingTimeEnd) {
        this.openingTimeEnd = openingTimeEnd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCycleBegin() {
        return cycleBegin;
    }

    public void setCycleBegin(Date cycleBegin) {
        this.cycleBegin = cycleBegin;
    }

    public Date getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(Date cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}
