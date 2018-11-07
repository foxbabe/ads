package com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
public class CustomerVisitDetailViewModel extends UpdateCustomerVisitViewModel{
    @ApiModelProperty(value = "客户编号")
    private String customerNumber;

    @ApiModelProperty(value = "客户联系人")
    private String contacts;

    @ApiModelProperty(value = "客户联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "客户联系邮箱")
    private String mailAdress;

    @ApiModelProperty(value = "客户详细地址")
    private String adressDetail;

    @ApiModelProperty(value = "客户拜访创建人")
    private String creator;

    @ApiModelProperty(value = "拜访时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date visitTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN,timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "客户头像")
    private String headPortrait;


    @ApiModelProperty(value = "所在省份")
    private String provinceName;

    @ApiModelProperty(value = "所在城市")
    private String cityName;

    @ApiModelProperty(value = "从属行业")
    private String industry;

    @ApiModelProperty(value = "省/市/区")
    private String region;

    @ApiModelProperty(value = "从属子行业")
    private String subIndustry;

    @ApiModelProperty(value = "负责人名称")
    private String ownerName;

    @ApiModelProperty(value = "正在投放广告")
    private boolean delivering;

    @ApiModelProperty(value = "投放次数")
    private long advertisementDeliveryTimes = 0;

    @ApiModelProperty(value = "广告总额")
    private double advertisementTotalAmount = 0.00;

    @ApiModelProperty(value = "最后投放广告时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN, timezone = "GMT+8")
    private Date lastestDeliveryTime;

    @ApiModelProperty(value = "能否编辑")
    private Boolean canEdit=true;

    @ApiModelProperty(value = "拜访次数")
    private long visitTimes;

    @ApiModelProperty(value = "最后一次拜访时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date lastestVisitTime;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isDelivering() {
        return delivering;
    }

    public void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }

    public long getAdvertisementDeliveryTimes() {
        return advertisementDeliveryTimes;
    }

    public void setAdvertisementDeliveryTimes(long advertisementDeliveryTimes) {
        this.advertisementDeliveryTimes = advertisementDeliveryTimes;
    }

    public double getAdvertisementTotalAmount() {
        return advertisementTotalAmount;
    }

    public void setAdvertisementTotalAmount(double advertisementTotalAmount) {
        this.advertisementTotalAmount = advertisementTotalAmount;
    }

    public Date getLastestDeliveryTime() {
        return lastestDeliveryTime;
    }

    public void setLastestDeliveryTime(Date lastestDeliveryTime) {
        this.lastestDeliveryTime = lastestDeliveryTime;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public String getAdressDetail() {
        return adressDetail;
    }

    public void setAdressDetail(String adressDetail) {
        this.adressDetail = adressDetail;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSubIndustry() {
        return subIndustry;
    }

    public void setSubIndustry(String subIndustry) {
        this.subIndustry = subIndustry;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public Date getVisitTime() {
        return visitTime;
    }

    @Override
    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(long visitTimes) {
        this.visitTimes = visitTimes;
    }

    public Date getLastestVisitTime() {
        return lastestVisitTime;
    }

    public void setLastestVisitTime(Date lastestVisitTime) {
        this.lastestVisitTime = lastestVisitTime;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
