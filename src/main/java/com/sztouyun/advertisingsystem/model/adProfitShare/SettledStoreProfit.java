package com.sztouyun.advertisingsystem.model.adProfitShare;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 结算表
 * Created by wenfeng on 2018/1/15.
 */
@Entity
public class SettledStoreProfit extends BaseModel {
    /**
     * 结算金额
     */
    @Column(nullable = false)
    private Double settledAmount=0D;

    /**
     * 结算状态（1：全部、2：待结算、3：已结算、0：待确认）
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer settleStatus=SettledStatusEnum.UnConformed.getValue();


    /**
     * 结算门店数量
     */
    @Column(nullable = false)
    private Integer storeCount=0;

    /**
     * 结算流水数量
     */
    @Column(nullable = false)
    private Integer streamCount;

    /**
     * 结算月份
     */
    @Column
    private Date settledMonth;

    /**
     * 结算时间
     */
    @Column
    private Date settledTime;

    public Double getSettledAmount() {
        return NumberFormatUtil.format(settledAmount);
    }

    public void setSettledAmount(Double settledAmount) {
        if(settledAmount==null){
            this.settledAmount=0D;
        }else{
            this.settledAmount = settledAmount;
        }
    }

    public Integer getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
    }

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }

    public Integer getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(Integer streamCount) {
        this.streamCount = streamCount;
    }


    public void addStoreCount() {
        if(this.storeCount==null){
            this.storeCount =1;
        }else {
            this.storeCount +=1;
        }
    }

    public void addStreamCount() {
        if(this.streamCount==null){
            this.streamCount =1;
        }else{
            this.streamCount += 1;
        }
    }

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }
    public SettledStoreProfit(){
        this.streamCount=0;
    }

    public Date getSettledTime() {
        return settledTime;
    }

    public void setSettledTime(Date settledTime) {
        this.settledTime = settledTime;
    }

}
