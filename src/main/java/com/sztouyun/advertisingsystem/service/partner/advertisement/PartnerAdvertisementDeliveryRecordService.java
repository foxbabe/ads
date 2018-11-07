package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.monitor.PartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.store.PartnerAdvertisementDeliveryRecordStatusEnum;
import com.sztouyun.advertisingsystem.repository.partner.advertisement.PartnerAdvertisementRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.partner.advertismentSource.AdvertisementSourceEnum;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.mongo.MongodbUtil;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerAdvertisementStoreListRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerAdvertisementStoreListViewModel;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerAdvertisementDeliveryRecordLog;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class PartnerAdvertisementDeliveryRecordService extends BaseService {
    @Autowired
    private PartnerAdvertisementRepository partnerAdvertisementRepository;
    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final QPartnerAdvertisement qPartnerAdvertisement = QPartnerAdvertisement.partnerAdvertisement;
    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;
    @Autowired
    private EnvironmentConfig environmentConfig;

    public List<PartnerAdvertisementStoreListViewModel> queryPartnerAdvertisementDeliveryRecordList(PartnerAdvertisementStoreListRequest request) {
        List<AggregationOperation> list = getPartnerAdvertisementDeliveryAggregationOperations(request);
        list.add(sort(Sort.Direction.DESC,"requestTime"));
        list.add(skip(request.getPageIndex()*request.getPageSize()));
        list.add(limit(request.getPageSize()));
        list.add(project("thirdPartId","duration","valid","requestTime","finishTime","takeOffTime","advertisementPositionCategory", "startTime").
                and("id").as("_id").
                and("storeId").as("storeId").
                and("store.storeNo").as("shopId").
                and("store.storeName").as("storeName").
                and("store.provinceId").as("provinceId").
                and("store.cityId").as("cityId").
                and("store.regionId").as("regionId").
                and("store.storeAddress").as("storeAddress").
                and("store.deviceId").as("deviceId").
                and("status").as("advertisementStoreStatus")
        );
        Aggregation agg = newAggregation(list);
        return mongoTemplate.aggregate(agg, PartnerAdvertisementDeliveryRecord.class, PartnerAdvertisementStoreListViewModel.class).getMappedResults();
    }

    private List<AggregationOperation> getPartnerAdvertisementDeliveryAggregationOperations(PartnerAdvertisementStoreListRequest request) {
        List<AggregationOperation> list=new ArrayList<>();
        list.add(match(Criteria.where("partnerAdvertisementId").is(request.getPartnerAdvertisementId())));
        if(request.getAdvertisementPositionType()!=null){
            list.add(match(Criteria.where("advertisementPositionCategory").is(request.getAdvertisementPositionType())));
        }
        if(request.getAdvertisementStoreStatus()!=null){
            list.add(match(Criteria.where("status").is(request.getAdvertisementStoreStatus())));
        }
        if(request.getValid()!=null){
            list.add(match(Criteria.where("valid").is(request.getValid())));
        }
        list.add(lookup("storeInfo", "storeId", "_id", "store"));
        list.add(unwind("store"));
        if(!StringUtils.isEmpty(request.getProvinceId())){
            list.add(match(Criteria.where("store.provinceId").exists(Boolean.TRUE)));
            list.add(match(Criteria.where("store.provinceId").is(request.getProvinceId())));
        }
        if(!StringUtils.isEmpty(request.getCityId())){
            list.add(match(Criteria.where("store.cityId").exists(Boolean.TRUE)));
            list.add(match(Criteria.where("store.cityId").is(request.getCityId())));
        }
        if(!StringUtils.isEmpty(request.getRegionId())){
            list.add(match(Criteria.where("store.regionId").exists(Boolean.TRUE)));
            list.add(match(Criteria.where("store.regionId").is(request.getRegionId())));
        }
        if(!StringUtils.isEmpty(request.getStoreName())){
            list.add(match(Criteria.where("store.storeName").regex(request.getStoreName())));
        }
        if(!StringUtils.isEmpty(request.getDeviceId())){
            list.add(match(Criteria.where("store.deviceId").regex(request.getDeviceId())));
        }
        if(!StringUtils.isEmpty(request.getShopId())){
            list.add(match(Criteria.where("store.storeNo").regex(request.getShopId())));
        }
        return list;
    }

    public Long queryPartnerAdvertisementDeliveryRecordListTotal(PartnerAdvertisementStoreListRequest request) {
        var aggregationOperations=getPartnerAdvertisementDeliveryAggregationOperations(request);
        aggregationOperations.add(count().as("count"));
        aggregationOperations.add(project("count"));
        return MongodbUtil.getCount(mongoTemplate,newAggregation(aggregationOperations),PartnerAdvertisementDeliveryRecord.class,Long.class);
    }

    public void savePartnerAdvertisementStartDeliveryRecordLog(List<PartnerAdvertisementDeliveryRecordLog> recordLogList) {
        var recordIds = Linq4j.asEnumerable(recordLogList).select(r->r.getRecordId()).toList();
        Query query= new Query(Criteria.where("_id").in(recordIds));
        query.addCriteria(Criteria.where("startTime").exists(false));
        query.fields().exclude("monitorUrls").exclude("startDisplayMonitorUrls").exclude("extend");
        List<PartnerAdvertisementDeliveryRecord> unStartedDeliveryRecords = mongoTemplate.find(query,PartnerAdvertisementDeliveryRecord.class);
        Map<String,PartnerAdvertisementDeliveryRecord> unStartedDeliveryRecordMap =Linq4j.asEnumerable(unStartedDeliveryRecords).toMap(d->d.getId(), d->d);

        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,PartnerAdvertisementDeliveryRecord.class);
        for (var recordLog:recordLogList){
            if(!unStartedDeliveryRecordMap.containsKey(recordLog.getRecordId()))
                continue;
            Update update = new Update();
            update.set("startTime",new Date().getTime());
            bulkOperations.updateOne(new Query(Criteria.where("_id").in(recordLog.getRecordId())), update);
            unStartedDeliveryRecordMap.remove(recordLog.getRecordId());
        }
        try{
            bulkOperations.execute();
        }catch (Exception ex){
            logger.info(ex.getMessage(),ex);
        }
    }

    public void savePartnerAdvertisementEndDeliveryRecordLog(List<PartnerAdvertisementDeliveryRecordLog> recordLogList) {
        var recordIds = Linq4j.asEnumerable(recordLogList).select(r->r.getRecordId()).toList();
        //查找所有未完的记录
        Query query= new Query(Criteria.where("_id").in(recordIds));
        query.addCriteria(Criteria.where("finishTime").exists(false));
        query.fields().exclude("monitorUrls").exclude("startDisplayMonitorUrls").exclude("extend");
        List<PartnerAdvertisementDeliveryRecord> unFinishedDeliveryRecords = mongoTemplate.find(query,PartnerAdvertisementDeliveryRecord.class);
        if(CollectionUtils.isEmpty(unFinishedDeliveryRecords))
            return;

        Map<String,PartnerAdvertisementDeliveryRecord> unFinishedDeliveryRecordMap =Linq4j.asEnumerable(unFinishedDeliveryRecords).toMap(d->d.getId(), d->d);
        List<PartnerDailyStoreMonitorStatistic> partnerDailyStoreMonitorStatistics = new ArrayList<>();
        Collection<String> allPartnerAdvertisementIds = new HashSet<>();
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,PartnerAdvertisementDeliveryRecord.class);
        for (var recordLog:recordLogList){
            if(!unFinishedDeliveryRecordMap.containsKey(recordLog.getRecordId()))
                continue;
            var deliveryRecord =unFinishedDeliveryRecordMap.get(recordLog.getRecordId());
            if(deliveryRecord ==null)
                continue;
            allPartnerAdvertisementIds.add(deliveryRecord.getPartnerAdvertisementId());
            var partnerDailyStoreMonitorStatistic = new PartnerDailyStoreMonitorStatistic(deliveryRecord.getPartnerId(),new Date(deliveryRecord.getRequestDate()),deliveryRecord.getStoreId(),deliveryRecord.getAdvertisementPositionCategory());
            partnerDailyStoreMonitorStatistic.setDisplayTimes(1);
            var isDelivering = PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue().equals(deliveryRecord.getStatus());
            //考虑队列可能会出现延迟，对于线上自动下架的如果是有效的，则回滚状态为已完成
            var isAutoTakeOff = environmentConfig.isOnline() && PartnerAdvertisementDeliveryRecordStatusEnum.TakeOff.getValue().equals(deliveryRecord.getStatus()) && StringUtils.isEmpty(deliveryRecord.getOperator());
            Update update = new Update();
            boolean valid =(isDelivering || isAutoTakeOff) && recordLog.getCreatedTime()<=deliveryRecord.getEffectiveFinishTime();
            //更新三方广告投放记录
            if(valid) {
                update.set("valid", valid);
                partnerDailyStoreMonitorStatistic.setValidTimes(1);
            }
            update.set("finishTime",recordLog.getCreatedTime());
            if(isDelivering || (isAutoTakeOff && valid)){
                var status = valid ? PartnerAdvertisementDeliveryRecordStatusEnum.Finished.getValue():PartnerAdvertisementDeliveryRecordStatusEnum.TakeOff.getValue();
                update.set("status",status);
                if(status.equals(PartnerAdvertisementDeliveryRecordStatusEnum.TakeOff.getValue())) {
                    update.set("takeOffTime", new Date().getTime());
                    update.set("remark", "广告上报监播地址时间超过RTB模式时长，广告播放无效，系统自动下架广告");
                }
            }
            bulkOperations.updateOne(new Query(Criteria.where("_id").in(recordLog.getRecordId())), update);
            partnerDailyStoreMonitorStatistics.add(partnerDailyStoreMonitorStatistic);
            unFinishedDeliveryRecordMap.remove(recordLog.getRecordId());
        }
        //批量提交保存三方广告投放记录
        try{
            bulkOperations.execute();
        }catch (Exception ex){
            logger.info(ex.getMessage(),ex);
        }

        List<String> oldDeliveringAdvertisementIds = partnerAdvertisementRepository.findAll(q->q.select(qPartnerAdvertisement.id).from(qPartnerAdvertisement)
                .where(qPartnerAdvertisement.advertisementStatus.eq(PartnerAdvertisementStatusEnum.Delivering.getValue()).and(qPartnerAdvertisement.id.in(allPartnerAdvertisementIds))));
        List<String> deliveringAdvertisementIds =partnerAdvertisementService.getDeliveringAdvertisementIds(oldDeliveringAdvertisementIds);
        partnerAdvertisementService.finishPartnerAdvertisement(Linq4j.asEnumerable(oldDeliveringAdvertisementIds).except(Linq4j.asEnumerable(deliveringAdvertisementIds)).toList());
        if(!partnerDailyStoreMonitorStatistics.isEmpty()){
            partnerAdvertisementMapper.updatePartnerAdvertisementDisplayStatistic(partnerDailyStoreMonitorStatistics);
        }
    }

    public void savePartnerAdvertisementDeliveryRecord(List<PartnerAdvertisementDeliveryRecord> recordList) {
        Map<String,Integer> partnerAdvertisementRequestTimesMap =Linq4j.asEnumerable(recordList).groupBy(r->r.getPartnerAdvertisementId()).toMap(r->r.getKey(),r->r.count());
        //插入三方广告投放中的操作日志
        var partnerAdvertisementIds =Linq4j.asEnumerable(partnerAdvertisementRequestTimesMap.keySet()).toList();
        List<String> deliveringAdvertisementIds = partnerAdvertisementRepository.findAll(q->q.select(qPartnerAdvertisement.id).from(qPartnerAdvertisement)
                .where(qPartnerAdvertisement.advertisementStatus.eq(PartnerAdvertisementStatusEnum.Delivering.getValue()).and(qPartnerAdvertisement.id.in(partnerAdvertisementIds))));
        List<PartnerAdvertisementOperationLog> partnerAdvertisementOperationLogList = Linq4j.asEnumerable(partnerAdvertisementIds).except(Linq4j.asEnumerable(deliveringAdvertisementIds))
                .select(id->new PartnerAdvertisementOperationLog(id,PartnerAdvertisementStatusEnum.Delivering.getValue())).toList();
        mongoTemplate.insertAll(partnerAdvertisementOperationLogList);

        // 保存三方广告素材
        List<PartnerAdvertisementMaterial> partnerAdvertisementMaterials =Linq4j.asEnumerable(recordList).select(r-> ApiBeanUtils.copyProperties(r,PartnerAdvertisementMaterial.class)).toList();
        partnerAdvertisementMapper.savePartnerAdvertisementMaterials(partnerAdvertisementMaterials);

        //保存三方广告
        List<PartnerAdvertisement> partnerAdvertisements = Linq4j.asEnumerable(partnerAdvertisementIds).select(id->{
            var partnerAdvertisement =new PartnerAdvertisement();
            var index = id.indexOf("X");
            if(index <0)
                return null;
            AdvertisementSourceEnum advertisementSource = EnumUtils.toEnum(Integer.parseInt(id.substring(0,index)),AdvertisementSourceEnum.class);
            partnerAdvertisement.setPartnerId(advertisementSource.getPartnerId());
            partnerAdvertisement.setThirdPartId(id.substring(index+1));
            partnerAdvertisement.setCreatedTime(new Date());
            partnerAdvertisement.setUpdatedTime(new Date());
            partnerAdvertisement.setId(id);
            return partnerAdvertisement;
        }).toList();
        partnerAdvertisementMapper.savePartnerAdvertisements(partnerAdvertisements);
        partnerAdvertisementMapper.updatePartnerAdvertisementMaterialType(partnerAdvertisementIds);
    }

    public void takeOff(Query query,String remark,String operator){
        query.addCriteria(Criteria.where("status").is(PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue()));
        Update update = new Update();
        update.set("status", PartnerAdvertisementDeliveryRecordStatusEnum.TakeOff.getValue());
        update.set("takeOffTime", new Date().getTime());
        if(!StringUtils.isEmpty(operator)){
            update.set("operator", operator);
        }
        update.set("remark", remark);
        mongoTemplate.updateMulti(query,update, PartnerAdvertisementDeliveryRecord.class);
    }

    public void manualTakeOff(String recordId,String remark){
        Query query= new Query(Criteria.where("_id").in(recordId));
        PartnerAdvertisementDeliveryRecord record = mongoTemplate.findOne(query,PartnerAdvertisementDeliveryRecord.class);
        if(record == null)
            throw new BusinessException("记录不存在!");
        if(!record.getStatus().equals(PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue()))
            throw new BusinessException("当前状态不支持本操作!");

        query= new Query(Criteria.where("_id").in(recordId));
        takeOff(query,remark,getUser().getId());
        var allAdvertisementIds =Arrays.asList(record.getPartnerAdvertisementId());
        var newDeliveringAdvertisementIds =partnerAdvertisementService.getDeliveringAdvertisementIds(allAdvertisementIds);
        if(CollectionUtils.isEmpty(newDeliveringAdvertisementIds)){
            //更新三方广告状态为已完成
            partnerAdvertisementService.finishPartnerAdvertisement(allAdvertisementIds);
        }
    }
}
