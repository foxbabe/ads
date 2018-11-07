package com.sztouyun.advertisingsystem.service.adProfitShare;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementProfitStatisticMapper;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.mapper.StoreProfitStatisticMapper;
import com.sztouyun.advertisingsystem.model.adProfitShare.*;
import com.sztouyun.advertisingsystem.repository.adProfitShare.PeriodStoreProfitStatisticRepository;
import com.sztouyun.advertisingsystem.repository.adProfitShare.SettledStoreProfitRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitSettledInfo;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.SettledDetailAdvertisementStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.SettledStoreManageRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.UnsettledPeriodStoreProfitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/1/15.
 */
@Service
public class SettledStoreProfitService extends BaseService {
    @Autowired
    private SettledStoreProfitRepository settledStoreProfitRepository;
    @Autowired
    private PeriodStoreProfitStatisticRepository periodStoreProfitStatisticRepository;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private StoreProfitStatisticMapper storeProfitStatisticMapper;
    @Autowired
    private AdvertisementProfitStatisticMapper advertisementProfitStatisticMapper;

    private static final QSettledStoreProfit qSettledStoreProfit = QSettledStoreProfit.settledStoreProfit;
    private static final QPeriodStoreProfitStatistic  qPeriodStoreProfitStatistic=QPeriodStoreProfitStatistic.periodStoreProfitStatistic;


    public SettledStoreProfit getProfitSettledInfo(String id){
        SettledStoreProfit settledStoreProfit = settledStoreProfitRepository.findById(id);
        if(null == settledStoreProfit)
            throw new BusinessException("结算信息不存在！");
        return settledStoreProfit;
    }

    public Page<SettledStoreProfit> getSettledManageList(SettledStoreManageRequest request) {
        if(!StringUtils.isEmpty(request.getSettledAmountDown()) && request.getSettledAmountDown() <= 0)
            throw new BusinessException("下区间不能小于等于0");
        if(!StringUtils.isEmpty(request.getSettledAmountDown()) && !StringUtils.isEmpty(request.getSettledAmountUp())
           && request.getSettledAmountDown() <= request.getSettledAmountUp())
            throw new BusinessException("下区间的值必须大于上区间的值！");

        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(),new QSort(qSettledStoreProfit.createdTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(request.getSelDate())) {
            predicate.and(qSettledStoreProfit.settledMonth.eq(DateUtils.strToDate(DateUtils.dateFormat(request.getSelDate(), Constant.DATA_YM)+"-01",Constant.DATA_YM_01)));
        }
        if(!StringUtils.isEmpty(request.getSettledAmountUp())){
            predicate.and(qSettledStoreProfit.settledAmount.goe(request.getSettledAmountUp()));
        }
        if(!StringUtils.isEmpty(request.getSettledAmountDown())){
            predicate.and(qSettledStoreProfit.settledAmount.loe(request.getSettledAmountDown()));
        }
        if(request.getSettleStatus() != null && request.getSettleStatus() > 1){
            predicate.and(qSettledStoreProfit.settleStatus.eq(request.getSettleStatus()));
        }

        return settledStoreProfitRepository.findAllAuthorized(predicate, pageable);
    }

    @Transactional
    public void emptyBySettledStoreProfitId(String settledStoreProfitId) {
        periodStoreProfitStatisticRepository.deleteBySettledStoreProfitId(settledStoreProfitId);
        SettledStoreProfit settledStoreProfit=settledStoreProfitRepository.findOne(settledStoreProfitId);
        settledStoreProfit.setSettledAmount(0D);
        settledStoreProfit.setStoreCount(0);
        settledStoreProfit.setStreamCount(0);
        settledStoreProfitRepository.save(settledStoreProfit);
    }

    @Transactional
    public SettledStoreProfit create(String periodStoreProfitStatisticId,Date settledMonth){
        PeriodStoreProfitStatistic storeProfitStatistic=periodStoreProfitStatisticRepository.findOne(periodStoreProfitStatisticId);
        if(storeProfitStatistic==null)
            throw  new BusinessException("门店日流水ID无效");
        SettledStoreProfit settledStoreProfit= new SettledStoreProfit();
        settledStoreProfit.setSettledMonth(settledMonth);
        settledStoreProfit.setStoreCount(0);
        return createOrUpdateSettledStoreProfit(storeProfitStatistic, settledStoreProfit);
    }


