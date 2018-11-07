package com.sztouyun.advertisingsystem.repository.store;


import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreInfoRepository extends BaseRepository<StoreInfo> {
    @Query(value = "select count(1) from  StoreInfo storeInfo where storeInfo.deleted=?1")
    int getCountByDeleted(boolean deleted);
}
