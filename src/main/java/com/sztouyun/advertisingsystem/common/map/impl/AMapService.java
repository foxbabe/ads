package com.sztouyun.advertisingsystem.common.map.impl;

import com.sztouyun.advertisingsystem.common.map.IMapService;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.viewmodel.store.AMapResponseViewModel;
import lombok.experimental.var;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * 高德地图服务
 */
@Service
public class AMapService implements IMapService {

    private RestTemplate restTemplate = new RestTemplate();
    Logger logger = LoggerFactory.getLogger(AMapService.class);

    @Value("${lbs.amap.com.key}")
    private String key;

    @Value("${lbs.amap.com.url}")
    private String url;

    private static final Integer SUCCESS_CODE = 10000;

    @Override
    public int searchNearByCount(double longitude, double latitude, int distance, Collection<String> poiTypes) {
        StringBuffer stringBuffer = new StringBuffer()
                .append("key=").append(key)
                .append("&location=").append(longitude).append(",").append(latitude)
                .append("&keywords=").append(StringUtils.join(poiTypes, "|"))
                .append("&offset=").append(1)
                .append("&radius=").append(distance);

        var apiUrl = url + "?" + stringBuffer.toString();
        AMapResponseViewModel result;
        ResponseEntity<AMapResponseViewModel> entity = restTemplate.getForEntity(apiUrl, AMapResponseViewModel.class);
        result = entity.getBody();
        if (result == null) {
            logger.warn("请求失败");
            throw new BusinessException("请求失败");
        }
        if(!result.getInfoCode().equals(SUCCESS_CODE)) {
            logger.warn("无法获取高德地图数据, 高德地图错误码为:" + result.getInfoCode());
            throw new BusinessException("无法获取高德地图数据, 高德地图错误码为:" + result.getInfoCode());
        }
        return result.getCount();
    }
}
