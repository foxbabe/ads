package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.store.StoreNearBy;
import com.sztouyun.advertisingsystem.viewmodel.DayPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNearByUpdateInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.EnvironmentTypeStoreStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreNearByMapper {
    List<EnvironmentTypeStoreStatistics> getEnvironmentTypeStoreStatistics();

    void syncStoreNearBy(List<StoreNearBy> list);

    List<StoreNearByUpdateInfo> findNeedSyncStoreInfo(DayPageRequest request);
}
