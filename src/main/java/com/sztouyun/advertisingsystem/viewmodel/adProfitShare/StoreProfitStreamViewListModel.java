package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 收益流水(返回)
 * @author guangpu.yan
 * @create 2018-01-11 9:43
 **/
@ApiModel
public class StoreProfitStreamViewListModel extends BaseStoreProfitStreamViewModel{

    @ApiModelProperty(value = "收益广告比例")
    private String proportion = Constant.ZERO_PERCENT;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date profitDate;

    @ApiModelProperty(value = "分成标准")
    private Double shareStandard;

    @ApiModelProperty(value = "分成标准单位")
    private Integer shareStandardUnit;

    @ApiModelProperty(value = "收益广告数量")
    private Integer effectiveAdvertisementCount;

    @ApiModelProperty(value = "门店下广告总数量")
    private Integer totalAdvertisementCount;

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
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

    public Double getShareStandard() {
        return shareStandard;
    }

    public void setShareStandard(Double shareStandard) {
        this.shareStandard = shareStandard;
    }

    public Integer getShareStandardUnit() {
        return shareStandardUnit;
    }

    public void setShareStandardUnit(Integer shareStandardUnit) {
        this.shareStandardUnit = shareStandardUnit;
    }

    public Integer getEffectiveAdvertisementCount() {
        return effectiveAdvertisementCount;
    }

    public void setEffectiveAdvertisementCount(Integer effectiveAdvertisementCount) {
        this.effectiveAdvertisementCount = effectiveAdvertisementCount;
    }

    public Integer getTotalAdvertisementCount() {
        return totalAdvertisementCount;
    }

    public void setTotalAdvertisementCount(Integer totalAdvertisementCount) {
        this.totalAdvertisementCount = totalAdvertisementCount;
    }
}
