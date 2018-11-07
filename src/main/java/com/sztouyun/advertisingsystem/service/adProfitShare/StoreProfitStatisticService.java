package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.mapper.StoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.QPeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.QStoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.adProfitShare.StoreProfitStatistic;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.adProfitShare.PeriodStoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.StoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StoreProfitStatisticService extends BaseService {
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private StoreProfitStatisticRepository storeProfitStatisticRepository;
    @Autowired
    private StoreProfitStatisticMapper storeProfitStatisticMapper;
	@Autowired
    private StoreInfoMapper storeInfoMapper;
	@Autowired
    private PeriodStoreProfitStatisticRepository periodStoreProfitStatisticRepository;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    private final QStoreProfitStatistic qStoreProfitStatistic = QStoreProfitStatistic.storeProfitStatistic;
    private final QPeriodStoreProfitStatistic qPeriodStoreProfitStatistic= QPeriodStoreProfitStatistic.periodStoreProfitStatistic;

    public StoreProfitStatisticViewModel getStoreProfitStatistic(String id){
        if(!storeInfoRepository.exists(id))
            throw new BusinessException("门店ID无效");
        StoreProfitStatisticViewModel storeProfitStatisticViewModel = storeProfitStatisticMapper.getAdvertisementStatistic(id);
        if(storeProfitStatisticViewModel.getStartDate() != null){
            Double totalAmount = storeProfitStatisticMapper.sumProfitAmount(id);
            storeProfitStatisticViewModel.setTotalShareAmount(NumberFormatUtil.formatAbandon(totalAmount,2));
            storeProfitStatisticViewModel.setEndDate(new Date());
            return storeProfitStatisticViewModel;
        }
        return new StoreProfitStatisticViewModel();
    }

    public List<StoreProfitStatistic> getStoreProfitCurve(StoreProfitCurveRequest storeProfitCurveRequest){
        if(!storeInfoRepository.exists(storeProfitCurveRequest.getStoreId()))
            throw new BusinessException("门店ID无效");

        Date endDate = DateUtils.addDays(storeProfitCurveRequest.getEndDate(), 1);
        Iterable<StoreProfitStatistic> iterable = storeProfitStatisticRepository.findAll(qStoreProfitStatistic.storeId
                .eq(storeProfitCurveRequest.getStoreId()).and(qStoreProfitStatistic.profitDate.goe(storeProfitCurveRequest.getStartDate())
                        .and(qStoreProfitStatistic.profitDate.lt(endDate))), new Sort(Sort.Direction.ASC, "profitDate"));
        return Lists.newArrayList(iterable);
    }

    public ProfitOverview getProfitOverviewInfo(){
        return storeProfitStatisticMapper.getProfitOverviewInfo();
    }

    public List<ProfitSettledInfo> getProfitOverviewStatistic(ProfitOverviewRequest profitOverviewRequest){
        profitOverviewRequest.setStartDate(new LocalDate(profitOverviewRequest.getStartDate()).withDayOfMonth(1).toDate());
        profitOverviewRequest.setEndDate(new LocalDate(profitOverviewRequest.getEndDate()).withDayOfMonth(1).plusMonths(1).toDate());
        return storeProfitStatisticMapper.getProfitOverviewStatistic(profitOverviewRequest);
    }

    public Page<StoreProfitBean> getUnsettledStoreProfitList(UnsettledPeriodStoreProfitListRequest request){
        if(request.getAreaIds().contains(Constant.AREA_ABNORMAL_NODE_ID)){
            request.setHasAbnormalNode(Boolean.TRUE);
        }
       Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qStoreProfitStatistic.profitDate.desc()));
        Long count=storeInfoMapper.getUnsettledStoreProfitCount(request);
        if(count==0L)
           return  pageResult(new ArrayList<StoreProfitBean>(),pageable,count);
        if(request.getSettledMonth()==null && StringUtils.isEmpty(request.getId()))
            return pageResult(storeInfoMapper.getUnsettledStoreProfitList(request),pageable,count);

        return pageResult(storeInfoMapper.getUnsettledStoreProfitListWithSettleMonth(request),pageable,count);
    }

    public BooleanBuilder filter(StoreProfitListRequest request,QStoreProfitStatistic qStoreProfitStatistic){
        BooleanBuilder predicate = new BooleanBuilder();
        if(request.getStartTime()!=null){
            predicate.and(qStoreProfitStatistic.profitDate.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qStoreProfitStatistic.profitDate.loe(request.getEndTime()));
        }
        if(!StringUtils.isEmpty(request.getStoreName())){
            predicate.and(qStoreInfo.storeName.contains(request.getStoreName()));
        }
        if(!StringUtils.isEmpty(request.getProvinceId())){
            predicate.and(qStoreInfo.provinceId.eq(request.getProvinceId()));
        }
        if(!StringUtils.isEmpty(request.getCityId())){
            predicate.and(qStoreInfo.cityId.eq(request.getCityId()));
        }
        if(!StringUtils.isEmpty(request.getRegionId())){
            predicate.and(qStoreInfo.regionId.eq(request.getRegionId()));
        }
        if(!StringUtils.isEmpty(request.getDeviceId())){
            predicate.and(qStoreInfo.deviceId.contains(request.getDeviceId()));
        }
        if(request.getSettled()!=null){
            predicate.and(qStoreProfitStatistic.settled.eq(request.getSettled()));
        }
        predicate.and(qStoreProfitStatistic.shareAmount.gt(0D));
        return predicate;
    }


    public StoreProfitStatistic getStoreProfitStatisticDay(String storeProfitStatisticId) {
        if(!storeProfitStatisticRepository.exists(storeProfitStatisticId))
            throw new BusinessException("门店ID无效");
        return storeProfitStatisticRepository.findOne(storeProfitStatisticId);
    }

    @Transactional
    public void deleteStoreProfitStatistic(List<String> storeProfitStatisticIds) {
        storeProfitStatisticMapper.deleteStoreProfitStatistic(storeProfitStatisticIds);
    }
    public List<Area> getAreaOfUnsettledStoreProfit(String settledStoreProfitId){
        settledStoreProfitId=settledStoreProfitId==null?"":settledStoreProfitId;
        return storeInfoMapper.getUnsettledAreaInfo(settledStoreProfitId);
    }

    public List<String> getCheckedUnsettledAreaIds(String settledStoreProfitId){
        return StringUtils.isEmpty(settledStoreProfitId)?new ArrayList<>():storeInfoMapper.getCheckedUnsettledAreaIds(settledStoreProfitId);
    }

    public void addSpecialNode(List<Area> areas) {
        areas.add(new Area(Constant.AREA_CONTAIN_ALL_NODE_NAME
                , Constant.TREE_ROOT_ID, null));
        if(existStoreProfitCountInAbnormal()) {
            areas.add(0, new Area(Constant.AREA_ABNORMAL_NODE_NAME, Constant.AREA_ABNORMAL_NODE_ID, Constant.TREE_ROOT_ID));
        }
    }
    private Boolean existStoreProfitCountInAbnormal(){
        return periodStoreProfitStatisticRepository.exists(qPeriodStoreProfitStatistic.storeInfo.provinceId.eq("").or(qPeriodStoreProfitStatistic.storeInfo.cityId.eq("")).or(qPeriodStoreProfitStatistic.storeInfo.regionId.eq("")).and(qPeriodStoreProfitStatistic.settled.eq(Boolean.FALSE))
        .and(qPeriodStoreProfitStatistic.shareAmount.gt(0)));
    }

    public List<Area> getAreasWithPeriodProfit() {
        return storeProfitStatisticMapper.getAreasWithPeriodProfit();
    }
}