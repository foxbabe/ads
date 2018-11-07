package com.sztouyun.advertisingsystem.mapper;


import com.sztouyun.advertisingsystem.viewmodel.monitor.RequestStoreRankBaseInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
@CacheConfig(cacheNames = "partners")
public interface PartnerMediaConfigMapper {
    @Cacheable(key = "#p0.getPartnerId()+'_'+#p0.getStoreId()",condition="#p0!=null")
    Boolean existsMediaInfo(StoreInfoRequest info);

    List<RequestStoreRankBaseInfo> findStoreCountByCity(String partnerId);
}
