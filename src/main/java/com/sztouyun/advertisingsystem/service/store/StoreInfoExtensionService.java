package com.sztouyun.advertisingsystem.service.store;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.mapper.StoreInfoExtensionMapper;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.store.QStoreInfoExtension;
import com.sztouyun.advertisingsystem.model.store.QStorePortrait;
import com.sztouyun.advertisingsystem.model.store.StoreInfoExtension;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoExtensionRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoUseCountStatistic;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitChartStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitChartStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.StorePortraitListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StoreInfoExtensionService extends BaseService{

    @Autowired
    private StoreInfoExtensionRepository storeInfoExtensionRepository;
    @Autowired
    private StoreInfoExtensionMapper storeInfoExtensionMapper;

    private static final QStoreInfoExtension qStoreInfoExtension=QStoreInfoExtension.storeInfoExtension;
    private static final QStoreInfo qStoreInfo=QStoreInfo.storeInfo;
    private static final QStorePortrait qStorePortrait=QStorePortrait.storePortrait;


    public List<StorePortraitChartStatisticInfo> chartStatistic(StorePortraitChartStatisticRequest request) {
        return storeInfoExtensionMapper.chartStatistic(request);
    }

    public Page<StoreInfoExtension> storeList(StorePortraitListRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qStoreInfoExtension.storeInfo.storeNo.asc()));
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qStoreInfo.deleted.isFalse()).and(qStoreInfo.storeType.gt(0));
        if(!StringUtils.isEmpty(request.getProvinceId())){
            predicate.and(qStoreInfo.provinceId.eq(request.getProvinceId()));
        }
        if(!StringUtils.isEmpty(request.getCityId())){
            predicate.and(qStoreInfo.cityId.eq(request.getCityId()));
        }
        if(!StringUtils.isEmpty(request.getRegionId())){
            predicate.and(qStoreInfo.regionId.eq(request.getRegionId()));
        }
        if(!StringUtils.isEmpty(request.getCommercialArea())) {
            predicate.and(qStoreInfoExtension.commercialArea.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getCommercialArea(), Constant.SEPARATOR)));
        }
        if(!StringUtils.isEmpty(request.getDailySales())) {
            predicate.and(qStoreInfoExtension.dailySales.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getDailySales(), Constant.SEPARATOR)));
        }
        if(!StringUtils.isEmpty(request.getDecoration())) {
            predicate.and(qStoreInfoExtension.decoration.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getDecoration(), Constant.SEPARATOR)));
        }
        if(!StringUtils.isEmpty(request.getOrderRatio())) {
            predicate.and(qStoreInfoExtension.orderRatio.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getOrderRatio(), Constant.SEPARATOR)));
        }
        if(!StringUtils.isEmpty(request.getStoreFrontType())) {
            predicate.and(qStoreInfoExtension.storeFrontType.in(com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getStoreFrontType(), Constant.SEPARATOR)));
        }
        if(!StringUtils.isEmpty(request.getSurroundingsDistrict())) {
            List<Integer> list = com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(request.getSurroundingsDistrict(), Constant.SEPARATOR);
            list.forEach(s ->predicate.and(qStoreInfoExtension.storePortraits.any().value.eq(s)));
        }

        return storeInfoExtensionRepository.findAll(predicate, pageable,new JoinDescriptor().innerJoin(qStoreInfoExtension.storeInfo,qStoreInfo));
    }

    public StoreInfoUseCountStatistic storePortraitUseCountStatistic() {
        return storeInfoExtensionMapper.storePortraitUseCountStatistic();
    }

    public Long getStoreInfoExtensionCount(StorePortraitListRequest request){
        return storeInfoExtensionMapper.getStoreInfoExtensionCount(request);
    }

    public List<StorePortraitListViewModel> getStoreInfoExtensionInfo(StorePortraitListRequest request){
        return storeInfoExtensionMapper.getStoreInfoExtensionInfo(request);
    }

}
