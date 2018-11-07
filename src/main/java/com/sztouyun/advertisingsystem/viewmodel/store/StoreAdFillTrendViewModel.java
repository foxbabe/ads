package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class StoreAdFillTrendViewModel {
    @ApiModelProperty("时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date date;

    @ApiModelProperty("门店总数")
    private long storeTotal;

    @ApiModelProperty("满刊门店数量")
    private long fullStoreNum;

    @ApiModelProperty("已用广告位为0的门店数量")
    private long usedAdPositionEqualZeroStoreNum;

    @ApiModelProperty("已用广告位50%以下的门店数量")
    private long usedAdPositionLessThanFiftyPercentStoreNum;

    @ApiModelProperty("已用广告位50%以上的门店数量")
    private long usedAdPositionGreaterThanEqualFiftyPercentStoreNum;

    @ApiModelProperty("满刊门店占比")
    public String getFullStoreProportion() {
        return calcProportion(fullStoreNum, storeTotal);
    }

    @ApiModelProperty("未满刊门店数量")
    public long getUnFullStoreNum() {
        return storeTotal - fullStoreNum;
    }

    @ApiModelProperty("未满刊门店占比")
    public String getUnFullStoreProportion() {
        return calcProportion(getUnFullStoreNum(), storeTotal);
    }

    @ApiModelProperty("已用广告位为0的门店占比")
    public String getUsedAdPositionEqualZeroStoreProportion() {
        return calcProportion(usedAdPositionEqualZeroStoreNum, storeTotal);
    }

    @ApiModelProperty("已用广告位50%以下的门店占比")
    public String getUsedAdPositionLessThanFiftyPercentStoreProportion() {
        return calcProportion(usedAdPositionLessThanFiftyPercentStoreNum, storeTotal);
    }

    @ApiModelProperty("已用广告位50%以上的门店占比")
    public String getUsedAdPositionGreaterThanEqualFiftyPercentStoreProportion() {
        return calcProportion(usedAdPositionGreaterThanEqualFiftyPercentStoreNum, storeTotal);
    }

    /**
     * 计算占比
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return
     */
    private String calcProportion(Long numerator, Long denominator) {
        return NumberFormatUtil.format(numerator, denominator, Constant.RATIO_PATTERN);
    }
}
