package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 收益流水(返回)
 * @author guangpu.yan
 * @create 2018-01-11 9:43
 **/
@ApiModel
public class BaseStoreProfitStreamViewModel {
    @ApiModelProperty(value = "收益流水id")
    private String id;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value = "开机是否达标")
    private boolean openingTimeStandardIs;

    @ApiModelProperty(value = "订单是否达标")
    private boolean orderStandardIs;

    @ApiModelProperty(value = "开机时长")
    private Double openingTime = 0D;

    @ApiModelProperty(value = "开店天数是否达标")
    private boolean storeCreatedTimeStandardIs;

    @ApiModelProperty(value = "当天月平均订单数")
    private Double orderNum;

    @ApiModelProperty(value = "开机标准值")
    private Double openingTimeStandard;

    @ApiModelProperty(value = "月平均订单标准值")
    private Double orderStandard;

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

    @ApiModelProperty(value = "是否结算")
    private Boolean settled;

    @ApiModelProperty(value = "分成金额")
    private Double shareAmount;

    @ApiModelProperty(value = "开店时间, 按照格式 **小时**分钟  显示")
    private String openingTimeConvert;

    public String getOpeningTimeConvert() {
        return openingTimeConvert;
    }

    public void setOpeningTimeConvert(String openingTimeConvert) {
        this.openingTimeConvert = openingTimeConvert;
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

    public boolean isOpeningTimeStandardIs() {
        return openingTimeStandardIs;
    }

    public void setOpeningTimeStandardIs(boolean openingTimeStandardIs) {
        this.openingTimeStandardIs = openingTimeStandardIs;
    }

    public boolean isOrderStandardIs() {
        return orderStandardIs;
    }

    public void setOrderStandardIs(boolean orderStandardIs) {
        this.orderStandardIs = orderStandardIs;
    }

    public Double getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Double openingTime) {
        this.openingTime = openingTime;
    }

    public Double getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Double orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOpeningTimeStandard() {
        return openingTimeStandard;
    }

    public void setOpeningTimeStandard(Double openingTimeStandard) {
        this.openingTimeStandard = openingTimeStandard;
    }

    public Double getOrderStandard() {
        return orderStandard;
    }

    public void setOrderStandard(Double orderStandard) {
        this.orderStandard = orderStandard;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public boolean isStoreCreatedTimeStandardIs() {
        return storeCreatedTimeStandardIs;
    }

    public void setStoreCreatedTimeStandardIs(boolean storeCreatedTimeStandardIs) {
        this.storeCreatedTimeStandardIs = storeCreatedTimeStandardIs;
    }

    public String getShareAmount() {
        return NumberFormatUtil.format(shareAmount, Constant.SCALE_TWO);
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }
}
