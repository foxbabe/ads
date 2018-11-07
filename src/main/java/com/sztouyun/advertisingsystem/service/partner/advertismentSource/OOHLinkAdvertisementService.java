package com.sztouyun.advertisingsystem.service.partner.advertismentSource;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerMediaConfigMapper;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementRequestResultEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.config.OOHLinkApiConfig;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.HttpUtils;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.viewmodel.partner.ConnectionTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.*;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class OOHLinkAdvertisementService extends BasePartnerAdvertisementService {
    @Autowired
    private OOHLinkApiConfig oohLinkApiConfig;
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private PartnerMediaConfigMapper partnerMediaConfigMapper;
    @Value("${partner.advertisement.local.monitor.url}")
    private String adsMonitorUrl;
    @Value("${partner.advertisement.http.time.out}")
    private Integer httpTimeOut;

    private Logger logger = Logger.getLogger(OOHLinkAdvertisementService.class);
    private RestTemplate restTemplate = new RestTemplate();
    private static final long REQUEST_SUCCESS_CODE = 0;
    private static final long RESULT_SUCCESS_CODE = 0;
    private static final long AD_NO_DATA_CODE = 1;

    private final List<OOHLinkAdMaterialTypeEnum> allMaterialTypes = Arrays.asList(OOHLinkAdMaterialTypeEnum.IMAGE,OOHLinkAdMaterialTypeEnum.VIDEO);

    private final Map<ConnectionTypeEnum, Integer> connectionTypeEnumIntegerMap = new HashMap(){{
        put(ConnectionTypeEnum.UNKNOWN_NETWORK, 0);
        put(ConnectionTypeEnum.WIFI, 100);
        put(ConnectionTypeEnum.MOBILE_2G, 2);
        put(ConnectionTypeEnum.MOBILE_3G, 3);
        put(ConnectionTypeEnum.MOBILE_4G, 4);
        put(ConnectionTypeEnum.ETHERNET, 101);
        put(ConnectionTypeEnum.NEW_TYPE, 999);
    }};

    public OOHLinkAdvertisementService() {
        super(AdvertisementSourceEnum.OOHLINK);
    }

    @Override
    public List<PartnerAdvertisementDeliveryRecord> getPartnerAdvertisements(StoreInfoRequest storeInfoRequest) {
        CooperationPartner cooperationPartner = cooperationPartnerService.findCooperationPartnerById(oohLinkApiConfig.getPartnerId());

        if(cooperationPartner.isDisabled())
            throw new BusinessException("合作方不存在或者被禁用!! 合作方ID为: " + oohLinkApiConfig.getPartnerId());

        storeInfoRequest.setPartnerId(oohLinkApiConfig.getPartnerId());
        Boolean existsMediaInfo = partnerMediaConfigMapper.existsMediaInfo(storeInfoRequest);

        if(existsMediaInfo == null || !existsMediaInfo)
            throw new BusinessException("该门店不在媒体配置表中, 门店ID为: " + storeInfoRequest.getStoreId());


        List<PartnerAdvertisementDeliveryRecord> partnerAdvertisementDeliveryRecords = new ArrayList<>();
        try {
            storeInfoRequest.setAdvertisementPositionCategory(AdvertisementPositionCategoryEnum.FullScreen.getValue());
            RTBRequest request = generateRequestBody(storeInfoRequest);
            Response response;
            ResponseEntity<Response> responseResponseEntity;


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap();

            HttpUtils.setHttpTimeout(restTemplate, httpTimeOut);
            for (var materialType : allMaterialTypes) {
                request.getAdSlot().setType(materialType.getValue().shortValue());
                String requestJson = ObjectMapperUtils.toJsonString(request);
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("rtbRequest", requestJson);
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(map, headers);
                map.add("rtbRequest", requestJson);

                responseResponseEntity = restTemplate.exchange(cooperationPartner.getApiUrl(), HttpMethod.POST, entity, Response.class, uriVariables);
                if (responseResponseEntity == null) {
                    throw new BusinessException("获取奥凌广告失败");
                }
                savePartnerAdvertisementRequestLog(storeInfoRequest.getPartnerId(),storeInfoRequest.getStoreId(),EnumUtils.toEnum(storeInfoRequest.getAdvertisementPositionCategory(), AdvertisementPositionCategoryEnum.class),getRequestResultEnum(responseResponseEntity));
                response = responseResponseEntity.getBody();
                if(response !=null){
                    logger.info("成功获取奥凌接口数据: " + response.toString());
                }
                if (response != null && response.getCode() == REQUEST_SUCCESS_CODE && response.getData().getErrorCode().equals((short)RESULT_SUCCESS_CODE)) {
                    partnerAdvertisementDeliveryRecords.addAll(packResultData(response, storeInfoRequest, cooperationPartner.getDuration()));
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("请求奥凌广告失败", e);
        }
        return partnerAdvertisementDeliveryRecords;
    }

    private RTBRequest generateRequestBody(StoreInfoRequest storeInfoRequest) {
        // 广告请求参数
        RTBRequest rtbRequest = new RTBRequest();
        rtbRequest.setPlayCode(storeInfoRequest.getStoreNo());
        rtbRequest.setChannelId(oohLinkApiConfig.getChannelId());
        rtbRequest.setToken(oohLinkApiConfig.getToken());
        rtbRequest.setRequestId(UUIDUtils.generateOrderedUUID());


        // 媒体设备信息
        MediaDevice mediaDevice = new MediaDevice();
        mediaDevice.setDeviceType((short) 4); // (1:手机；2:平板；3：智能电视;4:户外媒体)
        mediaDevice.setOsType((short) 1);   //操作系统类型(1:Android; 2:IOS; 3:Windows)
        mediaDevice.setOsVersion(storeInfoRequest.getOsVersion());
        mediaDevice.setScreenWidth(oohLinkApiConfig.getScreenSizeWidth()); // 设备屏幕宽(像素)
        mediaDevice.setScreenHeight(oohLinkApiConfig.getScreenSizeHeight()); // 设备屏幕高(像素)
        mediaDevice.setVendor(storeInfoRequest.getVendor());
        mediaDevice.setModel(storeInfoRequest.getModel());
        rtbRequest.setDevice(mediaDevice);

        // 移动网络参数
        MediaNetwork mediaNetwork = new MediaNetwork();
        mediaNetwork.setConnectionType((short) connectionTypeEnumIntegerMap.get(EnumUtils.toEnum(storeInfoRequest.getConnectionType(), ConnectionTypeEnum.class)).intValue());
        mediaNetwork.setIpv4(storeInfoRequest.getRequestIp());
        mediaNetwork.setOperatorType((short) 3);//运营商类型(1:移动;2:电信;3:联通;99:其他)
        rtbRequest.setNetwork(mediaNetwork);

        // 媒体设备识别码
        MediaUdId udId = new MediaUdId();
        udId.setMac(storeInfoRequest.getMac());
        rtbRequest.setUdid(udId);

        // 广告位参数
        AdSlot adSlot = new AdSlot();
        adSlot.setAdslotWidth(oohLinkApiConfig.getScreenSizeWidth()); // 广告位宽度
        adSlot.setAdslotHeight(oohLinkApiConfig.getScreenSizeHeight()); // 广告位高度
        adSlot.setDuration(oohLinkApiConfig.getDuration()); //空闲时长(秒)
        adSlot.setType((short) OOHLinkAdMaterialTypeEnum.IMAGE.getValue().intValue());
        rtbRequest.setAdSlot(adSlot);
        rtbRequest.setExt(new HashMap<>());
        return rtbRequest;
    }

    private List<PartnerAdvertisementDeliveryRecord> packResultData(Response response, StoreInfoRequest storeInfoRequest, Integer duration) {
        RTBPlanInfo rtbPlanInfo = response.getData();
        PartnerAdvertisementDeliveryRecord record = new PartnerAdvertisementDeliveryRecord();
        record.setThirdPartId(rtbPlanInfo.getPlanId().toString());
        record.setPartnerAdvertisementId(getPartnerAdvertisementId(record.getThirdPartId()));
        record.setStoreId(storeInfoRequest.getStoreId());
        record.setAdvertisementPositionCategory(AdvertisementPositionCategoryEnum.FullScreen.getValue());

        List<String> winNoticeUrlList = rtbPlanInfo.getWinNoticeUrlList();
        if (!CollectionUtils.isEmpty(rtbPlanInfo.getAdTrackList())) {
            List<String> startTrackUrl = Linq4j.asEnumerable(rtbPlanInfo.getAdTrackList()).where(a -> a.getType().equals(OOHLinkAdvertisementTrackEnum.StartPlay.getValue().shortValue())).select(b -> StringUtils.join(b.getTrackList(), ",")).toList();
            winNoticeUrlList.addAll(startTrackUrl);
        }
        record.setStartDisplayMonitorUrls(StringUtils.join(winNoticeUrlList, ","));

        if (!CollectionUtils.isEmpty(rtbPlanInfo.getAdTrackList())) {
            List<String> endTrackUrl = Linq4j.asEnumerable(rtbPlanInfo.getAdTrackList()).where(a -> a.getType().equals(OOHLinkAdvertisementTrackEnum.EndPlay.getValue().shortValue())).select(b -> StringUtils.join(b.getTrackList(), ",")).toList();
            record.setMonitorUrls(StringUtils.join(endTrackUrl, ","));
        }

        record.setRequestTime(new Date().getTime());
        record.setEffectiveFinishTime(new DateTime().plusMinutes(duration).toDate().getTime());
        record.setValid(false);
        record.setPartnerId(oohLinkApiConfig.getPartnerId());
        record.setDuration(oohLinkApiConfig.getDuration());
        record.setRequestId(rtbPlanInfo.getRequestId());

        MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum((int)rtbPlanInfo.getMatType(), OOHLinkAdMaterialTypeEnum.class).getPartnerMaterialType();
        record.setMaterialType(materialTypeEnum.getValue());

        record.setMaterialSpecification(getMaterialSpecification(rtbPlanInfo.getMatWidth(), rtbPlanInfo.getMatHeight()));
        record.setMd5(rtbPlanInfo.getMatMd5());
        record.setOriginalUrl(rtbPlanInfo.getMatUrl());

        packExtensionData(record, rtbPlanInfo);
        return Arrays.asList(record);
    }

    private String getMaterialSpecification(int width, int height) {
        StringBuffer sb = new StringBuffer();
        return sb.append(width).append("*").append(height).toString();
    }

    private void packExtensionData(PartnerAdvertisementDeliveryRecord record, RTBPlanInfo rtbPlanInfo) {
        OOHLinkAdvertisementExtension extension = new OOHLinkAdvertisementExtension();
        extension.setChannelId(rtbPlanInfo.getChannelId());
        extension.setFileName(rtbPlanInfo.getFileName());
        record.setExtend(extension);
    }

    private PartnerAdvertisementRequestResultEnum getRequestResultEnum(ResponseEntity<Response> response){
        if(response ==null || response.getBody() ==null)
            return PartnerAdvertisementRequestResultEnum.ApiError;
        if(response.getBody().getData().getErrorCode() == RESULT_SUCCESS_CODE)
            return PartnerAdvertisementRequestResultEnum.GetAd;
        if(response.getBody().getData().getErrorCode() == AD_NO_DATA_CODE)
            return PartnerAdvertisementRequestResultEnum.GetNoAd;
        return PartnerAdvertisementRequestResultEnum.ApiError;
    }
}
