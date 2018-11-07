package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.model.system.DurationUnitEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ContractAdvertisementPositionConfig extends BaseModel {
    /**
     * 广告位置 {@link com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum}
     */
    @Column(nullable = false)
    private Integer advertisementPositionType;

    /**
     * 终端类型 {@link com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum}
     */
    @Column(nullable = false,columnDefinition = "Integer default  1")
    private Integer terminalType;

    @Column(name = "advertisement_size_config_id",nullable = false,updatable = false,length = 36)
    private String advertisementSizeConfigId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_size_config_id",insertable = false,updatable = false)
    private AdvertisementSizeConfig advertisementSizeConfig;

    @Column(length = 36, columnDefinition = "varchar(36) default ''")
    private String advertisementDisplayTimesConfigId;

    @Column(nullable = false, length = 36, columnDefinition = "varchar(36) default ''")
    private String advertisementDurationConfigId;

    /**
     *展示次数
     */
    private Integer  displayTimes;

    /**
     *时间单位
     *
     */
    private Integer timeUnit;

    /**
     *时长
     */
    private Integer duration;

    /**
     *时长单位
     */
    private Integer durationUnit;

    @Column(name = "contract_id",nullable = false,updatable = false,length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private ContractExtension contractExtension;

    /**
     * 合同广告投放位置配置对应的system_param_config表的id
     */
    @Column(nullable = false, length = 5, columnDefinition = "varchar(5) not null default ''")
    private String systemParamAdvertisementPositionId;

    public String getDisplayTimesInfo(){
        return this.getDisplayTimes()+"次/"+ EnumUtils.getDisplayName(this.timeUnit,DisplayTimeUnitEnum.class);
    }

    public String getFullDuration(){
        return this.duration==null?"":(duration+ EnumUtils.getDisplayName(durationUnit,DurationUnitEnum.class));
    }

    public AdvertisementPositionCategoryEnum getAdvertisementPositionCategoryEnum() {
        return AdvertisementPositionCategoryEnum.getCategoryEnumByPositionAndTerminal(advertisementPositionType, terminalType);
    }
}
