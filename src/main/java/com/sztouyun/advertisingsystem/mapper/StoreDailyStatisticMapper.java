package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.store.StoreDailyStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNumStatistics;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreAdFillTrendRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreDailyStatisticMapper {
    void saveStoreDailyStatistics(List<StoreDailyStatistic> storeDailyStatistics);

    List<StoreNumStatistics> getStoreNumStatistics(StoreAdFillTrendRequest request);
}
