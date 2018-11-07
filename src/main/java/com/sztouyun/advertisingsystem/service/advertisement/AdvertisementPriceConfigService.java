package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.model.system.QAdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementPriceConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
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
public class AdvertisementPriceConfigService extends BaseService {

    private final QAdvertisementPriceConfig qAdvertisementPriceConfig = QAdvertisementPriceConfig.advertisementPriceConfig;

    @Autowired
    private AdvertisementPriceConfigRepository advertisementPriceConfigRepository;

    @Transactional
    public void createAdvertisementPriceConfig(AdvertisementPriceConfig advertisementPriceConfig){
        if(advertisementPriceConfigRepository.existsByStoreTypeAndActived(advertisementPriceConfig.getStoreType(),true))
            throw new BusinessException("已存在此类别的门店价格配置");
        advertisementPriceConfig.setStartTime(new Date());
        create(advertisementPriceConfig);
    }

    @Transactional
    public void deleteAdvertisementPriceConfig(String id){
        boolean existsFlag=advertisementPriceConfigRepository.exists(id);
        if(!existsFlag)
            throw new BusinessException("广告价格配置不存在！");
        advertisementPriceConfigRepository.delete(id);
    }

    @Transactional
    public void updateAdvertisementPriceConfig(AdvertisementPriceConfig advertisementPriceConfig){
        AdvertisementPriceConfig oldAdvertisementPriceConfig=advertisementPriceConfigRepository.findOne(advertisementPriceConfig.getId());
        if(null==oldAdvertisementPriceConfig)
            throw new BusinessException("广告价格配置不存在！");
        if(!oldAdvertisementPriceConfig.getActived())
            throw new BusinessException("广告价格配置已经过期，不允许修改！");
        if(advertisementPriceConfigRepository.existsByIdNotAndStoreTypeAndActived(advertisementPriceConfig.getId(),advertisementPriceConfig.getStoreType(),true) )
            throw new BusinessException("已存在此类别的门店价格配置");
        oldAdvertisementPriceConfig.setActived(false);
        oldAdvertisementPriceConfig.setUpdatedTime(new Date());
        advertisementPriceConfigRepository.save(oldAdvertisementPriceConfig);
        advertisementPriceConfig.setStartTime(oldAdvertisementPriceConfig.getStartTime());
        create(advertisementPriceConfig);
    }

    public AdvertisementPriceConfig getAdvertisementPriceConfig(String id){
        AdvertisementPriceConfig advertisementPriceConfig=advertisementPriceConfigRepository.findOne(id);
        if(null==advertisementPriceConfig)
            throw new BusinessException("广告价格配置不存在！");
        return advertisementPriceConfig;
    }

    public Page<AdvertisementPriceConfig> queryAdvertisementPriceConfigList(Boolean isActive,Integer storeType, Pageable pageable){
        Page<AdvertisementPriceConfig> pages=null;
        if(storeType>0){
            pages=advertisementPriceConfigRepository.findAllByStoreTypeAndActived(isActive,storeType, pageable );
        }else{
            pages=advertisementPriceConfigRepository.findAllByActived(isActive,pageable);
        }
        return pages;
    }

    private void create(AdvertisementPriceConfig advertisementPriceConfig){
        AdvertisementPriceConfig newConfig=new AdvertisementPriceConfig();
        newConfig.setUnitQuantity(advertisementPriceConfig.getUnitQuantity());
        newConfig.setPrice(advertisementPriceConfig.getPrice());
        newConfig.setStoreType(advertisementPriceConfig.getStoreType());
        newConfig.setPeriod(advertisementPriceConfig.getPeriod());
        newConfig.setStartTime(advertisementPriceConfig.getStartTime());
        newConfig.setUpdatedTime(DateUtils.getMaxDate());
        advertisementPriceConfigRepository.save(newConfig);
    }

    public boolean hasCompletedPriceConfig() {
        long completedPriceConfigCount = advertisementPriceConfigRepository.count(
                queryFactory -> queryFactory.selectDistinct(qAdvertisementPriceConfig.storeType).from(qAdvertisementPriceConfig).where(qAdvertisementPriceConfig.actived.eq(true)));
        return completedPriceConfigCount == StoreTypeEnum.values().length;
    }

    public Date findFirstCreateTimeByType(Integer storeType){
        return advertisementPriceConfigRepository.findFirstByStoreTypeOrderByCreatedTimeAsc(storeType).getCreatedTime();
    }
}
