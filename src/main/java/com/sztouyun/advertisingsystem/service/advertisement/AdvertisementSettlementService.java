package com.sztouyun.advertisingsystem.service.advertisement;

import com.mongodb.DBObject;
import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementMapper;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementSettlement;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisementSettlement;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementStoreDailyProfit;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementSettlementRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.utils.dataHandle.DataHandleManager;
import com.sztouyun.advertisingsystem.utils.excel.EffectProfitHandleInput;
import com.sztouyun.advertisingsystem.utils.excel.EffectProfitHandleOutput;
import com.sztouyun.advertisingsystem.utils.excel.ExcelConvertConfig;
import com.sztouyun.advertisingsystem.utils.mongo.MongodbUtil;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.InvalidEffectProfitStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.ExcelImportViewModel;
import com.sztouyun.advertisingsystem.viewmodel.partner.IdResult;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.BufferedInputStream;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@CacheConfig(cacheNames = "advertisements")
public class AdvertisementSettlementService extends BaseService {
    @Autowired
    private AdvertisementSettlementRepository advertisementSettlementRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private StoreInfoRepository storeInfoRepository;

    private final QAdvertisementSettlement qAdvertisementSettlement = QAdvertisementSettlement.advertisementSettlement;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    @Autowired
    private AdvertisementMapper advertisementMapper;

