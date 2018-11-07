package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by szty on 2017/8/7.
 */
@ApiModel
public class StoreCategoryStandardDetailViewModel  {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(value = "创建人")
    private String CreatorName;

    @ApiModelProperty(value = "门店类别标准名称")
    private String storeCategoryStandardName;

    @ApiModelProperty(value = "类别类型名称")
    private String storeTypeName;

    @ApiModelProperty(value = "类别类型")
    private Integer storeType;

    @ApiModelProperty(value = "平均每日交易订单数最小值")
    private Integer AvgDailyTradingAmountMin;

    @ApiModelProperty(value = "平均每日交易订单数最大值")
    private Integer AvgDailyTradingAmountMax;

    @ApiModelProperty(value = "左边运算符", required = true)
    private Integer leftSymbol;

    @ApiModelProperty(value = "右边运算符", required = true)
    private Integer rightSymbol;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreCategoryStandardName() {
        return storeCategoryStandardName;
    }

    public void setStoreCategoryStandardName(String storeCategoryStandardName) {
        this.storeCategoryStandardName = storeCategoryStandardName;
    }

    public String getStoreTypeName() {
        return storeTypeName;
    }

    public void setStoreTypeName(String storeTypeName) {
        this.storeTypeName = storeTypeName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getAvgDailyTradingAmountMin() {
        return AvgDailyTradingAmountMin;
    }

    public void setAvgDailyTradingAmountMin(Integer avgDailyTradingAmountMin) {
        AvgDailyTradingAmountMin = avgDailyTradingAmountMin;
    }

    public Integer getAvgDailyTradingAmountMax() {
        return AvgDailyTradingAmountMax;
    }

    public void setAvgDailyTradingAmountMax(Integer avgDailyTradingAmountMax) {
        AvgDailyTradingAmountMax = avgDailyTradingAmountMax;
    }

    public Integer getLeftSymbol() {
        return leftSymbol;
    }

    public void setLeftSymbol(Integer leftSymbol) {
        this.leftSymbol = leftSymbol;
    }

    public Integer getRightSymbol() {
        return rightSymbol;
    }

    public void setRightSymbol(Integer rightSymbol) {
        this.rightSymbol = rightSymbol;
    }
}
