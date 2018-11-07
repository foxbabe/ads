package com.sztouyun.advertisingsystem.mockapi;

import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.AdTrack;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.RTBPlanInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.RTBRequest;
import com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb.Response;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value = "OOHLinkRTB广告模拟")
@RestController
@Profile({"local","dev","test","stage"})
@RequestMapping("/mock/api/OOHLinkRtb")
public class MockOOHLinkRtbApi {

    @PostMapping(value = "/advertisements")
    public Response advertisements(@RequestParam String rtbRequest) {
        RTBRequest request = ObjectMapperUtils.jsonToObject(rtbRequest, RTBRequest.class);
        return handleRTBRequest(request,false);
    }

    @PostMapping(value = "/advertisements1")
    public Response uniqueAdvertisements(@RequestParam String rtbRequest) {
        RTBRequest request = ObjectMapperUtils.jsonToObject(rtbRequest, RTBRequest.class);
        return handleRTBRequest(request,true);
    }

    private Response handleRTBRequest(RTBRequest request,boolean uniqueAd) {
        Response response = new Response();

        response.setCode(0);
        response.setMessage("SUCCESS");


        RTBPlanInfo rtbPlanInfo = new RTBPlanInfo();

        int random = (int) (Math.random() * 4);
        if (random == 0) {
            rtbPlanInfo.setErrorCode((short)1);
            response.setData(rtbPlanInfo);
            return response;
        } else if (random == 1) {
            rtbPlanInfo.setErrorCode((short)202);
            response.setData(rtbPlanInfo);
            return response;
        }

        rtbPlanInfo.setRequestId(request.getRequestId());
        rtbPlanInfo.setPositionId(11111111L);//未知
        rtbPlanInfo.setChannelId((short) 1234);//由线下分配
        rtbPlanInfo.setMatWidth(1024);
        rtbPlanInfo.setMatHeight(768);
        rtbPlanInfo.setMatMd5("934b9b5409e01330941ae53b8ed6be1d");
        rtbPlanInfo.setDuration(15);
        rtbPlanInfo.setExpTime(3600);
        rtbPlanInfo.setWinNoticeUrlList(new ArrayList<String>(){{add("http://www.huahuahua.com");}});
        rtbPlanInfo.setErrorCode((short) 0);
        rtbPlanInfo.setClickUrl("http://www.google.com");

        rtbPlanInfo.setPlanId(Long.valueOf(createAdId(uniqueAd)));//广告ID

        switch (request.getAdSlot().getType()) {
            case 1:
            default:
                rtbPlanInfo.setMatUrl(getMaterialUrl(String.valueOf(rtbPlanInfo.getPlanId()),uniqueAd));
                rtbPlanInfo.setMatType((short) 1);
                rtbPlanInfo.setFileName("广告图片001.jpg");
                break;
            case 2:
                rtbPlanInfo.setMatType((short) 2);
                rtbPlanInfo.setMatUrl("http://storage.storify.cc/shop-ad/2018/6/25/F1539CB3785211E8A5D80017FA00455B.mp4");
                rtbPlanInfo.setFileName("广告视频001.mp4");
                break;
        }

        AdTrack adTrackBegin = new AdTrack();
        adTrackBegin.setType((short) 0);
        adTrackBegin.setTrackList(new ArrayList<String>(){{add("http://www.google.com");}});

        AdTrack adTrackOver = new AdTrack();
        adTrackOver.setType((short) 1);
        adTrackOver.setTrackList(new ArrayList<String>(){{add("http://www.google.com1122");}});
        rtbPlanInfo.setAdTrackList(new ArrayList<AdTrack>(){{add(adTrackBegin); add(adTrackOver);}});
        response.setData(rtbPlanInfo);
        return response;
    }
    private String createAdId(boolean uniqueAd) {
        if(uniqueAd){
            Long id =(new Date().getTime()/1000)%86400;
            return id.toString();
        }
        int key = (int) (Math.random() * 9.0 + 1);
        return adMap.get(key);
    }

    private String  getMaterialUrl(String adKey,boolean uniqueAd){
        if(uniqueAd)
            return "http://139.219.194.186:8080/image/"+"OOHLink "+adKey+".jpg";

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
    }};

    Map<String, String> materialUrlMap = new HashMap<String, String>(){{
        put("11111", "http://storage-test.storify.cc/shop-ad/2018/6/25/92546F1B784A11E8B0640017FA002C57.png");
        put("22222", "http://storage-test.storify.cc/shop-ad/2018/6/25/9AD84DFC784A11E8B0640017FA002C57.png");
        put("33333", "http://storage-test.storify.cc/shop-ad/2018/6/25/A0B9205D784A11E8B0640017FA002C57.png");
        put("44444", "http://storage-test.storify.cc/shop-ad/2018/6/25/A69CD8EE784A11E8B0640017FA002C57.png");
        put("55555", "http://storage-test.storify.cc/shop-ad/2018/6/25/AD316AF0784A11E8B0640017FA002C57.png");
        put("66666", "http://storage-test.storify.cc/shop-ad/2018/6/25/B287D7A4784A11E8B0640017FA002C57.png");
        put("77777", "http://storage-test.storify.cc/shop-ad/2018/6/25/B7BF4AA0784A11E8B0640017FA002C57.png");
        put("88888", "http://storage-test.storify.cc/shop-ad/2018/6/25/BE2504B9784A11E8B0640017FA002C57.png");
        put("99999", "http://storage-test.storify.cc/shop-ad/2018/6/25/C3534F29784A11E8B0640017FA002C57.png");
    }};
}
