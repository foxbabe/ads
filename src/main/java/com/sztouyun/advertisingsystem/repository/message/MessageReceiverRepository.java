package com.sztouyun.advertisingsystem.repository.message;

import com.sztouyun.advertisingsystem.model.message.MessageReceiver;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageReceiverRepository extends BaseRepository<MessageReceiver> {


    @Modifying
    @Query(value = "UPDATE MessageReceiver set hasRead = true WHERE receiverId = :userId and messageId = :messageId")
    void updateMessageByUserIdAndMessageId(@Param("userId") String userId,@Param("messageId") String messageId);

    @Modifying
    @Query(value = "UPDATE MessageReceiver set hasRead = true WHERE receiverId = :userId and messageId in (:list) and hasRead = false")
    void updateMessages(@Param("userId") String userId,@Param("list") List<String> list);


}
