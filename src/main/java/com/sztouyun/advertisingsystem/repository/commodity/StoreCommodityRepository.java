package com.sztouyun.advertisingsystem.repository.commodity;


import com.sztouyun.advertisingsystem.model.store.StoreCommodity;
import com.sztouyun.advertisingsystem.repository.BaseAutoKeyRepository;

public interface StoreCommodityRepository extends BaseAutoKeyRepository<StoreCommodity> {
     void deleteByStoreIdAndAndCommodityId(String storeId,Long commodityId);
}
