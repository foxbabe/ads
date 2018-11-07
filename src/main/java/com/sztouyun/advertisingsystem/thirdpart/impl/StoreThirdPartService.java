package com.sztouyun.advertisingsystem.thirdpart.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.job.StoreOpeningTime;
import com.sztouyun.advertisingsystem.viewmodel.job.StoreResult;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeDetailRequest;
import com.sztouyun.advertisingsystem.viewmodel.thirdpart.store.StoreOpeningTimeRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/3/22.
 */
@Component
public class StoreThirdPartService {
    @Value("${store.sys.url}")
    private String url;

    @Value("${store.sys.header.key}")
    private String headerKey;

    @Value("${store.sys.header.value}")
    private String headerValue;
    @Autowired
    private RestTemplate restTemplate;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public StoreResult<StoreOpeningTime> getStoreOpeningTime(StoreOpeningTimeRequest request){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerKey, headerValue);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            String shopIds=StringUtils.join(request.getShopIds(), Constant.SEPARATOR);
            param.add("shopIds", shopIds) ;
            param.add("date", DateUtils.dateFormat(request.getDate(),Constant.DATA_YMD));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(param,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url +"/shop/shop_open_close_time" , HttpMethod.POST, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (responseEntity.getBody() != null) {
                StoreResult<StoreOpeningTime> storeOpeningTimeList = mapper.readValue(responseEntity.getBody(), new TypeReference<StoreResult<StoreOpeningTime>>() {
                });
                return storeOpeningTimeList;
            }
        } catch (Exception e) {
            logger.warn("获取门店开机时长失败,url:"+url +"/shop/shop_open_close_time",e);
            e.printStackTrace();
            return new StoreResult<>();
        }
        return new StoreResult<>();
    }

    public List<Date> getStoreOpeningTimeDetail(StoreOpeningTimeDetailRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerKey, headerValue);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("shopId", request.getShopId()) ;
            param.add("date", DateUtils.dateFormat(request.getDate(),Constant.DATA_YMD));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(param,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url +"/shop/device_heartbeat_data" , HttpMethod.POST, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDateFormat(new SimpleDateFormat(Constant.DATETIME));
            if (responseEntity.getBody() != null) {
                StoreResult<Date> storeResult = mapper.readValue(responseEntity.getBody(), new TypeReference<StoreResult<Date>>() {
                });
                return storeResult.getData();
            }
        } catch (Exception e) {
            logger.warn("获取门店心跳失败,url:"+url +"/shop/device_heartbeat_data",e);
            e.printStackTrace();
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
