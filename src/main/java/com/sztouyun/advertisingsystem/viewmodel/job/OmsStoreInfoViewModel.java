package com.sztouyun.advertisingsystem.viewmodel.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class OmsStoreInfoViewModel {
    @JsonProperty("StoreId")
    private String storeId;

    @JsonProperty("StoreNo")
    private String storeNo;

    @JsonProperty(value = "OldStoreNo")
    private String oldStoreNo="";

    @JsonProperty("StoreName")
    private String storeName;

    @JsonProperty("StoreAddress")
    private String storeAddress;

    @JsonProperty("ContactTelephone")
    private String contactTelephone;

    @JsonProperty("CreateTime")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonProperty("ProvinceRegionCode")
    private String provinceRegionCode;

    @JsonProperty("CityRegionCode")
    private String cityRegionCode;

    @JsonProperty("DistrictRegionCode")
    private String districtRegionCode;

    @JsonProperty("DeviceNo")
    private String deviceNo;

    @JsonProperty("OrderNumber")
    private Integer orderNumber;

    @JsonProperty("IsAvailableDevice")
    private Boolean isAvailableDevice;

    @JsonProperty("IsExist")
    private Boolean isExist;

    @JsonProperty("Longitude")
    private Double longitude;

    @JsonProperty("Latitude")
    private Double latitude;

    @JsonProperty("ShopFrontPhotoUrl")
    private String outsidePicture;

    @JsonProperty("IsStandard")
    private Boolean isQualified;

    @JsonProperty("ShopInteriorPhotoUrl")
    private String insidePicture;


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getContactTelephone() {
        return contactTelephone;
    }

    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProvinceRegionCode() {
        return provinceRegionCode;
    }

    public void setProvinceRegionCode(String provinceRegionCode) {
        this.provinceRegionCode = provinceRegionCode;
    }

    public String getCityRegionCode() {
        return cityRegionCode;
    }

    public void setCityRegionCode(String cityRegionCode) {
        this.cityRegionCode = cityRegionCode;
    }

    public String getDistrictRegionCode() {
        return districtRegionCode;
    }

    public void setDistrictRegionCode(String districtRegionCode) {
        this.districtRegionCode = districtRegionCode;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getAvailableDevice() {
        return isAvailableDevice;
    }

    public void setAvailableDevice(Boolean availableDevice) {
        isAvailableDevice = availableDevice;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public String getOldStoreNo() {
        return oldStoreNo;
    }

    public void setOldStoreNo(String oldStoreNo) {
        this.oldStoreNo = oldStoreNo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getOutsidePicture() {
        return outsidePicture;
    }

    public void setOutsidePicture(String outsidePicture) {
        this.outsidePicture = outsidePicture;
    }

    public String getInsidePicture() {
        return insidePicture;
    }

    public void setInsidePicture(String insidePicture) {
        this.insidePicture = insidePicture;
    }

    public Boolean getQualified() {
        return isQualified;
    }

    public void setQualified(Boolean qualified) {
        isQualified = qualified;
    }
}
