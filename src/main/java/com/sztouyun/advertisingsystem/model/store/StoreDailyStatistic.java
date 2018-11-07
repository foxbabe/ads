package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name="index_store_id_date", columnList = "store_id,date", unique = true)
})
@ToString
public class StoreDailyStatistic extends BaseAutoKeyEntity {
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "store_id",nullable = false, length = 36)
    private String storeId;

    @Column
    private Long openingTimeBegin = 0L;

    @Column
    private Long openingTimeEnd = 0L;

    @Column
    private Integer orderCount = 0;

    /**
     * 门店开机时长, 单位: 毫秒
     */
    @Column(nullable = false,columnDefinition="bigint default 0")
    private Long openingTimeDuration = 0L;

    @Column
    private Boolean available = Boolean.FALSE;

    /**
     * 总广告位数量
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer totalAdPositionCount = 0;

    /**
     * 已使用广告位数量
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer usedAdPositionCount = 0;
}
