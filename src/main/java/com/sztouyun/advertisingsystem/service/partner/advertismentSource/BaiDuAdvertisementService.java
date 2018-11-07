package com.sztouyun.advertisingsystem.service.partner.advertismentSource;

import com.google.protobuf.ByteString;
import com.sztouyun.advertisingsystem.config.EnvironmentConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementRequestResultEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.partner.PartnerAdSlotEnum;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.config.BaiDuApiConfig;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.partner.ConnectionTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.partner.baiduRTB.BaiduRTBInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.baiduRTB.PartnerAdMaterialTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.partner.store.StoreInfoRequest;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class BaiDuAdvertisementService  extends BasePartnerAdvertisementService {
    private Logger logger = Logger.getLogger(BaiDuAdvertisementService.class);
    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Autowired
    private BaiDuApiConfig baiDuApiConfig;
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private EnvironmentConfig environmentConfig;
    @Value("${partner.advertisement.http.time.out}")
    private Integer httpTimeOut;

    private Map<PartnerAdSlotEnum, String> advertisementPositionTypeEnumMap;

    private static final long SUCCESS_CODE = 0;
    private static final long AD_NO_DATA_CODE = 201000L;
    private static final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;

    public BaiDuAdvertisementService() {
        super(AdvertisementSourceEnum.BAIDU);
    }

    @Autowired
    public void setAdvertisementPositionCategoryEnumMap() {
        advertisementPositionTypeEnumMap = new HashMap<PartnerAdSlotEnum, String>(){{
            put(PartnerAdSlotEnum.FullScreenImg, baiDuApiConfig.getFullScreenImgAdsLotId());
            put(PartnerAdSlotEnum.FullScreenVideo, baiDuApiConfig.getFullScreenVideoAdsLotId());
            put(PartnerAdSlotEnum.ScanPayImg, baiDuApiConfig.getScanPayImgAdsLotId());
            put(PartnerAdSlotEnum.ScanPayVideo, baiDuApiConfig.getScanPayVideoAdsLotId());
        }};
    }

    @Override
    public List<PartnerAdvertisementDeliveryRecord> getPartnerAdvertisements(StoreInfoRequest storeInfoRequest) {
        List<PartnerAdvertisementDeliveryRecord> partnerAdvertisementDeliveryRecords = new ArrayList<>();
        if(!baiDuApiConfig.getPartnerId().equals(storeInfoRequest.getPartnerId()))
            return partnerAdvertisementDeliveryRecords;

        CooperationPartner cooperationPartner = cooperationPartnerService.findCooperationPartnerById(baiDuApiConfig.getPartnerId());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new ProtobufHttpMessageConverter()));
        storeInfoRequest.setAdvertisementPositionCategory(AdvertisementPositionCategoryEnum.FullScreen.getValue());
        BaiduRTBInfo.TsApiRequest tsApiRequest = generateRequestBody(storeInfoRequest);
        ResponseEntity<BaiduRTBInfo.TsApiResponse> tsApiResponseResponseEntity;
        BaiduRTBInfo.TsApiResponse response;
        var adSlots = cooperationPartnerService.getPrioritizedAdSlots(baiDuApiConfig.getPartnerId());
        if(!environmentConfig.isOnline()){
            logger.info("广告位优先级："+String.join(",", Linq4j.asEnumerable(adSlots).select(a->a.toString())));
        }
        for (var adSlot: adSlots){
            PartnerAdSlotEnum partnerAdSlot = EnumUtils.toEnum(adSlot,PartnerAdSlotEnum.class);
            var advertisementPositionCategory =partnerAdSlot.getAdvertisementPositionCategory();
            storeInfoRequest.setAdvertisementPositionCategory(advertisementPositionCategory.getValue());
            tsApiRequest = setSlotInfo(tsApiRequest,partnerAdSlot);
            try {
                tsApiResponseResponseEntity = restTemplate.postForEntity(cooperationPartner.getApiUrl(), tsApiRequest, BaiduRTBInfo.TsApiResponse.class);
            } catch (Exception e) {
                logger.warn("请求百度广告失败", e);
                tsApiResponseResponseEntity = null;
            }
            savePartnerAdvertisementRequestLog(storeInfoRequest.getPartnerId(),storeInfoRequest.getStoreId(),advertisementPositionCategory,getRequestResultEnum(tsApiResponseResponseEntity));
            if(tsApiResponseResponseEntity == null)
                throw new BusinessException("获取百度广告失败");

            response = tsApiResponseResponseEntity.getBody();
            if(response !=null){
                logger.info("广告位:"+ partnerAdSlot.getDisplayName()+"AdslotId:"+ tsApiRequest.getSlot().getAdslotId().toStringUtf8()+",storeId:"+storeInfoRequest.getStoreId() +",成功获取百度接口数据"+ response.toString());
            }
            if (response != null && response.getErrorCode() == SUCCESS_CODE && tsApiRequest.getRequestId().equals(response.getRequestId())) {
                partnerAdvertisementDeliveryRecords.addAll(packResultData(response, storeInfoRequest, cooperationPartner.getDuration()));
                break;
            }
        }
        return partnerAdvertisementDeliveryRecords;
    }

    private PartnerAdvertisementRequestResultEnum getRequestResultEnum(ResponseEntity<BaiduRTBInfo.TsApiResponse> response){
        if(response ==null || response.getBody() ==null)
            return PartnerAdvertisementRequestResultEnum.ApiError;
        if(response.getBody().getErrorCode() == SUCCESS_CODE)
            return PartnerAdvertisementRequestResultEnum.GetAd;
        if(response.getBody().getErrorCode() == AD_NO_DATA_CODE)
            return PartnerAdvertisementRequestResultEnum.GetNoAd;
        return PartnerAdvertisementRequestResultEnum.ApiError;
    }

    private List<PartnerAdvertisementDeliveryRecord> packResultData(BaiduRTBInfo.TsApiResponse response, StoreInfoRequest storeInfoRequest, Integer duration) {
        List<PartnerAdvertisementDeliveryRecord> partnerAdvertisementDeliveryRecords = new ArrayList<>();

        for(BaiduRTBInfo.Ad adInfo : response.getAdsList()) {
            PartnerAdvertisementDeliveryRecord record = new PartnerAdvertisementDeliveryRecord();
            record.setThirdPartId(adInfo.getAdKey().toStringUtf8());
            record.setPartnerAdvertisementId(getPartnerAdvertisementId(record.getThirdPartId()));
            record.setStoreId(storeInfoRequest.getStoreId());
            record.setAdvertisementPositionCategory(storeInfoRequest.getAdvertisementPositionCategory());
            StringBuffer monitorUrl = new StringBuffer();
            adInfo.getWinNoticeUrlList().forEach(url ->monitorUrl.append(url.toStringUtf8()).append(","));
            adInfo.getThirdMonitorUrlList().forEach(url -> monitorUrl.append(url.toStringUtf8()).append(","));
            record.setMonitorUrls(monitorUrl.toString().substring(0, monitorUrl.length()-1));
            record.setRequestTime(new Date().getTime());
            record.setEffectiveFinishTime(new DateTime().plusMinutes(duration).toDate().getTime());
            record.setValid(false);
            record.setPartnerId(baiDuApiConfig.getPartnerId());
            record.setDuration(baiDuApiConfig.getDuration());
            record.setRequestId(response.getRequestId().toStringUtf8());

            BaiduRTBInfo.MaterialMeta materialMeta = adInfo.getMaterialMetasList().get(0);
            MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(materialMeta.getMaterialType().getNumber(), PartnerAdMaterialTypeEnum.class).getPartnerMaterialType();
            record.setMaterialType(materialTypeEnum.getValue());
            record.setMaterialSpecification(getMaterialSpecification(materialMeta.getMaterialWidth(), materialMeta.getMaterialHeight()));
            record.setMaterialSize(String.valueOf(materialMeta.getMaterialSize()));
            record.setMd5(materialMeta.getMaterialMd5().toStringUtf8());

            switch (materialTypeEnum) {
                case Img:
                    record.setOriginalUrl(materialMeta.getImageSrcList().get(0).toStringUtf8());
                    break;
                case Video:
                    record.setOriginalUrl(materialMeta.getVideoUrl().toStringUtf8());
                    break;
            }
            partnerAdvertisementDeliveryRecords.add(record);
        }
        return partnerAdvertisementDeliveryRecords;
    }

    private String getMaterialSpecification(int width, int height) {
        StringBuffer sb = new StringBuffer();
        return sb.append(width).append("*").append(height).toString();
    }

    private BaiduRTBInfo.TsApiRequest generateRequestBody(StoreInfoRequest storeInfoRequest) {

        Map<Integer, BaiduRTBInfo.Network.ConnectionType> connectionTypeMap = new HashMap<Integer, BaiduRTBInfo.Network.ConnectionType>(){{
            put(ConnectionTypeEnum.UNKNOWN_NETWORK.getValue(), BaiduRTBInfo.Network.ConnectionType.UNKNOWN_NETWORK);
            put(ConnectionTypeEnum.WIFI.getValue(), BaiduRTBInfo.Network.ConnectionType.WIFI);
            put(ConnectionTypeEnum.MOBILE_2G.getValue(), BaiduRTBInfo.Network.ConnectionType.MOBILE_2G);
            put(ConnectionTypeEnum.MOBILE_3G.getValue(), BaiduRTBInfo.Network.ConnectionType.MOBILE_3G);
            put(ConnectionTypeEnum.MOBILE_4G.getValue(), BaiduRTBInfo.Network.ConnectionType.MOBILE_4G);
            put(ConnectionTypeEnum.ETHERNET.getValue(), BaiduRTBInfo.Network.ConnectionType.ETHERNET);
            put(ConnectionTypeEnum.NEW_TYPE.getValue(), BaiduRTBInfo.Network.ConnectionType.NEW_TYPE);
        }};

        BaiduRTBInfo.TsApiRequest.Builder request = BaiduRTBInfo.TsApiRequest.newBuilder();

        BaiduRTBInfo.Version apiVersion = request.getApiVersion().newBuilderForType()
                .setMajor(6)
                .setMinor(0)
                .setMicro(0)
                .build();

        String[] versionArray = storeInfoRequest.getOsVersion().split("\\.");
        BaiduRTBInfo.Version osVersion = request.getApiVersion().newBuilderForType()
                .setMajor(Integer.valueOf(versionArray[0]))
                .setMinor(Integer.valueOf(versionArray[1]))
                .setMicro(Integer.valueOf(versionArray[2]))
                .build();

        String deviceUdId;

        if(!environmentConfig.isOnline()) {
            deviceUdId = storeInfoRepository.findOne(q -> q.select(qStoreInfo.deviceId).from(qStoreInfo).where(qStoreInfo.id.eq(storeInfoRequest.getStoreId())));
        } else {
            deviceUdId = storeInfoRequest.getStoreNo();
        }

        BaiduRTBInfo.UdId udId = BaiduRTBInfo.UdId.newBuilder()
                .setIdType(BaiduRTBInfo.UdIdType.MEDIA_ID)
                .setId(ByteString.copyFromUtf8(deviceUdId))
                .build();

        BaiduRTBInfo.Size size = BaiduRTBInfo.Size.newBuilder().setHeight(baiDuApiConfig.getScreenSizeHeight()).setWidth(baiDuApiConfig.getScreenSizeWidth()).build();
        BaiduRTBInfo.Device device = request.getDevice().newBuilderForType()
                .setUdid(udId)
                .setModel(ByteString.copyFromUtf8(storeInfoRequest.getModel()))
                .setOsType(BaiduRTBInfo.OsType.ANDROID)
                .setScreenSize(size)
                .setOsVersion(osVersion)
                .setVendor(ByteString.copyFromUtf8(storeInfoRequest.getVendor()))
                .build();

        BaiduRTBInfo.Network network = request.getNetwork().newBuilderForType()
                .setConnectionType(connectionTypeMap.get(storeInfoRequest.getConnectionType()))
                .setIpv4(ByteString.copyFromUtf8(storeInfoRequest.getRequestIp()))
                .setOperatorType(BaiduRTBInfo.Network.OperatorType.ISP_CHINA_MOBILE)
                .build();

        BaiduRTBInfo.SlotInfo slotInfo = request.getSlot().newBuilderForType()
                .setAdslotId(ByteString.copyFromUtf8(advertisementPositionTypeEnumMap.get(PartnerAdSlotEnum.FullScreenImg)))
                .build();

        request.setRequestId(ByteString.copyFromUtf8(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()));
        request.setApiVersion(apiVersion);
        request.setAppId(ByteString.copyFromUtf8(baiDuApiConfig.getAppId()));
        request.setDevice(device);
        request.setNetwork(network);
        request.setSlot(slotInfo);
        return request.build();
    }

    private BaiduRTBInfo.TsApiRequest setSlotInfo(BaiduRTBInfo.TsApiRequest tsApiRequest, PartnerAdSlotEnum partnerAdSlot) {
        BaiduRTBInfo.SlotInfo slotInfo = BaiduRTBInfo.TsApiRequest.newBuilder().getSlot().newBuilderForType()
                .setAdslotId(ByteString.copyFromUtf8(advertisementPositionTypeEnumMap.get(partnerAdSlot)))
                .build();
        return tsApiRequest.toBuilder().setRequestId(ByteString.copyFromUtf8(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())).setSlot(slotInfo).build();
    }
}
