package com.sztouyun.advertisingsystem.mapper;


import com.sztouyun.advertisingsystem.viewmodel.internal.task.CompleteTaskRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentMapper {
    void  saveTaskAttachments(CompleteTaskRequest request);
}
