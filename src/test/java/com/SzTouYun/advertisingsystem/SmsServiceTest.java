package com.SzTouYun.advertisingsystem;

import com.sztouyun.advertisingsystem.AdvertisingSystemApplication;
import com.sztouyun.advertisingsystem.common.sms.ISmsService;
import com.sztouyun.advertisingsystem.common.sms.SmsMessage;
import com.sztouyun.advertisingsystem.common.verificationCode.IVerificationCodeGenerator;
import com.sztouyun.advertisingsystem.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenfeng on 2017/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdvertisingSystemApplication.class)
public class SmsServiceTest {
    @Autowired
    private IVerificationCodeGenerator codeGenerator;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private OrderService orderService;

    @Value("${verification.code.template.id}")
    private String templateId;

    @Test
    public void smsMessageTest(){
        String code =codeGenerator.generate(6);
        String mobile = "15013429775";
        Map<String, Object> templateParams =new HashMap<>();
        templateParams.put("code",code);
        smsService.sendMessage(new SmsMessage(templateId,templateParams,mobile));
    }

    @Test
    public void orderTest(){
        List<String> storeIds = new ArrayList<>();
        storeIds.add("111");
        storeIds.add("1111");
        storeIds.add("1111");
    //    List<StoreDailyUsage> result = orderService.getUnavailableStoreDailyUsage
      //          (AdvertisementPositionCategoryEnum.AndroidAppBanner.getValue(),LocalDate.now().toDate(),LocalDate.now().toDate(),storeIds);

     }
}
