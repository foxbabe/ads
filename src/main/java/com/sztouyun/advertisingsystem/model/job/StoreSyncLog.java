package com.sztouyun.advertisingsystem.model.job;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 门店信息同步记录表
 */
@Entity
public class StoreSyncLog {
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private Integer recordCount;
    @Column
    private Integer pageNum;
    @Column
    private boolean successed;
    @Column
    private Integer storeSource;
    @Column
    private String jobName;

    public Integer getStoreSource() {
        return storeSource;
    }

    public void setStoreSource(Integer storeSource) {
        this.storeSource = storeSource;
    }

    @Column(nullable = false,updatable = false)
    @CreatedDate


    private Date createdTime = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public boolean isSuccessed() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}