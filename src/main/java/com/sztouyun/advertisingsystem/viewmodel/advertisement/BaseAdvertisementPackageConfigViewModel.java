package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class BaseAdvertisementPackageConfigViewModel {
    @ApiModelProperty(value = "套餐名称", required = true)
    @NotBlank(message = "请输入套餐名称")
    private String  packageName;



    @ApiModelProperty(value = "A类屏位数", required = true)
    @NotNull(message = "A类屏位数不能为空")
    @Min(value = 0,message = "请输入有效的屏位数量")
    private Integer amontOfTypeA;

    @ApiModelProperty(value = "B类屏位数", required = true)
    @NotNull(message = "B类屏位数不能为空")
    @Min(value = 0,message = "请输入有效的屏位数量")
    private Integer amontOfTypeB;

    @ApiModelProperty(value = "C类屏位数", required = true)
    @NotNull(message = "C类屏位数不能为空")
    @Min(value = 0,message = "请输入有效的屏位数量")
    private Integer amontOfTypeC;

    @ApiModelProperty(value = "D类屏位数", required = true)
    @NotNull(message = "D类屏位数不能为空")
    @Min(value = 0,message = "请输入有效的屏位数量")
    private Integer amontOfTypeD;

    @ApiModelProperty(value = "套餐总价", required = true)
    @NotNull(message = "“请输入套餐总价")
    @Min(value = 0,message = "请输入有效的广告套餐总价")
    @NumberFormat(pattern = "0.00")
    private BigDecimal totalCost;

    @ApiModelProperty(value = "套餐周期类型(1,2,3,6,12)", required = true)
    @NotNull(message = "请选择套餐周期")
    @Min(value=1,message = "请选择有效的套餐周期")
    @Max(value=12,message = "请选择有效的套餐周期")
    private Integer  period;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getAmontOfTypeA() {
        return amontOfTypeA;
    }

    public void setAmontOfTypeA(Integer amontOfTypeA) {
        this.amontOfTypeA = amontOfTypeA;
    }

    public Integer getAmontOfTypeB() {
        return amontOfTypeB;
    }

    public void setAmontOfTypeB(Integer amontOfTypeB) {
        this.amontOfTypeB = amontOfTypeB;
    }

    public Integer getAmontOfTypeC() {
        return amontOfTypeC;
    }

    public void setAmontOfTypeC(Integer amontOfTypeC) {
        this.amontOfTypeC = amontOfTypeC;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getAmontOfTypeD() {
        return amontOfTypeD;
    }

    public void setAmontOfTypeD(Integer amontOfTypeD) {
        this.amontOfTypeD = amontOfTypeD;
    }
}
