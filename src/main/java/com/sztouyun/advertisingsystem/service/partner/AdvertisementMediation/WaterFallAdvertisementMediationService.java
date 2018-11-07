package com.sztouyun.advertisingsystem.service.partner.AdvertisementMediation;

import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.mapper.PartnerMediaConfigMapper;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.advertismentSource.AdvertisementSourceEnum;
import com.sztouyun.advertisingsystem.service.partner.advertismentSource.PartnerAdvertisementServiceFactory;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WaterFallAdvertisementMediationService extends BaseService implements IAdvertisementMediationService {
    @Autowired
    private PartnerAdvertisementServiceFactory partnerAdvertisementServiceFactory;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private PartnerMediaConfigMapper partnerMediaConfigMapper;
    @Autowired
    private EnvironmentConfig environmentConfig;

    @Override
    public List<PartnerAdvertisementDeliveryRecord> getPartnerAdvertisements(StoreInfoRequest storeInfoRequest) {
        List<String> partnerIds =cooperationPartnerService.getPrioritizedPartnerIds();
        //测试环境和预发布环境请求的合作方做特殊处理
        if(environmentConfig.isDebug()){
            if("2".equals(storeInfoRequest.getModel())){
                partnerIds = Arrays.asList(AdvertisementSourceEnum.OOHLINK.getPartnerId());
            }else if("1".equals(storeInfoRequest.getModel())){
                partnerIds = Arrays.asList(AdvertisementSourceEnum.BAIDU.getPartnerId());
            }
        }
        for (var partnerId:partnerIds){
            storeInfoRequest.setPartnerId(partnerId);
            var partnerAdvertisementService = partnerAdvertisementServiceFactory.getPartnerAdvertisementService(partnerId);
            if(!environmentConfig.isOnline()){
                logger.info("开始尝试获取合作方："+partnerId+"的广告");
            }
            if(partnerAdvertisementService ==null)
                continue;

            Boolean existsMediaInfo = partnerMediaConfigMapper.existsMediaInfo(storeInfoRequest);
            if(existsMediaInfo == null || !existsMediaInfo)
                continue;

            try{
                var partnerAdvertisements = partnerAdvertisementService.getPartnerAdvertisements(storeInfoRequest);
                if(!CollectionUtils.isEmpty(partnerAdvertisements))
                    return partnerAdvertisements;
            }catch (Exception e){
                logger.warn("请求广告失败,partnerId"+partnerId,e);
            }
        }
        return new ArrayList<>();
    }
}
