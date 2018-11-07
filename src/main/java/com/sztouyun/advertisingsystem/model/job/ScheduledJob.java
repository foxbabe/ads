package com.sztouyun.advertisingsystem.model.job;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by wenfeng on 2017/9/13.
 */
@Entity
public class ScheduledJob {
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    @Column(nullable = false,length = 128)
    private String jobName;

    @Column(nullable = false)
    private Integer taskSize=0;

    @Column(nullable = false)
    private Integer failedTaskSize=0;

    @Column(nullable = false)
    private Boolean isSuccessed=true;

    @Column
    private Date createdTime;

    @Column(length = 512)
    private String remark;

    @Column
    private Integer pageNum;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public ScheduledJob(){

    }
    public ScheduledJob(String jobName){
        this.jobName=jobName;
        this.createdTime=new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Boolean getSuccessed() {
        return isSuccessed;
    }

    public void setSuccessed(Boolean successed) {
        isSuccessed = successed;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }

    public Integer getFailedTaskSize() {
        return failedTaskSize;
    }

    public void setFailedTaskSize(Integer failedTaskSize) {
        this.failedTaskSize = failedTaskSize;
    }

    public void updateSuccessed(){
        if(this.failedTaskSize>0){
            this.isSuccessed=false;
        }else{
            this.remark= Constant.SUCCESS_REMARK;
            this.isSuccessed=true;
        }
    }
}
