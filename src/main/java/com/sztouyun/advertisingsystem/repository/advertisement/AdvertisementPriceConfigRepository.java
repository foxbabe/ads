package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.system.AdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface AdvertisementPriceConfigRepository extends BaseRepository<AdvertisementPriceConfig> {
    boolean exists(String id);

    @Query(value = "select count(1) from  AdvertisementPriceConfig advertisementPriceConfig where advertisementPriceConfig.storeType=?1")
    Integer  getCountByStoreType(Integer storeType);


    boolean existsByStoreType(Integer storeType);

    boolean existsByStoreTypeAndActived(Integer storeType,Boolean isActive);

    boolean existsByIdNotAndStoreTypeAndActived(String id,Integer storeType,Boolean isActive);

    Page<AdvertisementPriceConfig> findAllByActived(Boolean isActive,Pageable pageable);

    Page<AdvertisementPriceConfig> findAllByStoreTypeAndActived(Boolean isActive,Integer storeType,Pageable pageable);

    List<AdvertisementPriceConfig> findAllByActived(Boolean enableFlag);

    AdvertisementPriceConfig findFirstByStoreTypeOrderByCreatedTimeAsc(Integer storeType);

    /**
     * 根据合同审核通过时间查询当时的配置（auditingTime，auditingTime2是审核通过时间）
     * @param auditingTime
     * @param auditingTime2
     * @return
     */
    List<AdvertisementPriceConfig> findAllByCreatedTimeBeforeAndUpdatedTimeAfter(Date auditingTime,Date auditingTime2);
}
