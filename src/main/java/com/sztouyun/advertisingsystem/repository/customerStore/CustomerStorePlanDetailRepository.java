package com.sztouyun.advertisingsystem.repository.customerStore;

import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlanDetail;
import com.sztouyun.advertisingsystem.repository.BaseAutoKeyRepository;

/**
 * Created by szty on 2018/5/15.
 */
public interface CustomerStorePlanDetailRepository extends BaseAutoKeyRepository<CustomerStorePlanDetail> {
    int deleteCustomerStorePlanDetailByCustomerStorePlanId(String customerStorePlanId);
}
