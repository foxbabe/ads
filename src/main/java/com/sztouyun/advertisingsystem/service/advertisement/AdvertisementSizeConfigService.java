package com.sztouyun.advertisingsystem.service.advertisement;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.QContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.system.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.model.system.QAdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSizeConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementPositionConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdvertisementSizeConfigService extends BaseService {

    @Autowired
    private AdvertisementSizeConfigRepository sizeConfigRepository;
    @Autowired
    private ContractAdvertisementPositionConfigRepository contractAdvertisementPositionConfigRepository;

    private final QAdvertisementSizeConfig qAdvertisementSizeConfig = QAdvertisementSizeConfig.advertisementSizeConfig;
    private final QContractAdvertisementPositionConfig qContractAdvertisementPositionConfig=QContractAdvertisementPositionConfig.contractAdvertisementPositionConfig;

    @Transactional
    public void createSizeConfig(AdvertisementSizeConfig sizeConfig) {
        boolean exists = sizeConfigRepository.exists(
                qAdvertisementSizeConfig.advertisementPositionType.eq(sizeConfig.getAdvertisementPositionType()).and(qAdvertisementSizeConfig.terminalType.eq(sizeConfig.getTerminalType())));

        if(exists)
            throw new BusinessException("已经存在对应的尺寸配置");

        sizeConfigRepository.save(sizeConfig);
    }

    @Transactional
    public void updateSizeConfig(AdvertisementSizeConfig sizeConfig) {
        boolean exists = sizeConfigRepository.exists(sizeConfig.getId());
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");

        boolean hasRepeat = sizeConfigRepository.exists(
            qAdvertisementSizeConfig.advertisementPositionType.eq(sizeConfig.getAdvertisementPositionType()).and(qAdvertisementSizeConfig.terminalType.eq(sizeConfig.getTerminalType())).and(qAdvertisementSizeConfig.id.ne(sizeConfig.getId())));

        if(hasRepeat)
            throw new BusinessException("已经存在对应的尺寸配置");

        sizeConfigRepository.save(sizeConfig);
    }

    public AdvertisementSizeConfig getSizeConfig(String id) {
        boolean exists = sizeConfigRepository.exists(id);
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");

        return sizeConfigRepository.findOne(qAdvertisementSizeConfig.id.eq(id));
    }

    public void deleteSizeConfig(String id) {
        boolean exists = sizeConfigRepository.exists(id);
        if(!exists)
            throw new BusinessException("广告尺寸配置数据不存在");
        if(contractAdvertisementPositionConfigRepository.exists(qContractAdvertisementPositionConfig.advertisementSizeConfigId.eq(id)))
            throw new BusinessException("广告尺寸配置存在业务关联，不允许删除");
        sizeConfigRepository.delete(id);
    }

    public Page<AdvertisementSizeConfig> getAllSizeConfitList(Pageable pageable) {
        return sizeConfigRepository.findAll(pageable);
    }

}

