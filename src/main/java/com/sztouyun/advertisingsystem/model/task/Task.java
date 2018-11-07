package com.sztouyun.advertisingsystem.model.task;

import com.sztouyun.advertisingsystem.model.BaseModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_object_id",columnList = "object_id"),
        @Index(name = "index_sub_object_id",columnList = "sub_object_id")})
public class Task extends BaseModel {
    @Column(length=500)
    private String name;

    @Column
    private String code;

    @Column(nullable = false,length = 36,name = "object_id")
    private String objectId;

    @Column(nullable = false,length = 36,name = "sub_object_id")
    private String subObjectId="";

    @Column
    private int taskType;

    @Column
    private int taskSubType;

    @Column
    private int taskCategory;

    @Column
    private int priority;

    @Column(nullable = false)
    private Integer taskStatus=TaskStatusEnum.PendingDistribute.getValue();

    @Column
    private Integer taskResult;

    @Column
    private Date beginTime;

    @Column
    private Date endTime;

    @Column
    private Integer expectedCompletionTime;

    @Column
    private Integer expectedCompletionUnit;

    @Column
    private String ownerName ="";

    @Column
    private String ownerPhone="";

    @Column(length=1000)
    private String remark="";

    @Column(nullable = false,updatable = false)
    private Date createdTime;
}
