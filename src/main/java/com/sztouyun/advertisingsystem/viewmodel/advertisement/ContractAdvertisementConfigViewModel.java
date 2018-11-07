package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.swing.text.Position;
import java.util.List;

@ApiModel
public class ContractAdvertisementConfigViewModel {

    @ApiModelProperty(value = "投放门店总数")
    private Integer totalStores;

    @ApiModelProperty(value = "A类门店数量")
    private Integer storeACount;

    @ApiModelProperty(value = "B类门店数量")
    private Integer storeBCount;

    @ApiModelProperty(value = "C类门店数量")
    private Integer storeCCount;

    @ApiModelProperty(value = "D类门店数量")
    private Integer storeDCount;

    @ApiModelProperty(value = "广告总价")
    private double totalCost;

    @ApiModelProperty(value = "投放广告位置")
    private String sizeName;

    @ApiModelProperty(value = "广告尺寸水平分辨率")
    private Integer horizontalResolution;

    @ApiModelProperty(value = "广告尺寸垂直分辨率")
    private Integer verticalResolution;

    @ApiModelProperty(value = "广告尺寸分辨率")
    private String resolution;

    @ApiModelProperty(value = "广告时长")
    private Integer duration;

    @ApiModelProperty(value = "广告时长单位中文")
    private String durationUnitName;

    @ApiModelProperty(value = "广告展示次数")
    private Integer  displayTimes;

    @ApiModelProperty(value = "广告展示次数单位中文")
    private String timeUnittName;

    @ApiModelProperty(value = "广告投放位置列表（图片或视频需要）")
    private List<PositionTypeItemViewModel> positionTypeItemList;

    /**
     * 广告位置是否选中 "全部"
     */
    @ApiModelProperty(value = "广告投放位置是否选中全部")
    private boolean  chooseAllPosition;

    public Integer getTotalStores() {
        return totalStores;
    }

    public void setTotalStores(Integer totalStores) {
        this.totalStores = totalStores;
    }

    public Integer getStoreACount() {
        return storeACount;
    }

    public void setStoreACount(Integer storeACount) {
        this.storeACount = storeACount;
    }

    public Integer getStoreBCount() {
        return storeBCount;
    }

    public void setStoreBCount(Integer storeBCount) {
        this.storeBCount = storeBCount;
    }

    public Integer getStoreCCount() {
        return storeCCount;
    }

    public void setStoreCCount(Integer storeCCount) {
        this.storeCCount = storeCCount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationUnitName() {
        return durationUnitName;
    }

    public void setDurationUnitName(String durationUnitName) {
        this.durationUnitName = durationUnitName;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }

    public String getTimeUnittName() {
        return timeUnittName;
    }

    public void setTimeUnittName(String timeUnittName) {
        this.timeUnittName = timeUnittName;
    }

    public List<PositionTypeItemViewModel> getPositionTypeItemList() {
        return positionTypeItemList;
    }

    public void setPositionTypeItemList(List<PositionTypeItemViewModel> positionTypeItemList) {
        this.positionTypeItemList = positionTypeItemList;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public boolean isChooseAllPosition() {
        return chooseAllPosition;
    }

    public void setChooseAllPosition(boolean chooseAllPosition) {
        this.chooseAllPosition = chooseAllPosition;
    }

    public Integer getStoreDCount() {
        return storeDCount;
    }

    public void setStoreDCount(Integer storeDCount) {
        this.storeDCount = storeDCount;
    }
}
