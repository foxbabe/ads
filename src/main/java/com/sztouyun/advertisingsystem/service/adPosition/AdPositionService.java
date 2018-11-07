package com.sztouyun.advertisingsystem.service.adPosition;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementPositionMapper;
import com.sztouyun.advertisingsystem.model.adPosition.AdPosition;
import com.sztouyun.advertisingsystem.model.adPosition.QAdPosition;
import com.sztouyun.advertisingsystem.repository.adPosition.AdPositionRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class AdPositionService extends BaseService {

    @Autowired
    private AdvertisementPositionMapper advertisementPositionMapper;

    @Autowired
    private AdPositionRepository adPositionRepository;
    private final QAdPosition qAdPosition = QAdPosition.adPosition;

    @Transactional
    public void createAdsPosition(AdPosition adsPosition){
        if(adPositionRepository.exists(qAdPosition.storeType.eq(adsPosition.getStoreType())))
            throw new BusinessException("类别类型已经存在");

        adsPosition.setCreatorId(getUser().getId());
        adPositionRepository.save(adsPosition);
    }

    @Transactional
    public void updateAdsPosition(AdPosition adsPosition){
        if(!adPositionRepository.exists(adsPosition.getId()))
            throw new BusinessException("类别广告位配置不存在");
        if(adPositionRepository.exists(qAdPosition.id.ne(adsPosition.getId()).and(qAdPosition.storeType.eq(adsPosition.getStoreType()))))
            throw new BusinessException("类别类型已存在");
        adsPosition.setUpdatedTime(new Date());
        adPositionRepository.save(adsPosition);
    }

    @Transactional
    public void deleteAdsPosition(String id){
        if(!adPositionRepository.exists(qAdPosition.id.eq(id)))
            throw new BusinessException("类别广告位配置不存在");
        adPositionRepository.delete(id);
    }

    public Page<AdPosition> findByPage(Pageable pageable){
        Page<AdPosition> pages= adPositionRepository.findAll(pageable);
        return  pages;
    }

    public AdPosition getAdsPosition(String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("类别广告位配置ID不能为空！");
        AdPosition adsPosition = adPositionRepository.findOne(id);
        if(null == adsPosition)
            throw new BusinessException("该类别广告位配置不存在");
        return adsPosition;
    }

    public List<DistributionStatisticDto> getAdvertisementPositionWithType(String contractId) {
        return advertisementPositionMapper.getAvailableStoreCountWithStoreType(contractId);
    }

}