package com.sztouyun.advertisingsystem.model.adProfitShare;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class StoreProfitStatisticExtension {

    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "storeProfitStatistic"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;


    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @PrimaryKeyJoinColumn
    private StoreProfitStatistic storeProfitStatistic;

    /**
     * 开机标准值
     */
    @Column
    private Double openingTimeStandard;

    /**
     * 开机时长
     */
    @Column(nullable = false, length = 128)
    private Double openingTime;

    /**
     * 开机时长比较类型
     */
    @Column
    private Integer openingTimeComparisonType;

    /**
     * 开机时长单位
     */
    @Column
    private Integer openingTimeUnit;

    /**
     * 开机时间点
     */
    @Column
    private Date openingTimeBegin;

    /**
     * 关机时间点
     */
    @Column
    private Date openingTimeEnd;

    /**
     *订单标准值
     */
    @Column
    private Double orderStandard;

    /**
     * 当天月平均订单数
     */
    @Column
    private Double orderNum;

    /**
     * 订单数量比较类型
     */
    @Column
    private Integer orderComparisonType;

    /**
     * 订单数量单位
     */
    @Column
    private Integer orderUnit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StoreProfitStatistic getStoreProfitStatistic() {
        return storeProfitStatistic;
    }

    public void setStoreProfitStatistic(StoreProfitStatistic storeProfitStatistic) {
        this.storeProfitStatistic = storeProfitStatistic;
    }

    public Double getOpeningTimeStandard() {
        return openingTimeStandard;
    }

    public void setOpeningTimeStandard(Double openingTimeStandard) {
        this.openingTimeStandard = openingTimeStandard;
    }

    public Double getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Double openingTime) {
        this.openingTime = openingTime;
    }

    public Integer getOpeningTimeComparisonType() {
        return openingTimeComparisonType;
    }

    public void setOpeningTimeComparisonType(Integer openingTimeComparisonType) {
        this.openingTimeComparisonType = openingTimeComparisonType;
    }

    public Integer getOpeningTimeUnit() {
        return openingTimeUnit;
    }

    public void setOpeningTimeUnit(Integer openingTimeUnit) {
        this.openingTimeUnit = openingTimeUnit;
    }

    public Date getOpeningTimeBegin() {
        return openingTimeBegin;
    }

    public void setOpeningTimeBegin(Date openingTimeBegin) {
        this.openingTimeBegin = openingTimeBegin;
    }

    public Date getOpeningTimeEnd() {
        return openingTimeEnd;
    }

    public void setOpeningTimeEnd(Date openingTimeEnd) {
        this.openingTimeEnd = openingTimeEnd;
    }

    public Double getOrderStandard() {
        return orderStandard;
    }

    public void setOrderStandard(Double orderStandard) {
        this.orderStandard = orderStandard;
    }

    public Double getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Double orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderComparisonType() {
        return orderComparisonType;
    }

    public void setOrderComparisonType(Integer orderComparisonType) {
        this.orderComparisonType = orderComparisonType;
    }

    public Integer getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(Integer orderUnit) {
        this.orderUnit = orderUnit;
    }
}
