package com.sztouyun.advertisingsystem.repository.customer;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.customer.CustomerVisit;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.customer.QCustomerVisit;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface CustomerVisitRepository extends BaseRepository<CustomerVisit> {
    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getUserAuthenticationFilter(QCustomerVisit.customerVisit.customer.ownerId);
    }

    boolean existsByCreatorId(String userId);
}
