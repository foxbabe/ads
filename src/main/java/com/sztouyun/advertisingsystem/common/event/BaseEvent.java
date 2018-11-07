package com.sztouyun.advertisingsystem.common.event;

import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import org.springframework.context.ApplicationEvent;

public class BaseEvent<TEventData> extends ApplicationEvent {
    private final UserViewModel user;

    public BaseEvent(TEventData eventData) {
        super(eventData);
        user = AuthenticationService.getUser();
    }

    public TEventData getEventData(){
        return (TEventData)getSource();
    }

    public UserViewModel getUser() {
        return user;
    }
}
