package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/7/25.
 */
@Data
@ApiModel
public class RequestStoreRankInfo extends RequestStoreRankBaseInfo {
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "请求门店数量")
    private Integer requestStoreCount;
    @ApiModelProperty(value = "请求门店占比")
    private String requestStoreRatio;
    @ApiModelProperty(value = "未请求门店数量")
    private Integer unRequestStoreCount;
    @ApiModelProperty(value = "未请求门店占比")
    private String unRequestStoreRatio;
    @ApiModelProperty(value = "请求次数")
    private Long requestTimes;
    @ApiModelProperty(value = "展示次数")
    private Long displayTimes;
    @ApiModelProperty(value = "有效次数")
    private Long validTimes;
    @ApiModelProperty(value = "有效占比")
    private String validRatio;

    public Integer getUnRequestStoreCount() {
        return configStoreCount.equals(0L)?0:(configStoreCount.intValue()-requestStoreCount);
    }

    public String getRequestStoreRatio() {
        return requestStoreCount>0?NumberFormatUtil.format(requestStoreCount.longValue(), configStoreCount, Constant.RATIO_PATTERN):Constant.ZERO_PERCENT;
    }

    public String getUnRequestStoreRatio() {
        return getUnRequestStoreCount()>0?NumberFormatUtil.format(getUnRequestStoreCount().longValue(), configStoreCount, Constant.RATIO_PATTERN):Constant.ZERO_PERCENT;
    }

    public String getValidRatio() {
        return validTimes>0?NumberFormatUtil.format(validTimes.longValue(), displayTimes.longValue(), Constant.RATIO_PATTERN):Constant.ZERO_PERCENT;
    }

    @JsonIgnore
    public Double getDoubleValidRatio(){
        return validTimes>0?((double) validTimes)/displayTimes:0;
    }
}
