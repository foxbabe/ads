package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.system.AdvertisementDisplayTimesConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AdvertisementDisplayTimesConfigRepository extends BaseRepository<AdvertisementDisplayTimesConfig> {
    boolean existsByIdNotAndDisplayTimesAndTimeUnit(String id, Integer displayTimes,Integer timeUnit);

    boolean existsByDisplayTimesAndTimeUnit(Integer displayTimes,Integer timeUnit);

    Page<AdvertisementDisplayTimesConfig> findAll( Pageable pageable);

    List<AdvertisementDisplayTimesConfig> findAll();

}
