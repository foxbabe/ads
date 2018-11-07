package com.sztouyun.advertisingsystem.service.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.common.map.IMapService;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.mapper.StoreNearByMapper;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandard;
import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandardDto;
import com.sztouyun.advertisingsystem.model.job.QStoreSyncLog;
import com.sztouyun.advertisingsystem.model.job.StoreSyncLog;
import com.sztouyun.advertisingsystem.model.mongodb.StoreDeviceDailyStatisticResult;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.store.*;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.repository.contract.StoreCategoryStandardRepository;
import com.sztouyun.advertisingsystem.repository.job.StoreSyncLogRepository;
import com.sztouyun.advertisingsystem.repository.partner.CooperationPartnerRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoExtensionRepository;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.repository.store.StorePortraitRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.DeleteStoreInfoNotificationData;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.UpdateStoreInfoNotificationData;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.thirdpart.impl.OmsNewThirdPartImpl;
import com.sztouyun.advertisingsystem.utils.*;
import com.sztouyun.advertisingsystem.viewmodel.DayPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.job.OmsResult;
import com.sztouyun.advertisingsystem.viewmodel.job.OmsStoreInfoPageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.job.OmsStoreInfoResult;
import com.sztouyun.advertisingsystem.viewmodel.job.OmsStoreInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreNearByUpdateInfo;
import com.sztouyun.advertisingsystem.viewmodel.store.openapi.PartnerStoreInfo;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "stores")
public class StoreInfoServiceJob extends BaseService{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${oms.store.info.url}")
    private String getUrl;
    @Value("${oms.store.info.url.new}")
    private String omsNewUrl;
    @Value("${oms.store.info.header.key}")
    private String headerKey;
    @Value("${oms.store.info.header.value}")
    private String headerValue;

    private static Integer pushRecordSize=1000;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private StoreSyncLogRepository syncStoreLogRepository;
    @Autowired
    private StoreCategoryStandardRepository storeCategoryStandardRepository;
    @Autowired
    private AreaService areaService;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private CooperationPartnerRepository cooperationPartnerRepository;
    @Autowired
    private StoreInfoExtensionRepository storeInfoExtensionRepository;
    @Autowired
    private StorePortraitRepository storePortraitRepository;
    @Autowired
    private OmsNewThirdPartImpl omsNewThirdPart;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IMapService mapService;
    @Autowired
    private StoreNearByMapper storeNearByMapper;
    private QStoreSyncLog qStoreSyncLog = QStoreSyncLog.storeSyncLog;
    private QStoreInfo qStoreInfo=QStoreInfo.storeInfo;
    private QStorePortrait qStorePortrait=QStorePortrait.storePortrait;

    @CacheEvict(allEntries = true)
    public void synchronizeStoreInfo(Date startDate, Date endDate) {
        saveRemoteStoreInfoList(startDate, endDate, StoreSourceEnum.NEW_OMS.getValue());
		new Thread(()->{calcStoreOpenCloseTime(startDate,endDate);}).start();
        syncStoreNearByInfo();
    }

    @Transactional
    public void calcStoreType() {
        List<StoreCategoryStandard> storeCategoryStandardList=storeCategoryStandardRepository.findAll();
        List<StoreCategoryStandardDto> list=new ArrayList<>();
        if(storeCategoryStandardList!=null &&!storeCategoryStandardList.isEmpty()){
            storeCategoryStandardList.forEach(item->{
                StoreCategoryStandardDto  storeCategoryStandardDto=ApiBeanUtils.copyProperties(item,StoreCategoryStandardDto.class);
                Integer leftSymbol=item.getLeftSymbol();
                Integer rightSymbol=item.getRightSymbol();
                if(null!=leftSymbol){
                    storeCategoryStandardDto.setLeftComparisonTypeValue(EnumUtils.getDisplayName(leftSymbol,ComparisonTypeEnum.class));
                }
                if(null!=rightSymbol){
                    storeCategoryStandardDto.setRightComparisonTypeValue(EnumUtils.getDisplayName(rightSymbol,ComparisonTypeEnum.class));
                }
                list.add(storeCategoryStandardDto);
            });
            storeInfoMapper.calculateStoreTypeByConfig(list);
        }
    }

