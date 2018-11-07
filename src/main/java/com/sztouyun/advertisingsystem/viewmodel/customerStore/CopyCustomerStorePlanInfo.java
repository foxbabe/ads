package com.sztouyun.advertisingsystem.viewmodel.customerStore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CopyCustomerStorePlanInfo {
    private String tempCustomerStorePlanId;

    private String oldCustomerStorePlanId;

    public CopyCustomerStorePlanInfo(String tempCustomerStorePlanId, String oldCustomerStorePlanId) {
        this.tempCustomerStorePlanId = tempCustomerStorePlanId;
        this.oldCustomerStorePlanId = oldCustomerStorePlanId;
    }
}
