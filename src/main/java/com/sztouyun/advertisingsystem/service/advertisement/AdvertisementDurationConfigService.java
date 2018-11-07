package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.AdvertisementDurationConfig;
import com.sztouyun.advertisingsystem.model.system.QAdvertisementDurationConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementDurationConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdvertisementDurationConfigService extends BaseService {

    @Autowired
    private AdvertisementDurationConfigRepository advertisementDurationConfigRepository;

    QAdvertisementDurationConfig qAdvertisementDurationConfig = QAdvertisementDurationConfig.advertisementDurationConfig;

    @Transactional
    public void createDurationConfig(AdvertisementDurationConfig advertisementDurationConfig) {
        boolean exists = advertisementDurationConfigRepository.exists(
                qAdvertisementDurationConfig.duration.eq(advertisementDurationConfig.getDuration()).and(qAdvertisementDurationConfig.durationUnit.eq(advertisementDurationConfig.getDurationUnit())));
        if(exists)
            throw new BusinessException("已存在对应的时长配置");
        advertisementDurationConfigRepository.save(advertisementDurationConfig);
    }

    @Transactional
    public void updateDurationConfig(AdvertisementDurationConfig advertisementDurationConfig) {
        boolean exists = advertisementDurationConfigRepository.exists(qAdvertisementDurationConfig.id.eq(advertisementDurationConfig.getId()));
        if(!exists)
            throw new BusinessException("广告时长配置不存在");
        boolean hasRepeat = advertisementDurationConfigRepository.exists(
                qAdvertisementDurationConfig.duration.eq(advertisementDurationConfig.getDuration())
                        .and(qAdvertisementDurationConfig.durationUnit.eq(advertisementDurationConfig.getDurationUnit()).and(qAdvertisementDurationConfig.id.ne(advertisementDurationConfig.getId()))));
        if(hasRepeat)
            throw new BusinessException("已存在对应的时长配置");
        advertisementDurationConfigRepository.save(advertisementDurationConfig);
    }

    public AdvertisementDurationConfig getDurationConfig(String id) {
        boolean exists = advertisementDurationConfigRepository.exists(qAdvertisementDurationConfig.id.eq(id));
        if(!exists)
            throw new BusinessException("广告时长配置不存在");
        return advertisementDurationConfigRepository.findOne(id);
    }

    @Transactional
    public void deleteDurationConfig(String id) {
        boolean exists = advertisementDurationConfigRepository.exists(qAdvertisementDurationConfig.id.eq(id));
        if(!exists)
            throw new BusinessException("广告时长配置不存在");
        advertisementDurationConfigRepository.delete(id);
    }


    public Page<AdvertisementDurationConfig> getAllDurationConfigList(Pageable pageable) {
        return advertisementDurationConfigRepository.findAll(pageable);
    }


}

