package com.sztouyun.advertisingsystem.service.message;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.message.Message;
import com.sztouyun.advertisingsystem.model.message.MessageCategoryEnum;
import com.sztouyun.advertisingsystem.model.message.MessageReceiver;
import com.sztouyun.advertisingsystem.model.message.QMessageReceiver;
import com.sztouyun.advertisingsystem.model.mongodb.MessageInfo;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.message.MessageReceiverRepository;
import com.sztouyun.advertisingsystem.repository.message.MessageRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.ObjectMapperUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.message.MessagePageInfoViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MessageService extends BaseService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageReceiverRepository messageReceiverRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private final QMessageReceiver qMessageReceiver = QMessageReceiver.messageReceiver;
    private final QUser qUser = QUser.user;

    @Transactional
    public void CreateMessage(Message message, List<String> messageReceiverIds){
        messageRepository.save(message);
        mongoTemplate.insert(new MessageInfo(message.getId(),message.getContent()));
        messageReceiverRepository.save(Linq4j.asEnumerable(messageReceiverIds).select(receiverId->new MessageReceiver(message.getId(),receiverId)).toList());
    }

    public MessageViewModel getMessageViewModel(Message message){
        MessageCategoryEnum messageCategoryEnum = EnumUtils.toEnum(message.getMessageCategory(),MessageCategoryEnum.class);
        Query query=new Query(Criteria.where("messageId").is(message.getId()));
        MessageInfo messageInfo= mongoTemplate.findOne(query,MessageInfo.class);
        if(messageInfo == null)
            return new MessageViewModel();
        return ObjectMapperUtils.jsonToObject(messageInfo.getContent(),messageCategoryEnum.getContentClass());
    }


    public Page<MessageReceiver> getMessageList(MessagePageInfoViewModel queryRequest) {
        Pageable pageable = new MyPageRequest(queryRequest.getPageIndex(), queryRequest.getPageSize(), new QSort(qMessageReceiver.message.createdTime.desc()));
        return messageReceiverRepository.findAll(getMessageCommonBuilder(queryRequest), pageable, new JoinDescriptor().innerJoin(qMessageReceiver.message));
    }

    public Boolean hasUnReadMessage(MessagePageInfoViewModel queryRequest) {
        BooleanBuilder predicate = getMessageCommonBuilder(queryRequest).and(qMessageReceiver.hasRead.isFalse());
        return messageReceiverRepository.exists(predicate, new JoinDescriptor().innerJoin(qMessageReceiver.message));
    }

    private BooleanBuilder getMessageCommonBuilder(MessagePageInfoViewModel queryRequest) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qMessageReceiver.receiverId.eq(getUser().getId()));
        if(!StringUtils.isEmpty(queryRequest.getOperatorName())) {
            List<String> userIds = userRepository.findAll(q -> q.select(qUser.id).from(qUser).where(qUser.nickname.contains(queryRequest.getOperatorName())));
            predicate.and(qMessageReceiver.message.creatorId.in(userIds));
        }
        if(queryRequest.getMessageType() != null) {
            predicate.and(qMessageReceiver.message.messageType.eq(queryRequest.getMessageType()));
        }
        if(queryRequest.getHasRead() != null) {
            predicate.and(qMessageReceiver.hasRead.eq(queryRequest.getHasRead()));
        }
        if (queryRequest.getStartTime() != null) {
            predicate.and(qMessageReceiver.message.createdTime.after(queryRequest.getStartTime()));
        }
        if (queryRequest.getEndTime() != null) {
            predicate.and(qMessageReceiver.message.createdTime.before(queryRequest.getEndTime()));
        }
        return predicate;
    }

    public MessageStatisticViewModel getMessageStatistic() {
        NumberExpression<Integer> unReadCaseWhen = new CaseBuilder().when(qMessageReceiver.hasRead.isFalse()).then(1).otherwise(0);
        return messageReceiverRepository.findOne(q -> q.select(Projections.bean(MessageStatisticViewModel.class,
                qMessageReceiver.id.count().as("totalMessageCount"),
                unReadCaseWhen.sum().as("unReadCount")
                ))
                .from(qMessageReceiver)
                .where(qMessageReceiver.receiverId.eq(getUser().getId()))
        );
    }

    @Transactional
    public void readMessage(String messageId ){
        messageReceiverRepository.updateMessageByUserIdAndMessageId(getUser().getId(),messageId);
    }

    @Transactional
    public void readMessages(MessagePageInfoViewModel queryRequest) {
        queryRequest.setHasRead(false);
        BooleanBuilder predicate=getMessageCommonBuilder(queryRequest);
        List<String> messageIds=messageReceiverRepository.findAll(q->q.select(qMessageReceiver.messageId).from(qMessageReceiver).innerJoin(qMessageReceiver.message).where(predicate));
        if(messageIds!=null && !messageIds.isEmpty()){
            messageReceiverRepository.updateMessages(getUser().getId(),messageIds);
        }
    }

}
