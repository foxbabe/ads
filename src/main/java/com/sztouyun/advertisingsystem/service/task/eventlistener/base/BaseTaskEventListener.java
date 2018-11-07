package com.sztouyun.advertisingsystem.service.task.eventlistener.base;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.service.task.TaskService;
import com.sztouyun.advertisingsystem.service.task.event.data.BaseTaskEventData;
import com.sztouyun.advertisingsystem.viewmodel.task.TaskViewModel;
import lombok.experimental.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public abstract class BaseTaskEventListener<E extends BaseEvent<TEventData>,TEventData extends BaseTaskEventData,TTaskViewModel extends TaskViewModel> implements ApplicationListener<E> {
    @Autowired
    private TaskService taskService;

    protected  abstract TTaskViewModel getTaskViewModel(TEventData eventData);

    @Override
    @Async
    public void onApplicationEvent(E event) {
        var user = event.getUser();
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(user, user.getPassword(),user.getAuthorities()));
        TEventData eventData = event.getEventData();
        TTaskViewModel taskViewModel = getTaskViewModel(eventData);
        BeanUtils.copyProperties(taskViewModel,eventData);
        taskViewModel.setId(eventData.getId());
        taskService.saveTaskViewModel(taskViewModel);
    }
}
