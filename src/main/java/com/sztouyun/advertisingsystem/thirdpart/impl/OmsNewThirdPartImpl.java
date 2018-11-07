package com.sztouyun.advertisingsystem.thirdpart.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.TimeSpan;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.store.OmsData;
import com.sztouyun.advertisingsystem.model.store.OmsStorePortrait;
import com.sztouyun.advertisingsystem.thirdpart.IThirdPart;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.job.OmsResult;
import com.sztouyun.advertisingsystem.viewmodel.job.StoreOpeningTime;
import com.sztouyun.advertisingsystem.viewmodel.job.StoreResult;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.NewStoreOrderInfo;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeDetailRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOrderRequest;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OmsNewThirdPartImpl implements IThirdPart{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${oms.store.info.url.new}")
    private String omsNewUrl;
    @Value("${oms.store.info.header.key}")
    private String headerKey;
    @Value("${oms.store.info.header.value}")
    private String headerValue;
    @Autowired
    private StoreThirdPartService storeThirdPartService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Map<String,Integer> getStoresDailyOrder(StoreOrderRequest storeOrderRequest) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerKey, headerValue);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
            requestData.add("storeNos", StringUtils.join(storeOrderRequest.getShopIds(), Constant.SEPARATOR));
            requestData.add("startDate",DateUtils.dateFormat(storeOrderRequest.getDate(),Constant.DATA_YMD));
            requestData.add("endDate",DateUtils.dateFormat(new LocalDate(storeOrderRequest.getDate()).plusDays(1).toDate(),Constant.DATA_YMD));
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(requestData,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(this.omsNewUrl+"?ApiType=GetStoreOrdersByStoreNo", HttpMethod.POST, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (responseEntity.getBody() != null) {
                OmsResult<List<NewStoreOrderInfo>> viewModel = mapper.readValue(responseEntity.getBody(), new TypeReference<OmsResult<List<NewStoreOrderInfo>>>() {});
                return Linq4j.asEnumerable(viewModel.getData()).toMap(q->q.getStoreNo(),l->l.getOrderQty());
            }
        } catch (Exception e) {
            logger.warn("获取新门店订单失败",e);
            e.printStackTrace();
            return new HashMap<>();
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, TimeSpan> getStoreOpeningTimes(StoreOpeningTimeRequest storeOpeningTimeRequest) {
        StoreResult<StoreOpeningTime> result=storeThirdPartService.getStoreOpeningTime(storeOpeningTimeRequest);
        Map<String, TimeSpan> map =  new HashMap<>();
        if(result==null || result.getData() ==null || result.getData().isEmpty())
            return map;
        result.getData().stream().forEach(a-> map.put(a.getShopId(),new TimeSpan(getTimeStamp(a.getBeginTime()),getTimeStamp(a.getEndTime()))));
        return map;
    }

    @Override
    public Page<Date> getStoreOpeningTimeDetail(StoreOpeningTimeDetailRequest storeOpeningTimeDetailsRequest) {
        return new PageImpl<>(storeThirdPartService.getStoreOpeningTimeDetail(storeOpeningTimeDetailsRequest));
    }

    private long getTimeStamp(Date date){
        if(date==null)
            return 0L;
        return date.getTime();
    }

    public OmsResult<OmsData<OmsStorePortrait>> getOmsStorePortraitList(Map<String,Object> param){
        return getOmsDate(restTemplate, param, "GetStorePortrait", new TypeReference<OmsResult<OmsData<OmsStorePortrait>>>() {
        });
    }

    public  <D> OmsResult<OmsData<D>> getOmsDate(RestTemplate restTemplate, Map<String,Object> param,String apiName,TypeReference<OmsResult<OmsData<D>> > typeReference){
        StringBuffer url=new StringBuffer(omsNewUrl).append("?");
        Logger logger = LoggerFactory.getLogger(apiName);
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerKey,headerValue);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("apitype", apiName);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        if(param==null)
            throw new BusinessException("请设置API参数");
        Map<String, Object> uriVariables = new HashMap<>();
        param.keySet().forEach(key->{
            uriVariables.put(key, param.get(key));
            url.append(key);
            url.append("=");
            url.append(param.get(key));
            url.append("&");
        });
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class, uriVariables);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (responseEntity.getBody() != null) {
                return mapper.readValue(responseEntity.getBody(), typeReference);
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.warn("新运维"+apiName+"接口调用异常.startDate:" + DateUtils.getDateFormat(new Date(Long.valueOf(param.get("startDate").toString()))) + ",endDate:" + DateUtils.getDateFormat(new Date(Long.valueOf(param.get("endDate").toString())))+ ",pageNum:" + param.get("pageNum").toString()+ ",url:" + url.toString(), e);
            return null;
        }
        return null;
    }


}
