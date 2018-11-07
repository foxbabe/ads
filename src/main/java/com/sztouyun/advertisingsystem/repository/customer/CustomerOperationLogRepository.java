package com.sztouyun.advertisingsystem.repository.customer;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.CustomerOperationLog;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.customer.QCustomerOperationLog;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;

public interface CustomerOperationLogRepository extends BaseRepository<CustomerOperationLog> {
    @Override
    default BooleanBuilder getAuthorizationFilter(){
          return AuthenticationService.getUserAuthenticationFilter(QCustomerOperationLog.customerOperationLog.customer.ownerId);
    }

}
