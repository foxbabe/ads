package com.sztouyun.advertisingsystem.viewmodel.monitor;

public class DeliveryMonitorStatistic {

    public DeliveryMonitorStatistic() {
    }

    public DeliveryMonitorStatistic(String storeId,Integer displayTimes) {
        this.displayTimes = displayTimes;
        this.storeId = storeId;
    }

    public DeliveryMonitorStatistic(String advertisementId, String storeId, Integer displayTimes) {
        this.displayTimes = displayTimes;
        this.advertisementId = advertisementId;
        this.storeId = storeId;
    }

    private String id;

    private Integer displayTimes;

    private String advertisementId;

    private String storeId;

    private String contractId;

    private Integer terminalType;

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDisplayTimes() {
        return displayTimes;
    }

    public void setDisplayTimes(Integer displayTimes) {
        this.displayTimes = displayTimes;
    }
}
