package com.sztouyun.advertisingsystem.model.monitor;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {
        @Index(name = "index_step_action_date_store",columnList = "material_url_step_id,action,date,store_id",unique = true)
})
public class AdvertisementDailyLinkMonitorStatistic extends BaseAutoKeyEntity{
    /**
     * 广告素材链接步骤ID
     */
    @Column(name = "material_url_step_id",nullable = false, length = 36)
    private String materialUrlStepId;

    /**
     * 监控日期
     */
    private Date date;

    /**
     * 门店ID
     */
    @Column(name = "store_id",updatable = false,length = 36)
    private String storeId;

    /**
     * 步骤对应的操作：MaterialLinkActionEnum
     */
    @Column(nullable = false)
    private Integer action;

    /**
     * 触发次数
     */
    private Integer triggerTimes;

    /**
     * 广告位置: AdvertisementPositionCategoryEnum
     */
    private Integer advertisementPositionCategory;

    /**
     * 步骤类型需要的参数: AdvertisementMaterialUrlStepTypeEnum
     */
    @Column(nullable = false)
    private Integer stepType;

    /**
     * 素材url类型: MaterialLinkTypeEnum
     */
    @Column(nullable = false)
    private Integer linkType;
}
