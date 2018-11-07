package com.SzTouYun.advertisingsystem;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.AdvertisingSystemApplication;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementDeliveryRecord;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.common.StoreMultiThreadPageTaskService;
import com.sztouyun.advertisingsystem.service.partner.CooperationPartnerService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementDeliveryRecordService;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerAdvertisementDeliveryRecordLog;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2017/7/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdvertisingSystemApplication.class)
public class CustomerTest {
    @Autowired
    private StoreMultiThreadPageTaskService storeMultiThreadTaskService;
    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;

    @Test
    public void save(){
        var list = new ArrayList<PartnerAdvertisementDeliveryRecord>();
        for (int i=0;i<5;i++){
            for (int j=0;j<20;j++){
                var record = new PartnerAdvertisementDeliveryRecord();
                record.setPartnerAdvertisementId("1#1237"+i);
                record.setRequestTime(new Date().getTime());
                record.setStoreId("sss"+j);
                record.setMaterialType(1);
                record.setOriginalUrl("www.baidu.cm"+i);
                record.setMd5("111111"+i);
                record.setEffectiveFinishTime(DateTime.now().plusHours(1).getMillis());
                list.add(record);
            }}
        partnerAdvertisementDeliveryRecordService.savePartnerAdvertisementDeliveryRecord(list);
        List<PartnerAdvertisementDeliveryRecordLog> logs =Linq4j.asEnumerable(list).select(t->{
            var a =new PartnerAdvertisementDeliveryRecordLog();
            a.setRecordId(t.getId());
            a.setCreatedTime(new Date().getTime());
            return a;
        }).toList();
        partnerAdvertisementDeliveryRecordService.savePartnerAdvertisementEndDeliveryRecordLog(logs);
    }

    @Test
    public void  testStoreMultiThreadTaskService(){
        storeMultiThreadTaskService.runPageStoreIdTask(new BooleanBuilder(),storeIds->{
            System.out.println(String.join(",",storeIds));
        },20,500);
    }

    @Autowired
    private CooperationPartnerService cooperationPartnerService;
    @Test
    public void  testCooperationPartnerService(){
        AuthenticationService.setAdminLogin();
        var adSlots = cooperationPartnerService.getPrioritizedAdSlots("11E80017FA002C57AC9EBA5A3BAE059D");
        System.out.println("广告位优先级："+String.join(",", Linq4j.asEnumerable(adSlots).select(a->a.toString())));
        cooperationPartnerService.toggleAdSlotEnabled(1);
        adSlots = cooperationPartnerService.getPrioritizedAdSlots("11E80017FA002C57AC9EBA5A3BAE059D");
        System.out.println("广告位优先级："+String.join(",", Linq4j.asEnumerable(adSlots).select(a->a.toString())));
        cooperationPartnerService.toggleAdSlotEnabled(2);
        cooperationPartnerService.toggleAdSlotEnabled(3);
        cooperationPartnerService.toggleAdSlotEnabled(4);
        adSlots = cooperationPartnerService.getPrioritizedAdSlots("11E80017FA002C57AC9EBA5A3BAE059D");
        System.out.println("广告位优先级："+String.join(",", Linq4j.asEnumerable(adSlots).select(a->a.toString())));
    }

}