package com.sztouyun.advertisingsystem.service;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.openapi.notification.OpenApiNotification;
import com.sztouyun.advertisingsystem.service.openapi.notification.data.base.OpenApiNotificationData;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BaseService {
    @Autowired
    private ApplicationContext applicationContext;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final UserViewModel getUser(){
        return AuthenticationService.getUser();
    }

    protected <T> Page<T> pageResult(List<T> resultList, Pageable pageable,long total){
        return  new PageImpl<>(resultList, pageable, total);
    }

    public final <E extends BaseEvent> void publishEvent(E event) {
        applicationContext.publishEvent(event);
    }

    protected final <TNotificationData extends OpenApiNotificationData> void publishOpenApiNotification(TNotificationData notificationData) {
        applicationContext.publishEvent(new OpenApiNotification<>(notificationData));
    }
}
