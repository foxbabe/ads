package com.sztouyun.advertisingsystem.viewmodel.store;


import io.swagger.annotations.ApiModelProperty;

public class StoreInfoAreaStatistic {
    @ApiModelProperty(value ="城市名称")
    private String areaName;

    @ApiModelProperty(value = "城市ID")
    private String areaId;

    @ApiModelProperty(value = "城市code")
    private String code;

    @ApiModelProperty(value = "城市总门店数量")
    private Long storeCount = 0L;

    @ApiModelProperty(value = "城市总门店数量占比")
    private String totalStoreCountRatio;

    @ApiModelProperty(value = "城市已使用门店数量")
    private Long usedStoreCount = 0L;

    @ApiModelProperty(value = "城市未使用门店数量")
    private Integer unUsedStoreCount = 0;

    @ApiModelProperty(value = "已使用门店数量占比")
    private String usedStoreCountRatio;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Long storeCount) {
        this.storeCount = storeCount;
    }

    public String getTotalStoreCountRatio() {
        return totalStoreCountRatio;
    }

    public void setTotalStoreCountRatio(String totalStoreCountRatio) {
        this.totalStoreCountRatio = totalStoreCountRatio;
    }

    public Long getUsedStoreCount() {
        return usedStoreCount;
    }

    public void setUsedStoreCount(Long usedStoreCount) {
        if(usedStoreCount!=null){
            this.usedStoreCount = usedStoreCount;
        }
        this.unUsedStoreCount=(int)(storeCount-this.usedStoreCount);
    }

    public Integer getUnUsedStoreCount() {
        return this.unUsedStoreCount;
    }

    public void setUnUsedStoreCount(Integer unUsedStoreCount) {
        this.unUsedStoreCount = unUsedStoreCount;
    }

    public String getUsedStoreCountRatio() {
        return usedStoreCountRatio;
    }

    public void setUsedStoreCountRatio(String usedStoreCountRatio) {
        this.usedStoreCountRatio = usedStoreCountRatio;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public StoreInfoAreaStatistic(){}
}