    public StoreSyncLog findNewestLogBySuccess() {
        return syncStoreLogRepository.findFirstBySuccessedOrderByCreatedTimeDesc(true);
    }

    public StoreSyncLog findNewestLogBySuccess(String jobName) {
        return syncStoreLogRepository.findFirstBySuccessedAndJobNameOrderByCreatedTimeDesc(true,jobName);
    }

    private String getAreaIdByCode(String code,String parentId) {
        if (StringUtils.isEmpty(code))
            return "";
        Area area =areaService.getAreaByCode(code);
        if (area == null || !area.getParentId().equals(parentId))
            return "";
        return area.getId();
    }

    private void saveRemoteStoreInfoList(Date startDate, Date endDate, Integer storeSource){
        int pageNum = 1;
        boolean hasGetAll =false;
        while (!hasGetAll){
            int finalPageNum = pageNum;
            StoreSyncLog syncStoreLog = syncStoreLogRepository.findOne(q->q.selectFrom(qStoreSyncLog)
                    .where(qStoreSyncLog.startDate.eq(startDate).and(qStoreSyncLog.endDate.eq(endDate)).and(qStoreSyncLog.pageNum.eq(finalPageNum)).and(qStoreSyncLog.storeSource.eq(storeSource))));

            if(syncStoreLog !=null && syncStoreLog.isSuccessed()){
                pageNum++;
                continue;
            }

            String url = getUrlByStoreSource(storeSource);
            OmsResult<OmsStoreInfoPageViewModel> omsResult =getHttpEntity(startDate,endDate,pageNum,200, url);
            if(omsResult == null){
                int i =0 ;
                while ( i< 3 && omsResult == null){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    omsResult = getHttpEntity(startDate,endDate,pageNum,200, url);
                    i++;
                }
            }
            if(omsResult == null || !omsResult.isSuccessful()){
                pageNum++;
                continue;
            }
            OmsStoreInfoPageViewModel pageStoreInfoList = omsResult.getData();
            if(pageStoreInfoList.getList()== null || pageStoreInfoList.getList().isEmpty())
                break;

            updateStoreInfoList(pageStoreInfoList.getList(), storeSource);
            if(syncStoreLog == null){
                saveStoreSyncLog(startDate, endDate, pageStoreInfoList.getList().size(), true,pageNum, storeSource);
            }else {
                syncStoreLog.setRecordCount(pageStoreInfoList.getList().size());
                syncStoreLog.setSuccessed(true);
                syncStoreLogRepository.save(syncStoreLog);
            }
            logger.info("同步门店信息完成.startDate:" +DateUtils.getDateFormat(startDate)   + ",endDate:" + DateUtils.getDateFormat(endDate)+ ",pageNum:" + pageNum);
            pageNum++;
            hasGetAll =pageStoreInfoList.getList()== null || pageStoreInfoList.getList().isEmpty();
        }
    }

