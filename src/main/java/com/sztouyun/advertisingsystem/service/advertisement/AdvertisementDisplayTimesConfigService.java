package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.AdvertisementDisplayTimesConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDisplayTimesConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Created by wenfeng on 2017/8/4.
 */
@Service
public class AdvertisementDisplayTimesConfigService extends BaseService {
    @Autowired
    private AdvertisementDisplayTimesConfigRepository advertisementDisplayTimesConfigRepository;

    @Transactional
    public void createAdvertisementDisplayTimesConfig(AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig){
        if(advertisementDisplayTimesConfigRepository.existsByDisplayTimesAndTimeUnit(advertisementDisplayTimesConfig.getDisplayTimes(),advertisementDisplayTimesConfig.getTimeUnit()))
            throw new BusinessException("已存在此类展示次数配置");
        create(advertisementDisplayTimesConfig);
    }

    @Transactional
    public void deleteAdvertisementDisplayTimesConfig(String id){
        boolean existsFlag=advertisementDisplayTimesConfigRepository.exists(id);
        if(!existsFlag)
            throw new BusinessException("展示次数配置不存在！");
        advertisementDisplayTimesConfigRepository.delete(id);
    }

    @Transactional
    public void updateAdvertisementDisplayTimesConfig(AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig){
        if(!advertisementDisplayTimesConfigRepository.exists(advertisementDisplayTimesConfig.getId()))
            throw new BusinessException("展示次数配置不存在！");
        if(advertisementDisplayTimesConfigRepository.existsByIdNotAndDisplayTimesAndTimeUnit(advertisementDisplayTimesConfig.getId(),advertisementDisplayTimesConfig.getDisplayTimes(),advertisementDisplayTimesConfig.getTimeUnit()) )
            throw new BusinessException("已存在此类展示次数配置");
        advertisementDisplayTimesConfig.setUpdatedTime(new Date());
        advertisementDisplayTimesConfigRepository.save(advertisementDisplayTimesConfig);
    }

    public AdvertisementDisplayTimesConfig getAdvertisementDisplayTimesConfig(String id){
        AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig=advertisementDisplayTimesConfigRepository.findOne(id);
        if(null==advertisementDisplayTimesConfig)
            throw new BusinessException("展示次数配置不存在！");
        return advertisementDisplayTimesConfig;
    }

    public Page<AdvertisementDisplayTimesConfig> queryAdvertisementDisplayTimesConfigList(Pageable pageable){
        Page<AdvertisementDisplayTimesConfig> pages=advertisementDisplayTimesConfigRepository.findAll(pageable);
        return pages;
    }

    private void create(AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig){
        AdvertisementDisplayTimesConfig newConfig=new AdvertisementDisplayTimesConfig();
        newConfig.setTimeUnit(advertisementDisplayTimesConfig.getTimeUnit());
        newConfig.setDisplayTimes(advertisementDisplayTimesConfig.getDisplayTimes());
        newConfig.setUpdatedTime(new Date());
        advertisementDisplayTimesConfigRepository.save(newConfig);
    }

}
