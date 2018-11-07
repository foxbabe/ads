package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AdvertisementStoreCountViewModel{

    @ApiModelProperty(value = "已激活门店数量")
    private Integer activatedCount = 0;

    @ApiModelProperty(value = "未激活门店数量")
    private Integer unActivatedCount = 0;

    @ApiModelProperty(value = "可用门店数量")
    private Long availableCount=0L;

    @ApiModelProperty(value = "不可用门店数量")
    private Long unavailableCount=0L;

    @ApiModelProperty(value = "总门店数量")
    private Integer totalStoreCount = 0;

    @ApiModelProperty(value = "激活比例")
    private String activeRatio;

    @ApiModelProperty(value = "可用比例")
    private String availableRatio;

    public String getActiveRatio() {
        return NumberFormatUtil.format(activatedCount.longValue(), totalStoreCount.longValue(), Constant.RATIO_PATTERN);
    }

    public String getAvailableRatio() {
        return NumberFormatUtil.format(availableCount.longValue(), totalStoreCount.longValue(), Constant.RATIO_PATTERN);
    }

    public Integer getUnActivatedCount() {
        return totalStoreCount - activatedCount;
    }

    public Long getUnavailableCount() {
        return totalStoreCount - availableCount;
    }
}
