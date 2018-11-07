package com.sztouyun.advertisingsystem.service.task.event.data;

import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/4/8.
 */
@Data
public class BaseTaskEventData {
    private String id;

    private String objectId;

    private String subObjectId="";

    private int taskCategory;

    private String name;

    private String code;

    private Date createdTime;

    private Integer expectedCompletionTime;

    private Integer expectedCompletionUnit;

    private String remark;

    public BaseTaskEventData(){}

    public BaseTaskEventData(String id,String objectId, String subObjectId, int taskCategory) {
        this.id=id;
        this.objectId = objectId;
        this.subObjectId = subObjectId;
        this.taskCategory = taskCategory;
    }
}