    private SettledStoreProfit createOrUpdateSettledStoreProfit(PeriodStoreProfitStatistic periodStoreProfitStatistic, SettledStoreProfit settledStoreProfit) {
        Boolean existStore=periodStoreProfitStatisticRepository.exists(qPeriodStoreProfitStatistic.storeId.eq(periodStoreProfitStatistic.getStoreId()).and(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(settledStoreProfit.getId())));
        if(!existStore) {
            settledStoreProfit.addStoreCount();
        }
        settledStoreProfit.addStreamCount();
        settledStoreProfit.setSettledAmount((settledStoreProfit.getSettledAmount()*100+periodStoreProfitStatistic.getShareAmount()*100)/100);
        if(settledStoreProfit.getStoreCount()==0){
            settledStoreProfit.setSettledMonth(periodStoreProfitStatistic.getSettledMonth());
        }
        settledStoreProfit=settledStoreProfitRepository.save(settledStoreProfit);
        periodStoreProfitStatistic.setSettledStoreProfitId(settledStoreProfit.getId());
        periodStoreProfitStatisticRepository.save(periodStoreProfitStatistic);
        return settledStoreProfit;
    }


    @Transactional
    public SettledStoreProfit addPeriodStoreProfitStatistic(String id,String periodStoreProfitStatisticId,Date settledMonth){
        SettledStoreProfit settledStoreProfit= settledStoreProfitRepository.findOne(id);
        if(settledStoreProfit==null)
            throw  new BusinessException("结算ID无效");
        PeriodStoreProfitStatistic periodStoreProfitStatistic=periodStoreProfitStatisticRepository.findOne(periodStoreProfitStatisticId);
        if(periodStoreProfitStatistic==null || settledStoreProfit.getSettledMonth().getTime()!=settledMonth.getTime())
            throw  new BusinessException("门店月流水ID无效");
        //反选
        if(periodStoreProfitStatisticRepository.exists(qPeriodStoreProfitStatistic.id.eq(periodStoreProfitStatisticId).and(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(id)))){
            settledStoreProfit.setStreamCount(settledStoreProfit.getStreamCount()-1);
            Long storeCount=periodStoreProfitStatisticRepository.count(q->q.selectDistinct(qPeriodStoreProfitStatistic.storeId).from(qPeriodStoreProfitStatistic).where(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(settledStoreProfit.getId()).and(qPeriodStoreProfitStatistic.id.ne(periodStoreProfitStatisticId))));
            settledStoreProfit.setStoreCount(storeCount.intValue());
            settledStoreProfit.setSettledAmount(periodStoreProfitStatisticRepository.findOne(q->q.select(qPeriodStoreProfitStatistic.shareAmount.multiply(100).sum().divide(100)).from(qPeriodStoreProfitStatistic).where(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(settledStoreProfit.getId()).and(qPeriodStoreProfitStatistic.id.ne(periodStoreProfitStatisticId)))));
            periodStoreProfitStatistic.setSettledStoreProfitId("");
            periodStoreProfitStatisticRepository.save(periodStoreProfitStatistic);
            return settledStoreProfitRepository.save(settledStoreProfit);
        }else{
            return createOrUpdateSettledStoreProfit(periodStoreProfitStatistic, settledStoreProfit);
        }
    }

    @Transactional
    public void deleteSettledManageInfo(String id) {
        if (!settledStoreProfitRepository.exists(qSettledStoreProfit.id.eq(id)))
            throw new BusinessException("结算信息不存在");
        if (settledStoreProfitRepository.exists(qSettledStoreProfit.settleStatus.eq(SettledStatusEnum.Settled.getValue()).and(qSettledStoreProfit.id.eq(id))))
            throw new BusinessException("已结算的记录不能删除");
        periodStoreProfitStatisticRepository.deleteBySettledStoreProfitId(id);
        settledStoreProfitRepository.delete(id);
    }

    @Transactional
    public SettledStoreProfit selectAll(UnsettledPeriodStoreProfitListRequest request){
        if(request.getAreaIds().contains(Constant.AREA_ABNORMAL_NODE_ID)){
            request.setHasAbnormalNode(Boolean.TRUE);
        }
        SettledStoreProfit settledStoreProfit= null;
        String settledStoreProfitId=request.getId();
        if(StringUtils.isEmpty(settledStoreProfitId)){
            settledStoreProfit= new SettledStoreProfit();
            request.setId(settledStoreProfit.getId());
        }else{
            settledStoreProfit=settledStoreProfitRepository.findOne(settledStoreProfitId);
            if(periodStoreProfitStatisticRepository.count(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(settledStoreProfitId))>0L){
                storeProfitStatisticMapper.cancelSettlePeriodStoreProfitStatistic(settledStoreProfitId);
            }
        }
        Long count=storeInfoMapper.getUnsettledStoreProfitCount(request);
        if(count==0L)
            throw new BusinessException("所选月份没有待结算的门店");
        Double settleAmount=storeInfoMapper.getUnsettledStoreProfitAmount(request);
        settledStoreProfit.setSettledMonth(request.getSettledMonth());
        settledStoreProfit.setStoreCount(count.intValue());
        settledStoreProfit.setSettledAmount(settleAmount);
        settledStoreProfit=settledStoreProfitRepository.save(settledStoreProfit);
        storeInfoMapper.updateUnsettledStoreProfit(request);
        return settledStoreProfit;
    }

