package com.sztouyun.advertisingsystem.service.message.eventlistener.base;

import com.sztouyun.advertisingsystem.common.event.BaseEvent;
import com.sztouyun.advertisingsystem.model.message.Message;
import com.sztouyun.advertisingsystem.model.message.MessageCategoryEnum;
import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.message.MessageService;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Date;
import java.util.List;

public abstract class BaseMessageEventListener<E extends BaseEvent<TEventData>,TEventData,TMessageViewModel extends MessageViewModel> implements ApplicationListener<E> {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    protected abstract String  getObjectId(TEventData eventData);

    protected abstract MessageTypeEnum getMessageType(TEventData eventData);

    protected abstract MessageCategoryEnum getMessageCategory(TEventData eventData);

    protected abstract TMessageViewModel getMessageViewModel(TEventData eventData);

    protected abstract List<String> getMessageReceiverIds(TEventData eventData);

    protected boolean isCreateMessage(TEventData eventData){
        return true;
    }

    @Override
    @Async
    public void onApplicationEvent(E event) {
        //设置当前用户登录状态
        var user = event.getUser();
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(user, user.getPassword(),user.getAuthorities()));

        TEventData eventData = event.getEventData();
        if(!isCreateMessage(eventData))
            return;
        MessageCategoryEnum messageCategory =getMessageCategory(eventData);
        if(messageCategory == null)
            return;

        Message message = new Message();
        message.setMessageType(getMessageType(eventData).getValue());
        message.setObjectId(getObjectId(eventData));
        message.setMessageCategory(messageCategory.getValue());

        TMessageViewModel messageViewModel = getMessageViewModel(eventData);
        messageViewModel.setId(message.getId());
        messageViewModel.setMessageCategory(message.getMessageCategory());
        messageViewModel.setMessageType(message.getMessageType());
        messageViewModel.setCreatorId(user.getId());
        messageViewModel.setCreatorName(user.getNickname());
        messageViewModel.setCreatedTime(new Date());

        message.setContent(ObjectMapperUtils.toJsonString(messageViewModel));
        List<String> messageReceiverIds =getMessageReceiverIds(eventData);
        messageReceiverIds.add(user.getId());
        //加上用户所有的领导
        messageReceiverIds.addAll(userService.getUserAllLeaderIds(messageReceiverIds));
        //加上所有的系统管理员
        messageReceiverIds.addAll(userService.getAllAdminUserIds());
        messageService.CreateMessage(message, Linq4j.asEnumerable(messageReceiverIds).where(id-> !org.springframework.util.StringUtils.isEmpty(id)).distinct().toList());
    }
}
