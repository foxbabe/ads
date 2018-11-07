package com.sztouyun.advertisingsystem.repository.store;


import com.sztouyun.advertisingsystem.model.store.StorePortrait;
import com.sztouyun.advertisingsystem.repository.BaseAutoKeyRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface StorePortraitRepository extends BaseAutoKeyRepository<StorePortrait> {
    @Modifying
    @Transactional
    @Query(value = "delete from StorePortrait where storeId=:storeId")
    void deleteStorePortraitByStoreId(@Param("storeId") String storeId);
}