    public SettledStoreProfit findSettledStoreProfitByID(String id){
        SettledStoreProfit settledStoreProfit=settledStoreProfitRepository.findOne(id);
        if(settledStoreProfit==null)
            throw new BusinessException("结算信息不存在");
        return settledStoreProfit;
    }

    public Date getFirstSettleDate(Date settleDate){
        return periodStoreProfitStatisticRepository.findOne(q->q.select(qPeriodStoreProfitStatistic.settledMonth).from(qPeriodStoreProfitStatistic).where(filter(settleDate)).orderBy(qPeriodStoreProfitStatistic.settledMonth.asc()));
    }

    private BooleanBuilder filter(Date settleDate){
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qPeriodStoreProfitStatistic.shareAmount.gt(0)).and(qPeriodStoreProfitStatistic.settledMonth.loe(settleDate)).and(qPeriodStoreProfitStatistic.settled.isFalse());
        return predicate;
    }


    public Date findByStatusAndSettledMonth() {
        LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(),
                1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return periodStoreProfitStatisticRepository.findOne(q -> q.select(qPeriodStoreProfitStatistic.settledMonth).from(qPeriodStoreProfitStatistic)
            .where(qPeriodStoreProfitStatistic.settled.isFalse().and(qPeriodStoreProfitStatistic.settledMonth.lt(date)).and(qPeriodStoreProfitStatistic.shareAmount.gt(0)))
            .orderBy(qPeriodStoreProfitStatistic.settledMonth.asc()).limit(1)
        );
    }

    public void settle(String settledStoreProfitId){
        SettledStoreProfit  settledStoreProfit=settledStoreProfitRepository.findOne(settledStoreProfitId);
        if(settledStoreProfit==null)
            throw new BusinessException("结算信息不存在");
        if(settledStoreProfit.getSettleStatus().equals(SettledStatusEnum.Settled.getValue()))
            throw new BusinessException("当前记录已结算");
        if(settledStoreProfit.getStoreCount()==0)
            throw new BusinessException("请选择未结算门店");
        settledStoreProfit.setSettleStatus(SettledStatusEnum.Settled.getValue());
        settledStoreProfit.setSettledTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        settledStoreProfitRepository.save(settledStoreProfit);
        storeProfitStatisticMapper.settlePeriodStoreProfitStatistic(settledStoreProfitId);
        storeProfitStatisticMapper.settleStoreProfitStatistic(settledStoreProfitId);
        Date endDate = new org.joda.time.LocalDate(settledStoreProfit.getSettledMonth()).plusMonths(1).toDate();
        AdvertisementProfitSettledInfo advertisementProfitSettledInfo = new AdvertisementProfitSettledInfo(settledStoreProfit.getSettledMonth(),endDate,settledStoreProfitId);
        asyncUpdate(advertisementProfitSettledInfo);
    }

    @Async
    public void  asyncUpdate(AdvertisementProfitSettledInfo advertisementProfitSettledInfo){
        advertisementProfitStatisticMapper.updateAdvertisementProfit(advertisementProfitSettledInfo);
        advertisementProfitStatisticMapper.updateStoreAdvertisementProfit(advertisementProfitSettledInfo);
    }

    public void confirmCreate(String id){
        SettledStoreProfit  settledStoreProfit=settledStoreProfitRepository.findOne(id);
        if(settledStoreProfit==null)
            throw new BusinessException("结算信息不存在");
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        settledStoreProfit.setCreatedTime(now);
        settledStoreProfit.setUpdatedTime(now);
        Long storeCount=periodStoreProfitStatisticRepository.count(q->q.selectDistinct(qPeriodStoreProfitStatistic.storeId).from(qPeriodStoreProfitStatistic).where(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(id).and(qPeriodStoreProfitStatistic.shareAmount.gt(0))));
        if(storeCount==0)
            throw new BusinessException("请选择未结算门店");
        settledStoreProfit.setSettleStatus(SettledStatusEnum.UnSettled.getValue());
        Double profitAmount=periodStoreProfitStatisticRepository.findOne(q->q.select(qPeriodStoreProfitStatistic.shareAmount.multiply(100).sum().divide(100)).from(qPeriodStoreProfitStatistic).where(qPeriodStoreProfitStatistic.settledStoreProfitId.eq(id)));
        settledStoreProfit.setStoreCount(storeCount.intValue());
        settledStoreProfit.setSettledAmount(profitAmount);
        settledStoreProfitRepository.save(settledStoreProfit);
    }

    public List<SettledDetailAdvertisementStatisticViewModel> getSettledDetailAdvertisementStatistic(String settledStoreProfitId) {
        return advertisementProfitStatisticMapper.getSettledDetailAdvertisementStatistic(settledStoreProfitId);
    }
}

