package com.sztouyun.advertisingsystem.repository.order;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.order.OrderInfo;
import com.sztouyun.advertisingsystem.model.order.QOrderInfo;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderInfoRepository extends BaseRepository<OrderInfo> {
    @Override
    default BooleanBuilder getAuthorizationFilter(){
       return AuthenticationService.getUserAuthenticationFilter(QOrderInfo.orderInfo.partner.ownerId);
    }

    @Query("select orderInfo.id from  OrderInfo orderInfo where orderInfo.orderStatus=?1 and  orderInfo.endTime < ?2")
    List<String> findAllOrderToFinish(Integer value, Date date);

    @Query("select orderInfo.id from  OrderInfo orderInfo where orderInfo.orderStatus=?1 and  orderInfo.startTime < ?2")
    List<String> findAllOrderToDelivery(Integer value, Date date);

    @Query("select orderInfo.id from  OrderInfo orderInfo where orderInfo.orderStatus=?1 and  orderInfo.createdTime < ?2")
    List<String> findAllOrderToCancel(Integer value, Date date);

    @Modifying
    @Query("update OrderInfo orderInfo set orderInfo.createdTime =?1 where orderInfo.id=?2")
    void updateCreateTime(Date date,String id);
}