    private OmsResult<OmsStoreInfoPageViewModel> getHttpEntity(Date startDate, Date endDate, int pageNum, int pageSize, String url) {
       try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerKey, headerValue);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("apitype", "GetActivationStoreData");
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("startDate", startDate.getTime());
            uriVariables.put("endDate", endDate.getTime());
            uriVariables.put("pageNum", pageNum);
            uriVariables.put("pageSize", pageSize);
            uriVariables.put("t", new Date().getTime());
            url = url + "?startDate={startDate}&endDate={endDate}&pageNum={pageNum}&pageSize={pageSize}&t={t}";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, uriVariables);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (responseEntity.getBody() != null) {
                return mapper.readValue(responseEntity.getBody(), new TypeReference<OmsStoreInfoResult>() {
                });
            }
        } catch (Exception e) {
            e.getStackTrace();
            url = url + "?startDate={startDate}&endDate={endDate}&pageNum={pageNum}&pageSize={pageSize}&t={t}";
            logger.warn("门店信息接口调用异常.startDate:" + DateUtils.getDateFormat(startDate) + ",endDate:" + DateUtils.getDateFormat(endDate)+ ",pageNum:" + pageNum+ ",url:" + url, e);
            return null;
        }
        return null;
    }

    private void updateStoreInfoList(List<OmsStoreInfoViewModel> responseList, Integer storeSource){
        List<StoreInfo> storeInfoList = new ArrayList<>();
        responseList.forEach(responseStoreInfoViewModel -> {
            StoreInfo storeInfo = new StoreInfo();
            storeInfo.setStoreSource(storeSource);
            storeInfo.setStoreNo(responseStoreInfoViewModel.getStoreNo());
            storeInfo.setStoreName(com.sztouyun.advertisingsystem.utils.StringUtils.getMaxLengthString(responseStoreInfoViewModel.getStoreName(),50));
            String provinceId = getAreaIdByCode(responseStoreInfoViewModel.getProvinceRegionCode(),"0");
            storeInfo.setProvinceId(provinceId);
            String cityId = getAreaIdByCode( responseStoreInfoViewModel.getCityRegionCode(),provinceId);
            //接口直辖市返回的直辖市对应的省ID，这里做一下处理
            if(areaService.isMunicipalityCode(responseStoreInfoViewModel.getCityRegionCode())){
                Iterable<Area> cities =areaService.getSubAreas(provinceId);
                cityId = Linq4j.asEnumerable(cities).select(c->c.getId()).firstOrDefault();
            }
            storeInfo.setCityId(cityId);
            String regionId = getAreaIdByCode(responseStoreInfoViewModel.getDistrictRegionCode(),cityId);
            storeInfo.setRegionId(regionId);
            storeInfo.setDeviceId(com.sztouyun.advertisingsystem.utils.StringUtils.getMaxLengthString(responseStoreInfoViewModel.getDeviceNo(),50));//deviceId-设备编码
            storeInfo.setStoreAddress(com.sztouyun.advertisingsystem.utils.StringUtils.getMaxLengthString(responseStoreInfoViewModel.getStoreAddress(),100));//address
            storeInfo.setCreatedAt(responseStoreInfoViewModel.getCreateTime() == null? new Date() : responseStoreInfoViewModel.getCreateTime());//createdAt-开店时间
            storeInfo.setTransactionCount(responseStoreInfoViewModel.getOrderNumber());//orderNumber
            storeInfo.setDeleted(!responseStoreInfoViewModel.getExist());
            storeInfo.setIsQualified(responseStoreInfoViewModel.getQualified());
            storeInfo.setAvailable(responseStoreInfoViewModel.getExist() && responseStoreInfoViewModel.getAvailableDevice());
            storeInfo.setOldStoreNo(responseStoreInfoViewModel.getOldStoreNo());
            if(storeSource.equals(StoreSourceEnum.NEW_OMS.getValue()) ){
                if(responseStoreInfoViewModel.getLatitude()!=null){
                    storeInfo.setLongitude(responseStoreInfoViewModel.getLongitude());
                    storeInfo.setLatitude(responseStoreInfoViewModel.getLatitude());
                }
                storeInfo.setOutsidePicture(responseStoreInfoViewModel.getOutsidePicture());
                storeInfo.setInsidePicture(responseStoreInfoViewModel.getInsidePicture());
            }
            storeInfoList.add(storeInfo);
        });
       if(storeSource.equals(StoreSourceEnum.NEW_OMS.getValue())){
            synchroniseNewStoreInfoList(storeInfoList);
       }
    }

    private int synchroniseNewStoreInfoList(List<StoreInfo> storeList) {
        int total = storeList.size();
        List<String> storeNos = Linq4j.asEnumerable(storeList).selectMany(l -> Linq4j.asEnumerable(Arrays.asList(l.getOldStoreNo(),l.getStoreNo()))).where(a->!StringUtils.isEmpty(a)).distinct().toList();
        Enumerable<StoreInfo> existStoreList = Linq4j.asEnumerable(storeInfoRepository.findAll(QStoreInfo.storeInfo.storeNo.in(storeNos)));
        List<StoreInfo> newStoreInfoList =new ArrayList<>();
        storeList.forEach(storeInfo -> {
            StoreInfo existNewStoreInfo = existStoreList.firstOrDefault(s ->s.getStoreNo().equals(storeInfo.getStoreNo()) );
            if (existNewStoreInfo != null) {
                updateStoreInfo(existNewStoreInfo,storeInfo,StoreSourceEnum.NEW_OMS.getValue());
            }else{
                StoreInfo existOldStoreInfo = existStoreList.firstOrDefault(s ->s.getStoreNo().equals(storeInfo.getOldStoreNo()) );
                if(existOldStoreInfo == null) {//非迁移门店（更新新门店信息）
                    storeInfo.setCreatedTime(new Date());
                    storeInfo.setUpdatedTime(new Date());
                    storeInfo.setGpsPositionUpdateTime(new Date());
                    storeInfo.setIsQualified(!storeInfo.isDeleted() && storeInfo.getIsQualified());
                    newStoreInfoList.add(storeInfo);
                }else {//迁移的门店（更新旧门店信息）
                    updateStoreInfo(existOldStoreInfo,storeInfo,StoreSourceEnum.NEW_OMS.getValue());
                    existOldStoreInfo.setStoreNo(storeInfo.getStoreNo());
                    existOldStoreInfo.setStoreSource(StoreSourceEnum.NEW_OMS.getValue());
                }
            }
        });
        storeInfoRepository.save(existStoreList);
        storeInfoRepository.save(newStoreInfoList);
        mongoTemplate.remove(new Query(Criteria.where("_id").in(existStoreList.select(a->a.getId()).toList())),StoreInfo.class);
        mongoTemplate.insert(new ArrayList<StoreInfo>(){{
            addAll(existStoreList.toList());
            addAll(newStoreInfoList);
        }},StoreInfo.class);
        logger.info("同步新门店数据.size：" + total);
        return total;
    }

    private void updateStoreInfo(StoreInfo existStoreInfo,StoreInfo storeInfo,Integer storeSource){
        if(existStoreInfo != null){
            existStoreInfo.setStoreName(storeInfo.getStoreName());
            existStoreInfo.setStoreAddress(storeInfo.getStoreAddress());
            existStoreInfo.setProvinceId(storeInfo.getProvinceId());
            existStoreInfo.setCityId(storeInfo.getCityId());
            existStoreInfo.setRegionId(storeInfo.getRegionId());
            existStoreInfo.setDeviceId(storeInfo.getDeviceId());
            existStoreInfo.setTransactionCount(storeInfo.getTransactionCount());
            existStoreInfo.setCreatedAt(storeInfo.getCreatedAt());
            existStoreInfo.setIsQualified(!existStoreInfo.getIsTest() && !storeInfo.isDeleted() && storeInfo.getIsQualified());
            existStoreInfo.setDeleted(!existStoreInfo.getIsTest() && storeInfo.isDeleted());
            existStoreInfo.setAvailable(!storeInfo.isDeleted() && storeInfo.isAvailable());
            existStoreInfo.setStoreSource(storeInfo.getStoreSource());
            if(StoreSourceEnum.NEW_OMS.getValue().equals(storeSource)){
                if(existStoreInfo.getLongitude()!=storeInfo.getLongitude() || existStoreInfo.getLatitude()!=storeInfo.getLatitude()){
                    storeInfo.setGpsPositionUpdateTime(new Date());
                }
                existStoreInfo.setLongitude(storeInfo.getLongitude());
                existStoreInfo.setLatitude(storeInfo.getLatitude());
                existStoreInfo.setOutsidePicture(storeInfo.getOutsidePicture());
                existStoreInfo.setInsidePicture(storeInfo.getInsidePicture());
            }
            existStoreInfo.setUpdatedTime(new Date());
        }
    }

    private void saveStoreSyncLog(Date startDate, Date endDate, int total, boolean success, int pageNum, int storeSource) {
        StoreSyncLog syncStoreLog = new StoreSyncLog();
        syncStoreLog.setStartDate(startDate);
        syncStoreLog.setEndDate(endDate);
        syncStoreLog.setRecordCount(total);
        syncStoreLog.setSuccessed(success);
        syncStoreLog.setPageNum(pageNum);
        syncStoreLog.setStoreSource(storeSource);
        syncStoreLogRepository.save(syncStoreLog);
    }

    private void saveStoreSyncLog(Date startDate, Date endDate, int total, boolean success, int pageNum,String jobName) {
        StoreSyncLog syncStoreLog = new StoreSyncLog();
        syncStoreLog.setStartDate(startDate);
        syncStoreLog.setEndDate(endDate);
        syncStoreLog.setRecordCount(total);
        syncStoreLog.setSuccessed(success);
        syncStoreLog.setPageNum(pageNum);
        syncStoreLog.setJobName(jobName);
        syncStoreLog.setStoreSource(null);
        syncStoreLogRepository.save(syncStoreLog);
    }

    private String getUrlByStoreSource(Integer storeSource) {
        StoreSourceEnum storeSourceEnum = EnumUtils.toEnum(storeSource, StoreSourceEnum.class);
        switch (storeSourceEnum) {
            case OMS:
                return getUrl;
            case NEW_OMS:
                return omsNewUrl;
            default:
                return "";
        }
    }

    public void pushStoreToPartner(String partnerId){
       CooperationPartner cooperationPartner=cooperationPartnerRepository.findById(partnerId);
       if(cooperationPartner==null)
           throw new BusinessException("合作方ID无效");
       if(cooperationPartner.isDisabled())
           throw new BusinessException("合作方已被禁用");
        Long count=storeInfoRepository.count(qStoreInfo.available.isTrue());
        UpdateStoreInfoNotificationData data=new UpdateStoreInfoNotificationData();
        Map<String,String> allArea=areaService.getAllAreaNames();
        data.setPartnerId(partnerId);
        Integer pageIndex=0;
        while(count>0){
            Page<StoreInfo> page=storeInfoRepository.findAll(qStoreInfo.available.isTrue(),new PageRequest(pageIndex++,pushRecordSize));
            if(page.getContent()!=null && !page.getContent().isEmpty()){
                data.setStoreInfoList(convertToPartnerStoreInfoList(page.getContent(),allArea));
                publishOpenApiNotification(data);
            }
            count-=pushRecordSize;
        }
    }

    private void pushUpdateList(List<PartnerStoreInfo> storeInfoList ){
        if(storeInfoList.isEmpty())
            return ;
        UpdateStoreInfoNotificationData data=new UpdateStoreInfoNotificationData();
        data.setStoreInfoList(storeInfoList);
        publishOpenApiNotification(data);
    }

    private void pushDeleteList( List<String> storeIds){
        if( storeIds.isEmpty())
            return ;
        DeleteStoreInfoNotificationData data=new DeleteStoreInfoNotificationData(){};
        data.setStoreIds(storeIds);
        publishOpenApiNotification(data);
    }

    private  List<PartnerStoreInfo> convertToPartnerStoreInfoList(List<StoreInfo> list,Map<String,String> allArea){
        List<PartnerStoreInfo> partnerStoreInfoList=new ArrayList<>();
        list.stream().forEach(item->{
            partnerStoreInfoList.add(getPartnerStoreInfo(item,allArea));
        });
        return partnerStoreInfoList;
    }

    private PartnerStoreInfo  getPartnerStoreInfo(StoreInfo storeInfo, Map<String,String> allArea){
        PartnerStoreInfo partnerStoreInfo=new PartnerStoreInfo();
        partnerStoreInfo.setId(storeInfo.getId());
        partnerStoreInfo.setStoreName(storeInfo.getStoreName());
        partnerStoreInfo.setProvince(allArea.get(storeInfo.getProvinceId()));
        partnerStoreInfo.setCity(allArea.get(storeInfo.getCityId()));
        partnerStoreInfo.setRegion(allArea.get(storeInfo.getRegionId()));
        partnerStoreInfo.setAddress(storeInfo.getStoreAddress());
        return partnerStoreInfo;
    }

    public void syncStorePortraitInfo(Date startDate,Date endDate,String jobName,Integer pageSize){
        Integer startPageIndex=1;
        Map<String,Object> param=HttpUtils.getDefaultCommonParam(startDate.getTime(),endDate.getTime());
        OmsResult<OmsData<OmsStorePortrait>> result=null;
        do {
            Integer finalPageNum=startPageIndex;
            param.put("pageNum",startPageIndex);
            StoreSyncLog syncStoreLog = syncStoreLogRepository.findOne(q->q.selectFrom(qStoreSyncLog)
                    .where(qStoreSyncLog.startDate.eq(startDate).and(qStoreSyncLog.endDate.eq(endDate)).and(qStoreSyncLog.pageNum.eq(finalPageNum)).and(qStoreSyncLog.jobName.eq(jobName))));
            if(syncStoreLog !=null && syncStoreLog.isSuccessed()){
                startPageIndex++;
            }
            result= omsNewThirdPart.getOmsStorePortraitList(param);
            if(result==null || result.getData().getList().isEmpty())
                break;
            updateStorePortraitInfo(result);
            if(syncStoreLog == null){
                saveStoreSyncLog(startDate, endDate, result.getData().getList().size(), true,startPageIndex, jobName);
            }else {
                syncStoreLog.setRecordCount(result.getData().getList().size());
                syncStoreLog.setSuccessed(true);
                syncStoreLogRepository.save(syncStoreLog);
            }

        }while (result!=null && pageSize==result.getData().getList().size());

    }

    private void updateStorePortraitInfo(OmsResult<OmsData<OmsStorePortrait>> omsResult){
        if(omsResult!=null && !omsResult.getData().getList().isEmpty()){
            List<OmsStorePortrait> omsStorePortraits=omsResult.getData().getList();
            List<String> storeNos=omsStorePortraits.stream().map(a->a.getStoreNo()).collect(Collectors.toList());
            List<StoreInfoExtension> storeInfoExtensions=new ArrayList<>();
            List<StoreInfo> updateStoreList=new ArrayList<>();
            Map<String,StoreInfo> storeInfoMap=storeInfoRepository.findAll(qStoreInfo.storeNo.in(storeNos),new JoinDescriptor().leftJoin(qStoreInfo.storeInfoExtension)).stream().collect(Collectors.toMap(StoreInfo::getStoreNo,storeInfo -> storeInfo));
            omsStorePortraits.stream().forEach(item->{
                StoreInfo storeInfo=storeInfoMap.get(item.getStoreNo());
                StoreInfoExtension storeInfoExtension=null;
                if(storeInfo!=null){
                    if(storeInfo.getStoreInfoExtension()==null){
                        storeInfoExtension=new StoreInfoExtension();
                        storeInfoExtension.setStoreInfo(storeInfo);
                    }else {
                        storeInfoExtension=storeInfo.getStoreInfoExtension();
                    }
                    updateSurroundings(item,storeInfo.getId());
                    setStoreInfoExtension(storeInfoExtension,item);
                    storeInfo.setStoreInfoExtension(storeInfoExtension);
                    updateStoreList.add(storeInfo);
                    storeInfoExtensions.add(storeInfoExtension);
                }
            });
            if(!storeInfoExtensions.isEmpty()){
                storeInfoExtensionRepository.save(storeInfoExtensions);
            }
        }
    }



    private void setStoreInfoExtension(StoreInfoExtension storeInfoExtension,OmsStorePortrait omsStorePortrait){
        StoreFrontTypeEnum storeFrontTypeEnum=EnumUtils.displayNameToEnum(omsStorePortrait.getStoreType(),StoreFrontTypeEnum.class);
        if(storeFrontTypeEnum!=null){
            storeInfoExtension.setStoreFrontType(storeFrontTypeEnum.getValue());
        }
        DailySalesEnum dailySalesEnum=EnumUtils.displayNameToEnum(omsStorePortrait.getDailySales(),DailySalesEnum.class);
        if(dailySalesEnum!=null) {
            storeInfoExtension.setDailySales(dailySalesEnum.getValue());
        }
        DecorationEnum decorationEnum=EnumUtils.displayNameToEnum(omsStorePortrait.getDecoration(),DecorationEnum.class);
        if(decorationEnum!=null) {
            storeInfoExtension.setDecoration(decorationEnum.getValue());
        }
        CommercialAreaEnum commercialAreaEnum=EnumUtils.displayNameToEnum(omsStorePortrait.getUsageArea(),CommercialAreaEnum.class);
        if(commercialAreaEnum!=null) {
            storeInfoExtension.setCommercialArea(commercialAreaEnum.getValue());
        }
        OrderRatioEnum orderRatioEnum=EnumUtils.displayNameToEnum(omsStorePortrait.getTheBookingRatioOfItsFranchisees(),OrderRatioEnum.class);
        if(orderRatioEnum!=null) {
            storeInfoExtension.setOrderRatio(orderRatioEnum.getValue());
        }
        storeInfoExtension.setBankCard(omsStorePortrait.getBankCardNo());
        storeInfoExtension.setProfitability(omsStorePortrait.getProfit());
        storeInfoExtension.setResidentialDistrictCount(omsStorePortrait.getResidentialNum());
        List<OmsStoreAttachment> omsStoreAttachments=omsStorePortrait.getOmsStoreAttachments();
        omsStoreAttachments.stream().forEach(attachment->{
            if(attachment!=null){
                 StorePictureTypeEnum storePictureTypeEnum=EnumUtils.toEnum(attachment.getPictureType(),StorePictureTypeEnum.class);
                switch (storePictureTypeEnum){
                    case InsidePicture:
                        storeInfoExtension.setInsidePicture(attachment.getUrl());
                        break;
                    case OutsidePicture:
                        storeInfoExtension.setOutsidePicture(attachment.getUrl());
                        break;
                    case CashRegisterPicture:
                        storeInfoExtension.setCashRegisterPicture(attachment.getUrl());
                        break;
                        default:break;
                }
            }
        });
    }

    @Transactional
    public void updateSurroundings(OmsStorePortrait omsStorePortrait,String storeId) {
        List<StorePortrait> storePortraits=new ArrayList<>();
        if(com.sztouyun.advertisingsystem.utils.StringUtils.convertToBoolean(omsStorePortrait.getCommercialArea())){
            storePortraits.add(new StorePortrait(storeId, StorePortraitEnum.surroundingsDistrict.getValue(), SurroundingsDistrictEnum.CommercialDistrict.getValue()));
        }
        if(com.sztouyun.advertisingsystem.utils.StringUtils.convertToBoolean(omsStorePortrait.getResidential())){
            storePortraits.add(new StorePortrait(storeId,StorePortraitEnum.surroundingsDistrict.getValue(),SurroundingsDistrictEnum.ResidentialDistrict.getValue()));
        }
        if(com.sztouyun.advertisingsystem.utils.StringUtils.convertToBoolean(omsStorePortrait.getSchool())){
            storePortraits.add(new StorePortrait(storeId,StorePortraitEnum.surroundingsDistrict.getValue(),SurroundingsDistrictEnum.SchoolDistrict.getValue()));
        }
        if(storePortraitRepository.exists(qStorePortrait.storeId.eq(storeId))){
            storePortraitRepository.deleteStorePortraitByStoreId(storeId);
        }
        if(!storePortraits.isEmpty()){
            storePortraitRepository.save(storePortraits);
        }
    }

    public void calcStoreOpenCloseTime(Date start,Date end){
        while(!start.after(end)){
            calcDailyStoreOpenCloseTime(start);
            start=new LocalDate(start).plusDays(1).toDate();
        }
    }

    private void calcDailyStoreOpenCloseTime(Date date){
        logger.info("计算门店开关机时间：" + DateUtils.getCurrentFormat());
        Long datetime=new LocalDate(date).toDate().getTime();
        MapReduceOptions mro=new MapReduceOptions();
        mro.outputTypeMerge();
        mro.outputCollection("storeDeviceDailyStatisticResult");
        try {
            mongoTemplate.mapReduce(
                    new Query(Criteria.where("createdDate").is(datetime)),
                    "storeDeviceHeartbeat",
                    "classpath:script/storeOpenCloseTime/mapFunc.js",
                    "classpath:script/storeOpenCloseTime/reduceFunc.js",
                    mro,
                    StoreDeviceDailyStatisticResult.class
            );
        }catch (Exception e){
            logger.error("计算门店开关机时间异常，时间"+DateUtils.dateFormat(date, Constant.DATA_YMD_CN) ,e);
        }
    }

    public void syncStoreNearByInfo(){
        Date now=new Date();
        Integer index=0;
        Integer pageSize=20;
        DayPageRequest request=new DayPageRequest(now);
        request.setPageSize(pageSize);
        while(true){
            request.setPageIndex(index);
            List<StoreNearByUpdateInfo> updateList=storeNearByMapper.findNeedSyncStoreInfo(request);
           if(CollectionUtils.isEmpty(updateList))
                break;
            updateStoreNearBy(updateList,now,Constant.POI_SEARCH_DISTANCE);
            index++;
        }

    }

    private void updateStoreNearBy(List<StoreNearByUpdateInfo> list,Date date,int distance){
        List<StoreNearBy> storeNearByList=new ArrayList<>();
        list.forEach(storeInfo -> {
            var storeNearBy =getStoreNearBy(storeInfo,date,distance);
            if(storeNearBy !=null){
                storeNearByList.add(storeNearBy);
            }
        });
        storeNearByMapper.syncStoreNearBy(storeNearByList);
    }

    private StoreNearBy getStoreNearBy(StoreNearByUpdateInfo storeInfo, Date date,int distance){
        StoreNearBy item=new StoreNearBy();
        item.setUpdatedTime(date);
        item.setId(storeInfo.getId());
        double longitude=storeInfo.getLongitude();
        double latitude=storeInfo.getLatitude();
        try {
            item.setCommercialDistrictCount(mapService.searchNearByCount(longitude,latitude,distance, EnvironmentTypeEnum.CommercialDistrict.getPoiTypes()));
            item.setResidentialDistrictCount(mapService.searchNearByCount(longitude,latitude,distance, EnvironmentTypeEnum.ResidentialDistrict.getPoiTypes()));
            item.setSchoolDistrictCount(mapService.searchNearByCount(longitude,latitude,distance, EnvironmentTypeEnum.SchoolDistrict.getPoiTypes()));
            ThreadUtil.sleep(100);
        }catch (Exception e){
            logger.warn("从高德地图拉取门店周边信息异常,时间"+DateUtils.dateFormat( LocalDateTime.now().toDate(),Constant.DATETIME),e);
            item.setUpdatedTime(new DateTime("2017-01-01").toDate());
        }
        item.updateMainEnvironmentType();
        return item;
    }


}