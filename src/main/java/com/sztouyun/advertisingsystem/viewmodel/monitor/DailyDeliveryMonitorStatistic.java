package com.sztouyun.advertisingsystem.viewmodel.monitor;


import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class DailyDeliveryMonitorStatistic extends BaseAutoKeyEntity {

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 广告ID
     */
    private String advertisementId;

    /**
     *订单与素材的中间表ID
     */
    private String advertisementMaterialId;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 每日时间
     */
    private Date date;

    /**
     * 每日展示次数
     */
    private Integer displayTimes;

    /**
     * 投放平台
     */
    private Integer terminalType = TerminalTypeEnum.CashRegister.getValue();
    /**
     * 广告位置
     */
    private Integer positionType;

    private Integer advertisementPositionCategory;

}
