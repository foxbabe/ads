package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import lombok.Data;

import java.util.Date;

@Data
public class AdvertisementInfoModel {

    /**
     * 广告ID
     */
    private String advertisementId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 广告素材中间表ID
     */
    private String advertisementMaterialId;

    /**
     * 终端类型
     */
    private Integer terminalType;

    /**
     * 广告位类型
     */
    private Integer advertisementPositionType;

    /**
     * 订单ID, (百度聚屏, )
     */
    private String orderId;

    /**
     * 第三方ID
     */
    private String partnerId;
    /**
     * 实际开始投放时间
     */
    private Date effectiveStartTime;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAdvertisementMaterialId() {
        return advertisementMaterialId;
    }

    public void setAdvertisementMaterialId(String advertisementMaterialId) {
        this.advertisementMaterialId = advertisementMaterialId;
    }

}
