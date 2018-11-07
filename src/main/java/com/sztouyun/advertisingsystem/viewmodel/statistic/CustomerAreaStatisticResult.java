package com.sztouyun.advertisingsystem.viewmodel.statistic;

public class CustomerAreaStatisticResult {

    private String areaId;

    private Long totalCustomer;

    private Long totalSignedCustomer;

    private Long totalCustomerInProvince = 0L;

    public Long getTotalCustomerInProvince() {
        return totalCustomerInProvince;
    }

    public void setTotalCustomerInProvince(Long totalCustomerInProvince) {
        this.totalCustomerInProvince = totalCustomerInProvince;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Long getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(Long totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    public Long getTotalSignedCustomer() {
        return totalSignedCustomer;
    }

    public void setTotalSignedCustomer(Long totalSignedCustomer) {
        this.totalSignedCustomer = totalSignedCustomer;
    }
}
