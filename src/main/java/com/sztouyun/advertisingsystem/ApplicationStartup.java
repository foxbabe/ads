package com.sztouyun.advertisingsystem;

import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by wenfeng on 2017/8/28.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ResourceDownloadThread resourceDownloadThread;
    @Autowired
    private PartnerAdvertisementRequestLogService partnerAdvertisementRequestLogService;

    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        Thread thread = new Thread(resourceDownloadThread);
        thread.start();
        if(partnerAdvertisementRequestLogService !=null){
            new Thread(()->partnerAdvertisementRequestLogService.savePartnerRequestLog()).start();
        }
    }
}