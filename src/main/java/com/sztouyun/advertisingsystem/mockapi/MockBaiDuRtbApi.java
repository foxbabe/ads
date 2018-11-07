package com.sztouyun.advertisingsystem.mockapi;

import com.google.protobuf.ByteString;
import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.viewmodel.partner.baiduRTB.BaiduRTBInfo;
import io.swagger.annotations.Api;
import lombok.experimental.var;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Api(value = "RTB广告模拟")
@RestController
@Profile({"local","dev","test","stage"})
@RequestMapping("/mock/api/rtb")
public class MockBaiDuRtbApi extends BaseApiController {

    @PostMapping(produces = "application/x-protobuf", value = "/advertisements")
    public BaiduRTBInfo.TsApiResponse advertisements(@RequestBody BaiduRTBInfo.TsApiRequest tsApiRequest) {
        return createResponse(tsApiRequest.getRequestId(), tsApiRequest.getSlot().getAdslotId(),false);
    }

    @PostMapping(produces = "application/x-protobuf", value = "/advertisements1")
    public BaiduRTBInfo.TsApiResponse uniqueAdvertisements(@RequestBody BaiduRTBInfo.TsApiRequest tsApiRequest) {
        return createResponse(tsApiRequest.getRequestId(), tsApiRequest.getSlot().getAdslotId(),true);
    }

    private BaiduRTBInfo.TsApiResponse createResponse(ByteString requestId, ByteString adsSlotId,boolean uniqueAd) {
        BaiduRTBInfo.TsApiResponse.Builder responseBuilder = BaiduRTBInfo.TsApiResponse.newBuilder();
        responseBuilder.setRequestId(requestId);

        int random = (int) (Math.random() * 3);
        if (random == 0) {
            responseBuilder.setErrorCode(201000);
            return responseBuilder.build();
        } else if (random == 1) {
            responseBuilder.setErrorCode(-999);
            return responseBuilder.build();
        }

        responseBuilder.setErrorCode(0);
        responseBuilder.setAdslotId(adsSlotId);
        responseBuilder.setExpirationTime(1527226096);
        responseBuilder.setSearchKey(ByteString.copyFromUtf8("16fbc7836dc9c4a1"));
        responseBuilder.setJpAdtext(ByteString.copyFromUtf8("http://cpro.baidustatic.com/cpro/ui/noexpire/img/newBDlogo/wap_ads_2x.png"));
        responseBuilder.setJpAdlogo(ByteString.copyFromUtf8("http://cpro.baidustatic.com/cpro/ui/noexpire/img/newBDlogo/wap_hand_2x.png"));

        BaiduRTBInfo.Ad.Builder adBuilder = BaiduRTBInfo.Ad.newBuilder();
        adBuilder.addWinNoticeUrl(ByteString.copyFromUtf8("http://jpaccess.baidu.com/win_third?app_id=ea582656&adslot_id=5827925&type=win&search_key=win_16fbc7836dc9c4a1"));
        adBuilder.addThirdMonitorUrl(ByteString.copyFromUtf8("http://jpaccess.baidu.com/win_third?app_id=ea582656&adslot_id=5827925&type=third&search_key=third_16fbc7836dc9c4a1"));

        BaiduRTBInfo.MaterialMeta.Builder materialBuilder = BaiduRTBInfo.MaterialMeta.newBuilder();
        materialBuilder.setMaterialHeight(768);
        materialBuilder.setMaterialWidth(1024);
        var adKey = createAdId(uniqueAd);
        adBuilder.setAdKey(ByteString.copyFromUtf8(adKey));

        String adsSlotIdStr = adsSlotId.toStringUtf8();
        switch (adsSlotIdStr) {
            case "5827924":
            case "5827925":
                materialBuilder.setMaterialType(BaiduRTBInfo.MaterialType.IMAGE);
                materialBuilder.addImageSrc(ByteString.copyFromUtf8(getMaterialUrl(adKey,uniqueAd)));
                materialBuilder.setMaterialSize(7692);
                materialBuilder.setMaterialMd5(ByteString.copyFromUtf8("129acaaf68768d1754da415446bb4bd8"));
                break;

            case "5594583":
            case "5827917":
                materialBuilder.setMaterialType(BaiduRTBInfo.MaterialType.VIDEO);
                materialBuilder.setVideoUrl(ByteString.copyFromUtf8("http://storage.storify.cc/shop-ad/2018/6/19/11E80017FA00455BAB71FF86C6B77384.mp4"));
                materialBuilder.setMaterialSize(2928);
                materialBuilder.setMaterialMd5(ByteString.copyFromUtf8("934b9b5409e01330941ae53b8ed6be1d"));
                break;
        }
        adBuilder.addMaterialMetas(materialBuilder);
        adBuilder.setAdKey(ByteString.copyFromUtf8(adKey));
        responseBuilder.addAds(adBuilder.build());
        return responseBuilder.build();
    }

    private String createAdId(boolean uniqueAd) {
        if(uniqueAd){
            return DateTime.now().toString("HH:mm:ss");
        }
        int key = (int) (Math.random() * 19.0 + 1);
        return adMap.get(key);
    }

    private String  getMaterialUrl(String adKey,boolean uniqueAd){
        if(uniqueAd)
            return "http://139.219.194.186:8080/image/"+"BaiDu "+adKey+".jpg";

        return materialUrlMap.get(adKey);
    }

    Map<Integer, String> adMap = new HashMap<Integer, String>(){{
        put(1, "11111");
        put(2, "22222");
        put(3, "33333");
        put(4, "44444");
        put(5, "55555");
        put(6, "66666");
        put(7, "77777");
        put(8, "88888");
        put(9, "99999");
        put(10, "1010");
        put(11, "1111");
        put(12, "1212");
        put(13, "1313");
        put(14, "1414");
        put(15, "1515");
        put(16, "1616");
        put(17, "1717");
        put(18, "1818");
        put(19, "1919");
    }};

    Map<String, String> materialUrlMap = new HashMap<String, String>(){{
        put("11111", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEC0CA38395A7D.png");
        put("22222", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEBB587B585A7D.png");
        put("33333", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEB5811ED75A7D.png");
        put("44444", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEAF481A565A7D.png");
        put("55555", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEA82579915A7D.png");
        put("66666", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DEA035281A5A7D.png");
        put("77777", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DE7E0D697F5A7D.png");
        put("88888", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DE786E33E95A7D.png");
        put("99999", "http://storage-test.storify.cc/shop-ad/2018/5/18/11E80017FA002C5791DE717E14625A7D.png");
        put("1010", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0D9FD664E55CAF.png");
        put("1111", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DBE5BA7845CAF.png");
        put("1212", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DC8C0C9985CAF.png");
        put("1313", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DD23D876A5CAF.png");
        put("1414", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DD88028DB5CAF.png");
        put("1515", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DDEE2FC7C5CAF.png");
        put("1616", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DE5B09FDD5CAF.png");
        put("1717", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DEBCBBC305CAF.png");
        put("1818", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DF1691CF15CAF.png");
        put("1919", "http://storage-test.storify.cc/shop-ad/2018/5/21/11E80242BE8638308A0DF68F78F25CAF.png");
    }};


}
