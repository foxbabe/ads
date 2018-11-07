package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.order.OrderDetail;
import com.sztouyun.advertisingsystem.model.order.OrderDetailDate;
import com.sztouyun.advertisingsystem.model.order.OrderDetailStore;
import com.sztouyun.advertisingsystem.viewmodel.order.DailyDeliveryStoreStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.order.DailyStoreCountItem;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreOrderRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreOrderViewModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void saveOrderDetails(List<OrderDetail> orderDetails);

    void saveOrderDetailDates(List<OrderDetailDate> orderDetailDates);

    void saveOrderDetailStores(List<OrderDetailStore> orderDetailStores);

    List<StoreOrderViewModel> getOrderStoreList(StoreOrderRequest request);

    Long getOrderStoreListCount(StoreOrderRequest request);

    List<DailyStoreCountItem> getDailyStoreCountStatistic(DailyDeliveryStoreStatisticRequest request);

    Long getTotalStoreCountWithDate(String orderId);

}
