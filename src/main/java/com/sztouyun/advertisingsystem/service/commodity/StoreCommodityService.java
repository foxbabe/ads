package com.sztouyun.advertisingsystem.service.commodity;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.store.QStoreCommodity;
import com.sztouyun.advertisingsystem.model.store.StoreCommodity;
import com.sztouyun.advertisingsystem.repository.commodity.StoreCommodityRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StoreCommodityService extends BaseService {
    @Autowired
    private StoreCommodityRepository storeCommodityRepository;
    private QStoreCommodity qStoreCommodity=QStoreCommodity.storeCommodity;
    @Transactional
    public void create(StoreCommodity storeCommodity){
        if(existCommodity(storeCommodity))
            throw new BusinessException("该门店已存在该商品");
        storeCommodityRepository.save(storeCommodity);
    }

    @Transactional
    public void delete(StoreCommodity storeCommodity){
        if(!existCommodity(storeCommodity))
            throw new BusinessException("该门店不存在该商品");
        storeCommodityRepository.deleteByStoreIdAndAndCommodityId(storeCommodity.getStoreId(),storeCommodity.getCommodityId());
    }

    private Boolean existCommodity(StoreCommodity storeCommodity){
        return  storeCommodityRepository.exists(qStoreCommodity.storeId.eq(storeCommodity.getStoreId()).and(qStoreCommodity.commodityId.eq(storeCommodity.getCommodityId())));
    }
}
