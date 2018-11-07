package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoUseCountStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitChartStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitChartStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitListViewModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreInfoExtensionMapper {

    List<StorePortraitChartStatisticInfo> chartStatistic(StorePortraitChartStatisticRequest request);

    StoreInfoUseCountStatistic storePortraitUseCountStatistic();

    Long getStoreInfoExtensionCount(StorePortraitListRequest request);

    List<StorePortraitListViewModel> getStoreInfoExtensionInfo(StorePortraitListRequest request);
}
