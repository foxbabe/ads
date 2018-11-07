package com.sztouyun.advertisingsystem.repository.customerStore;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlan;
import com.sztouyun.advertisingsystem.model.customerStore.QCustomerStorePlan;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import lombok.experimental.var;

/**
 * Created by szty on 2018/5/15.
 */
public interface CustomerStorePlanRepository extends BaseRepository<CustomerStorePlan> {

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        var user =AuthenticationService.getUser();
        //广告客户加载创建人为当前客户下所有的选点记录数据
        if(user.getRoleTypeEnum().equals(RoleTypeEnum.AdvertisementCustomer))
            return new BooleanBuilder(QCustomerStorePlan.customerStorePlan.creatorId.eq(user.getId()));

        return AuthenticationService.getUserAuthenticationFilter(QCustomerStorePlan.customerStorePlan.customer.ownerId);
    }

}
