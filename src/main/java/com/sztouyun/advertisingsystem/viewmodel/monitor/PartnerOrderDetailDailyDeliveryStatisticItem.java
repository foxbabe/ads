package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by wenfeng on 2018/3/8.
 */
@ApiModel
@Data
public class PartnerOrderDetailDailyDeliveryStatisticItem extends BasePartnerOrderDailyStoreStatistic {
    @ApiModelProperty(value = "展示标准")
    private Integer displayStandard=0;
    @ApiModelProperty(value = "当天预期展示次数")
    private Integer totalDisplayTimes=0;
    @ApiModelProperty(value = "当天实际展示次数")
    private Integer displayTimes=0;
    @ApiModelProperty(value = "当天完成比例")
    private String ratio="0%";
    public void setTotalDisplayTimes(Integer totalDisplayTimes) {
        this.totalDisplayTimes = totalDisplayTimes;
        ratio= NumberFormatUtil.format(displayTimes.longValue(), totalDisplayTimes.longValue(), Constant.RATIO_PATTERN);
    }

    public PartnerOrderDetailDailyDeliveryStatisticItem() {
        super();
    }
    public PartnerOrderDetailDailyDeliveryStatisticItem(Date date) {
        super(date);
    }
}
