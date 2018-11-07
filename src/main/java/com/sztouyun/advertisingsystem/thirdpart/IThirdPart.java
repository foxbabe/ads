package com.sztouyun.advertisingsystem.thirdpart;

import com.sztouyun.advertisingsystem.common.TimeSpan;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeDetailRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOrderRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Map;

public interface IThirdPart {
    Map<String,Integer> getStoresDailyOrder(StoreOrderRequest storeOrderRequest);

    Map<String,TimeSpan> getStoreOpeningTimes(StoreOpeningTimeRequest storeOpeningTimeRequest);

    Page<Date> getStoreOpeningTimeDetail(StoreOpeningTimeDetailRequest storeOpeningTimeDetailsRequest);
}
