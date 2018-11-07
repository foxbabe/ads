package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementPositionConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by szty on 2018/7/3.
 */
@Mapper
@CacheConfig(cacheNames = "advertisementPositionConfigs")
public interface ContractAdvertisementPositionConfigMapper {

    @Cacheable(key = "#p0",condition="#p0!=null")
    AdvertisementPositionConfig getAdvertisementPositionConfig(String urlStepId);
}