    public Page<AdvertisementSettlement> advertisementSettlementList(AdvertisementSettlementListRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAdvertisementSettlement.advertisementId.eq(request.getId()));
        if (request.getSettled()!=null) {
            predicate.and(qAdvertisementSettlement.settled.eq(request.getSettled()));
        }
        return advertisementSettlementRepository.findAllAuthorized(predicate,new PageRequest(request.getPageIndex(),request.getPageSize(),new QSort(qAdvertisementSettlement.createdTime.desc())));
    }

    public void delete(String settlementId) {
        var settlement = getOperationalAdvertisementSettlement(settlementId);
        String advertisementId = settlement.getAdvertisementId();
        advertisementSettlementRepository.delete(settlementId);
        var query = new Query(Criteria.where("advertisementId").is(advertisementId));
        query.addCriteria(Criteria.where("settlementId").is(settlementId));
        mongoTemplate.updateMulti(query,Update.update("settlementId", ""), AdvertisementStoreDailyProfit.class);
    }

    /**
     * 获取可操作的广告结算记录
     * @return
     */
    private AdvertisementSettlement getOperationalAdvertisementSettlement(String settlementId) {
        AdvertisementSettlement advertisementSettlement = advertisementSettlementRepository.findOneAuthorized(qAdvertisementSettlement.id.eq(settlementId));
        if(advertisementSettlement==null)
            throw new BusinessException("结算记录不存在");
        if(advertisementSettlement.isSettled())
            throw new BusinessException("已结算的记录不能进行此操作");
        return advertisementSettlement;
    }

    @Transactional
    public void settle(String settlementId) {
        AdvertisementSettlement advertisementSettlement = getOperationalAdvertisementSettlement(settlementId);
        String advertisementId = advertisementSettlement.getAdvertisementId();
        advertisementMapper.updateSettlementInfo(settlementId,advertisementSettlement.getAdvertisementProfitMode(),getUser().getId());
        if(advertisementSettlement.getAdvertisementProfitMode().equals(AdvertisementProfitModeEnum.DeliveryEffect.getValue())){
            new Thread(()-> mongoTemplate.updateMulti(new Query(Criteria.where("oid").is(settlementId).and("bid").is(advertisementId)),Update.update("settled", true), Constant.IMPORT_DATA_COLLECTION)).start();
        }else{
            new Thread(()-> mongoTemplate.updateMulti(new Query(Criteria.where("settlementId").is(settlementId).and("advertisementId").is(advertisementId)),Update.update("settled", true), AdvertisementStoreDailyProfit.class)).start();
        }
    }

    public List<AdvertisementStoreDailyItem> advertisementStoreDailyItems(AdvertisementStoreDailyRequest request) {
        if(!advertisementRepository.exists(request.getAdvertisementId()))
            throw new BusinessException("广告ID无效");

        var storeQuery = ApiBeanUtils.copyProperties(request,AdvertisementSettlementSelectAllRequest.class);
        fillAreaQuery(storeQuery);
        if(!storeQuery.isSelectAll()){
            request.setStoreIds(storeInfoMapper.getAdvertisementStoreIds(storeQuery));
        }
        List<AggregationOperation> aggregationOperations = advertisementStoreAggregationOperation(request,true);
        aggregationOperations.add(project("date", "shareAmount", "storeId","id")
                .and("tempSettlementId").as( "settlementId")
                .and("store.storeNo").as("shopId")
                .and("store.storeName").as("storeName")
                .and("store.provinceId").as("provinceId")
                .and("store.cityId").as("cityId")
                .and("store.regionId").as("regionId")
                .and("store.deviceId").as("deviceId")
        );
        return mongoTemplate.aggregate(newAggregation(aggregationOperations), AdvertisementStoreDailyProfit.class, AdvertisementStoreDailyItem.class).getMappedResults();
    }

    private List<AggregationOperation> advertisementStoreAggregationOperation(AdvertisementStoreDailyRequest request,boolean withPage) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(match(Criteria.where("advertisementId").is(request.getAdvertisementId())));
        aggregationOperations.add(match(Criteria.where("settlementId").in(request.getSettlementId(),"")));//未结算或者选中
        aggregationOperations.add(match(Criteria.where("shareAmount").gt(0)));//分成金额大于0
        if(request.getStartTime() !=null && request.getEndTime() == null){
            aggregationOperations.add(match(Criteria.where("date").gte(request.getStartTime().getTime())));
        }
        if(request.getEndTime() !=null && request.getStartTime() == null){
            aggregationOperations.add(match(Criteria.where("date").lte(request.getEndTime().getTime())));
        }
        if(request.getEndTime() !=null && request.getStartTime() !=null) {
            aggregationOperations.add(match(Criteria.where("date").gte(request.getStartTime().getTime()).lte(request.getEndTime().getTime())));
        }
        if(request.getStoreIds() !=null){
            aggregationOperations.add(match(Criteria.where("storeId").in(request.getStoreIds())));
        }
        if(withPage){
            aggregationOperations.add(sort(Sort.Direction.DESC, "id"));
            aggregationOperations.add(skip(request.getPageIndex() * request.getPageSize().longValue()));
            aggregationOperations.add(limit(request.getPageSize()));
            aggregationOperations.add(lookup("storeInfo", "storeId", "_id", "store"));
            aggregationOperations.add(unwind("store"));
        }

        return aggregationOperations;
    }

    public Long advertisementStoreDailyItemCount(AdvertisementStoreDailyRequest request) {
        List<AggregationOperation> aggregationOperations = advertisementStoreAggregationOperation(request,false);
        aggregationOperations.add(count().as("count"));
        aggregationOperations.add(project("count"));
        return MongodbUtil.getCount(mongoTemplate,newAggregation(aggregationOperations),AdvertisementStoreDailyProfit.class,Long.class);
    }

    @Cacheable(key = "'Area_'+#p0")
    public List<Area> getAdvertisementAreaInfo(String advertisementId) {
        if(!advertisementRepository.exists(advertisementId))
            throw new BusinessException("广告ID无效");
        return storeInfoMapper.getAdvertisementAreaInfo(advertisementId);
    }

    public void addSpecialNode(List<Area> areas, String advertisementId) {
        areas.add(new Area(Constant.AREA_CONTAIN_ALL_NODE_NAME
                , Constant.TREE_ROOT_ID, null));
        if(existStoreProfitCountInAbnormal(advertisementId)) {
            areas.add(0, new Area(Constant.AREA_ABNORMAL_NODE_NAME, Constant.AREA_ABNORMAL_NODE_ID, Constant.TREE_ROOT_ID));
        }
    }

    public Boolean existStoreProfitCountInAbnormal(String advertisementId){
        Boolean exists = storeInfoMapper.existStoreInAbnormal(advertisementId);
        return exists == null ? false : exists;
    }

    public List<String> getSettlementCheckArea(String advertisementId,String settlementId) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(match(Criteria.where("advertisementId").is(advertisementId)));
        aggregationOperations.add(match(Criteria.where("settlementId").is(settlementId)));
        aggregationOperations.add(group("storeId"));
        List<IdResult> idResults = mongoTemplate.aggregate(newAggregation(aggregationOperations), AdvertisementStoreDailyProfit.class, IdResult.class).getMappedResults();
        var storeIds= Linq4j.asEnumerable(idResults).select(e -> e.getId()).toList();
        return storeInfoRepository.findAll(q->q.select(qStoreInfo.regionId).from(qStoreInfo).where(qStoreInfo.id.in(storeIds)).groupBy(qStoreInfo.regionId));
    }

    public AdvertisementSettlementInfo getAdvertisementSettlementInfo(String advertisementId,String settlementId) {
        List<AggregationOperation> aggregationCountOperations = new ArrayList<>();
        aggregationCountOperations.add(match(Criteria.where("advertisementId").is(advertisementId)));
        aggregationCountOperations.add(match(Criteria.where("tempSettlementId").is(settlementId)));
        aggregationCountOperations.add(group("storeId"));
        aggregationCountOperations.add(group().count().as("count"));

        Integer storeCount = MongodbUtil.getCount(mongoTemplate, newAggregation(aggregationCountOperations), AdvertisementStoreDailyProfit.class, Long.class).intValue();

        List<AggregationOperation> aggregationSumOperations = new ArrayList<>();
        aggregationCountOperations.add(match(Criteria.where("advertisementId").is(advertisementId)));
        aggregationSumOperations.add(match(Criteria.where("tempSettlementId").is(settlementId)));
        aggregationSumOperations.add(group().sum("shareAmount").as("shareAmount"));
        aggregationSumOperations.add(project("shareAmount"));

        AdvertisementSettlementInfo settlementInfo = mongoTemplate.aggregate(newAggregation(aggregationSumOperations), AdvertisementStoreDailyProfit.class, AdvertisementSettlementInfo.class).getUniqueMappedResult();
        settlementInfo = settlementInfo == null ? new AdvertisementSettlementInfo() : settlementInfo;
        settlementInfo.setStoreCount(storeCount);
        return settlementInfo;
    }

    public ExcelImportViewModel importStoreProfitFromExcel(MultipartFile file, String advertisementId){
        String settlementId= UUIDUtils.generateBase58UUID();
        ExcelImportViewModel excelImportViewModel=new ExcelImportViewModel(settlementId);
        ExcelConvertConfig excelConvertConfig =new ExcelConvertConfig(
                settlementId,
                advertisementId,
                null,
                new String[]{"门店ID","门店名称","单价","数量","分成金额"},
                new String[]{"storeNo","storeName","unitPrice","count","shareAmount"}
        );
        excelConvertConfig.addCurrencyKeys(Arrays.asList("unitPrice","shareAmount"));
        excelConvertConfig.configDefaultValue(new HashMap(){{
            put("storeNo","");put("storeName","");put("unitPrice",0);put("count",0);put("shareAmount",0);
        }});
        DataHandleManager dataHandleManager=new DataHandleManager();
        EffectProfitHandleOutput output=null;
        try {
            output= (EffectProfitHandleOutput) dataHandleManager.handle(new EffectProfitHandleInput(new BufferedInputStream(file.getInputStream()),excelConvertConfig));
        } catch (Exception e){
            throw new BusinessException(e.getMessage());
        }
        Integer rows=output.getRows();
        if(rows==0)
            throw  new BusinessException("至少导入一家门店");
        excelImportViewModel.setTotal(rows);
        Integer validCount=getStoreProfitValidStoreCount(settlementId,advertisementId).intValue();
        excelImportViewModel.setUnmatched(rows-validCount);
        createAdvertisementSettlement(settlementId,advertisementId,validCount);
        return excelImportViewModel;
    }

    private Long getStoreProfitValidStoreCount(String settlementId,String advertisementId){
        return mongoTemplate.count(new Query(Criteria.where("oid").is(settlementId).and("bid").is(advertisementId).and("validType").is(0)), Constant.IMPORT_DATA_COLLECTION);
    }

    private Long getStoreProfitValidShareAmount(String settlementId,String advertisementId){
        List<AggregationOperation> operationList=new ArrayList<>();
        operationList.add( match(Criteria.where("oid").is(settlementId).and("bid").is(advertisementId).and("validType").is(0)));
        operationList.add(group("oid","bio").sum("shareAmount").as("count"));
        operationList.add(project("count"));
        Aggregation aggregation=newAggregation(operationList);
        return MongodbUtil.getCount(mongoTemplate,aggregation,Constant.IMPORT_DATA_COLLECTION,Long.class);
    }

    private void createAdvertisementSettlement(String settlementId,String advertisementId,Integer storeCount){
        AdvertisementSettlement advertisementSettlement=new AdvertisementSettlement();
        advertisementSettlement.setAdvertisementId(advertisementId);
        advertisementSettlement.setId(settlementId);
        advertisementSettlement.setStoreCount(storeCount);
        advertisementSettlement.setAdvertisementProfitMode(AdvertisementProfitModeEnum.DeliveryEffect.getValue());
        advertisementSettlement.setShareAmount(getStoreProfitValidShareAmount(settlementId,advertisementId));
        advertisementSettlementRepository.save(advertisementSettlement);
    }


   public List<InvalidEffectProfitStoreInfo> getInvalidEffectProfitStoreInfo(InvalidEffectProfitStoreInfoRequest request){
        List<AggregationOperation> operationList=new ArrayList<>();
        operationList.add( match(Criteria.where("oid").is(request.getId()).and("validType").gt(0)));
        operationList.add(skip(request.getPageIndex()*request.getPageSize()));
        operationList.add(limit(request.getPageSize()));
        operationList.add(project("storeName","unitPrice","count","shareAmount","validType").and("storeNo").as("shopId"));
        Aggregation aggregation=newAggregation(operationList);
        return mongoTemplate.aggregate(aggregation,Constant.IMPORT_DATA_COLLECTION,InvalidEffectProfitStoreInfo.class).getMappedResults();
    }

    public Integer getEffectProfitStoreCount(String id,Boolean valid){
        Query query=new Query(Criteria.where("oid").is(id));
        if(valid){
            query.addCriteria(Criteria.where("validType").is(0));
        }else{
            query.addCriteria(Criteria.where("validType").gt(0));
        }
        Long count= mongoTemplate.count(query,Constant.IMPORT_DATA_COLLECTION);
        return count.intValue();
    }

    public void toggleSelectAdvertisementStoreDailyProfit(String advertisementStoreDailyProfitId, String settlementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(advertisementStoreDailyProfitId));
        query.fields().include("tempSettlementId");
        AdvertisementStoreDailyProfit advertisementStoreDailyProfit = mongoTemplate.findOne(query, AdvertisementStoreDailyProfit.class);
        if (advertisementStoreDailyProfit == null)
            throw new BusinessException("记录不存在");

        Update update = new Update();
        if (!settlementId.equals(advertisementStoreDailyProfit.getTempSettlementId())) {
            update.set("tempSettlementId", settlementId);
        } else {
            update.set("tempSettlementId", "");
        }
        mongoTemplate.updateFirst(query,update,AdvertisementStoreDailyProfit.class);
    }

    public AdvertisementSettlement findAdvertisementSettlementById(String id){
        AdvertisementSettlement advertisementSettlement=advertisementSettlementRepository.findOne(id);
        if(advertisementSettlement==null)
            throw new BusinessException("ID无效");
        return advertisementSettlement;
    }

    public  List<DBObject> getAdvertisementSettlementDetailList(ObjectId lastId, String id, Long pageSize, AdvertisementProfitModeEnum profitMode){
        List<AggregationOperation> operations=new ArrayList<>();
        if(lastId!=null){
            operations.add(match(Criteria.where("_id").gt(lastId)));
        }
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect)){
            operations.add(match(Criteria.where("oid").is(id).and("validType").is(0)));
            operations.add(project("_id","storeNo","storeName","unitPrice","count","shareAmount"));
            operations.add(sort(Sort.Direction.ASC,"_id"));
            operations.add(limit(pageSize));
        }else{
            operations.add(match(Criteria.where("settlementId").is(id)));
            operations.add(sort(Sort.Direction.ASC,"id"));
            operations.add(limit(pageSize));
            operations.add(lookup("storeInfo", "storeId", "_id", "store"));
            operations.add(unwind("store"));
            operations.add(project("id","shareAmount","date")
                    .and("store.storeNo").as("storeNo")
                    .and("store.storeName").as("storeName")
                    .and("store.provinceId").as("provinceId")
                    .and("store.cityId").as("cityId")
                    .and("store.regionId").as("regionId")
                    .and("store.deviceId").as("deviceId"));
        }
        Aggregation aggregation=newAggregation(operations);
        return profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect)?
                 mongoTemplate.aggregate(aggregation,Constant.IMPORT_DATA_COLLECTION,DBObject.class).getMappedResults()
                        :mongoTemplate.aggregate(aggregation,AdvertisementStoreDailyProfit.class,DBObject.class).getMappedResults();

    }

    public void selectAllAdvertisementStoreDailyProfit(AdvertisementSettlementSelectAllRequest request) {
        if(!advertisementRepository.exists(request.getAdvertisementId()))
            throw new BusinessException("广告ID无效");

        Query query = new Query();
        fillAreaQuery(request);
        if(!request.isSelectAll()){
            query.addCriteria(Criteria.where("storeId").in(storeInfoMapper.getAdvertisementStoreIds(request)));
        }
        query =getSelectAllQuery(request, query);
        Update update = new Update();
        update.set("tempSettlementId", request.getSettlementId());
        mongoTemplate.updateMulti(query, update, AdvertisementStoreDailyProfit.class);
    }

    private Query getSelectAllQuery(AdvertisementSettlementSelectAllRequest request, Query query) {
        query.addCriteria(Criteria.where("advertisementId").is(request.getAdvertisementId()));
        query.addCriteria(Criteria.where("settlementId").in(request.getSettlementId(),""));
        query.addCriteria(Criteria.where("shareAmount").gt(0));
        if(request.getStartTime() !=null && request.getEndTime() == null){
            query.addCriteria(Criteria.where("date").gte(request.getStartTime().getTime()));
        }
        if(request.getEndTime() !=null && request.getStartTime() == null){
            query.addCriteria(Criteria.where("date").lte(request.getEndTime().getTime()));
        }
        if(request.getEndTime() !=null && request.getStartTime() !=null) {
            query.addCriteria(Criteria.where("date").gte(request.getStartTime().getTime()).lte(request.getEndTime().getTime()));
        }
        return query;
    }

    private void fillAreaQuery(AdvertisementSettlementSelectAllRequest request) {
        if (!StringUtils.isEmpty(request.getAreaIds())) {
            List<String> regionIds = Arrays.asList(request.getAreaIds().split(","));
            if(regionIds.contains(Constant.TREE_ROOT_ID))
            {
                request.setSelectAll(true);
                return;
            }
            if (regionIds.contains(Constant.AREA_ABNORMAL_NODE_ID)) {
                request.setHasAbnormalNode(true);
            }
            request.setRegionIds(regionIds);
        }
    }

    public void empty(String advertisementId,String settlementId) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("advertisementId").is(advertisementId));
        query.addCriteria(Criteria.where("tempSettlementId").is(settlementId));
        update.set("tempSettlementId", "");
        mongoTemplate.updateMulti(query, update, AdvertisementStoreDailyProfit.class);
    }

    public  List<DBObject> getEffectProfitInvalidList(ObjectId lastId, String id, Long pageSize){
        List<AggregationOperation> operations=new ArrayList<>();
        if(lastId!=null){
            operations.add(match(Criteria.where("_id").gt(lastId)));
        }
        operations.add(match(Criteria.where("oid").is(id).and("validType").gt(0)));
        operations.add(project("_id","storeNo","storeName","unitPrice","count","shareAmount","validType"));
        operations.add(sort(Sort.Direction.ASC,"_id"));
        operations.add(limit(pageSize));
        Aggregation aggregation=newAggregation(operations);
        return mongoTemplate.aggregate(aggregation,Constant.IMPORT_DATA_COLLECTION,DBObject.class).getMappedResults();
    }

    public void saveSettlement(String advertisementId,String settlementId) {
        Integer mode = advertisementRepository.findOneAuthorized(q -> q.select(qAdvertisement.mode).from(qAdvertisement).where(qAdvertisement.id.eq(advertisementId)));
        if (mode == null || mode.equals(AdvertisementProfitModeEnum.FixedAmount.getValue())  || mode.equals(AdvertisementProfitModeEnum.DeliveryEffect.getValue()))
            throw new BusinessException("该广告模式不支持结算操作");
        AdvertisementSettlementInfo advertisementSettlementInfo = getAdvertisementSettlementInfo(advertisementId,settlementId);
        if(advertisementSettlementInfo.getStoreCount() == null || advertisementSettlementInfo.getStoreCount().equals(0))
            throw new BusinessException("请选择结算门店");
        AdvertisementSettlement advertisementSettlement = new AdvertisementSettlement();
        advertisementSettlement.setAdvertisementId(advertisementId);
        advertisementSettlement.setSettledUser(getUser().getNickname());
        advertisementSettlement.setId(settlementId);
        advertisementSettlement.setAdvertisementProfitMode(mode);
        advertisementSettlement.setStoreCount(advertisementSettlementInfo.getStoreCount());
        advertisementSettlement.setShareAmount(advertisementSettlementInfo.getShareAmount());
        advertisementSettlementRepository.save(advertisementSettlement);
        updateAdvertisementStoreDailyProfit(advertisementId,settlementId);
    }

    private void updateAdvertisementStoreDailyProfit(String advertisementId,String settlementId) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,AdvertisementStoreDailyProfit.class);
        //更新新选中的记录结算ID为当前结算ID
        Query query = new Query();
        query.addCriteria(Criteria.where("advertisementId").is(advertisementId));
        query.addCriteria(Criteria.where("tempSettlementId").is(settlementId));
        query.addCriteria(Criteria.where("settlementId").is(""));
        Update update = new Update();
        update.set("settlementId", settlementId);
        bulkOperations.updateMulti(query,update);

        //更新取消选中的记录结算ID为空
        var unSelectedQuery =new Query(Criteria.where("tempSettlementId").is(""));
        unSelectedQuery.addCriteria(Criteria.where("advertisementId").is(advertisementId));
        unSelectedQuery.addCriteria(Criteria.where("settlementId").is(settlementId));
        Update unSelectedUpdate = new Update();
        unSelectedUpdate.set("settlementId", "");
        bulkOperations.updateMulti(unSelectedQuery,unSelectedUpdate);

        bulkOperations.execute();
    }

    /**
     * 取消结算
     */
    public void cancelSettlement(String advertisementId,String settlementId){
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,AdvertisementStoreDailyProfit.class);
        //将所有取消选中的还原
        Query query = new Query();
        query.addCriteria(Criteria.where("advertisementId").is(advertisementId));
        query.addCriteria(Criteria.where("settlementId").is(settlementId));
        query.addCriteria(Criteria.where("tempSettlementId").is(""));
        Update update = new Update();
        update.set("tempSettlementId", settlementId);
        bulkOperations.updateMulti(query, update);

        //将所有新选中的清空
        Query newAddQuery = new Query();
        newAddQuery.addCriteria(Criteria.where("advertisementId").is(advertisementId));
        newAddQuery.addCriteria(Criteria.where("settlementId").is(""));
        newAddQuery.addCriteria(Criteria.where("tempSettlementId").is(settlementId));
        Update newAddUpdate = new Update();
        newAddUpdate.set("tempSettlementId", "");
        bulkOperations.updateMulti(newAddQuery, newAddUpdate);

        bulkOperations.execute();
    }

    public Map<String,AdvertisementSettlementStatistic> getEffectProfitStoreSettleInfoMap(String advertisementId, List<String> storeIds ){
        Map<String,AdvertisementSettlementStatistic> resultMap=new HashMap<>();
        List<AggregationOperation> operations=new ArrayList<>();
        if(CollectionUtils.isEmpty(storeIds) || StringUtils.isEmpty(advertisementId))
            return resultMap;
        operations.add(match(Criteria.where("bid").is(advertisementId)));
        if(!CollectionUtils.isEmpty(storeIds)){
            operations.add(match(Criteria.where("storeId").exists(true).in(storeIds)));
        }
        operations.add(match(Criteria.where("settled").is(true)));
        operations.add(group("storeId").sum("shareAmount").as("totalShareAmount"));
        operations.add(project("totalShareAmount").and("storeId").previousOperation());
        Aggregation aggregation=newAggregation(operations);
        Iterator<AdvertisementSettlementStatistic> statisticIterator=mongoTemplate.aggregate(aggregation,Constant.IMPORT_DATA_COLLECTION,AdvertisementSettlementStatistic.class).getMappedResults().iterator();
        while (statisticIterator.hasNext()){
            AdvertisementSettlementStatistic item=statisticIterator.next();
            resultMap.put(item.getStoreId(),item);
        }
        return resultMap;
    }

}
