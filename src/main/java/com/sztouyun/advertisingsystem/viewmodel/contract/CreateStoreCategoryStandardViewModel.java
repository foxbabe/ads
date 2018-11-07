package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.BorderType;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.common.annotation.validation.NumberBorder;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class CreateStoreCategoryStandardViewModel {

    @ApiModelProperty(value = "门店类别标准名称", required = true)
    @Size(max = 128,message ="门店类别标准名称太长" )
    @NotBlank(message = "门店类别标准名称不能为空")
    private String storeCategoryStandardName;

    @ApiModelProperty(value = "类别类型", required = true)
    @EnumValue(enumClass= StoreTypeEnum.class,nullable = false,message = "请输入正确的门店类别")
    private Integer storeType;

    @ApiModelProperty(value = "平均每日交易订单数最小值")
    @NumberBorder(type = BorderType.MIN,border = 0,nullable = true,message = "请输入有效的平均每日交易订单数最小值")
    private Integer avgDailyTradingAmountMin;

    @ApiModelProperty(value = "平均每日交易订单数最大值(如果是无穷大，填写：999999)")
    @NumberBorder(type = BorderType.MAX,border = 999999,nullable = true,message = "请输入有效的平均每日交易订单数最大值")
    private Integer avgDailyTradingAmountMax;


    @ApiModelProperty(value = "左边运算符")
    @EnumValue(enumClass = ComparisonTypeEnum.class,nullable = true,message = "比较类型参数不匹配")
    private Integer leftSymbol;

    @ApiModelProperty(value = "右边运算符")
    @EnumValue(enumClass = ComparisonTypeEnum.class,nullable = true,message = "比较类型参数不匹配")
    private Integer rightSymbol;

    public String getStoreCategoryStandardName() {
        return storeCategoryStandardName;
    }

    public void setStoreCategoryStandardName(String storeCategoryStandardName) {
        this.storeCategoryStandardName = storeCategoryStandardName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getAvgDailyTradingAmountMin() {
        return avgDailyTradingAmountMin;
    }

    public void setAvgDailyTradingAmountMin(Integer avgDailyTradingAmountMin) {
        this.avgDailyTradingAmountMin = avgDailyTradingAmountMin;
    }

    public Integer getAvgDailyTradingAmountMax() {
        return avgDailyTradingAmountMax;
    }

    public void setAvgDailyTradingAmountMax(Integer avgDailyTradingAmountMax) {
        this.avgDailyTradingAmountMax = avgDailyTradingAmountMax;
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
