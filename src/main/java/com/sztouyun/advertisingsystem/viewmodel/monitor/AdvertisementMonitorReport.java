package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementMaterialViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class AdvertisementMonitorReport extends AdvertisementMonitorStatisticViewModel {

    @ApiModelProperty(value = "广告类型名称")
    private String advertisementTypeName;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    private String advertisementId;

    private Integer duration;

    private Integer displayFrequency;

    private String deliveryCities;

    private Integer totalStoreCount;

    private String remark;

    private List<AdvertisementMaterialViewModel> advertisementMaterials = new ArrayList<>();

    private String advertisementMaterial;

    public String getAdvertisementTypeName() {
        return advertisementTypeName;
    }

    public void setAdvertisementTypeName(String advertisementTypeName) {
        this.advertisementTypeName = advertisementTypeName;
    }

    public String getAdvertisementStatusName() {
        return advertisementStatusName;
    }

    public void setAdvertisementStatusName(String advertisementStatusName) {
        this.advertisementStatusName = advertisementStatusName;
    }

    public List<AdvertisementMaterialViewModel> getAdvertisementMaterials() {
        return advertisementMaterials;
    }

    public void setAdvertisementMaterials(List<AdvertisementMaterialViewModel> advertisementMaterials) {
        this.advertisementMaterials = advertisementMaterials;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDisplayFrequency() {
        return displayFrequency;
    }

    public void setDisplayFrequency(Integer displayFrequency) {
        this.displayFrequency = displayFrequency;
    }

    public String getDeliveryCities() {
        return deliveryCities;
    }

    public void setDeliveryCities(String deliveryCities) {
        this.deliveryCities = deliveryCities;
    }

    public Integer getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Integer totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getAdvertisementMaterial() {
        return advertisementMaterial;
    }

    public void setAdvertisementMaterial(String advertisementMaterial) {
        this.advertisementMaterial = StringUtils.isEmpty(advertisementMaterial)?"":advertisementMaterial.substring(0,advertisementMaterial.length()-1);
    }
}
