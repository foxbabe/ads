package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by szty on 2018/7/26.
 */
@Data
@ApiModel
public class PartnerAdvertisementRequestInfo implements Serializable {
    @ApiModelProperty(value = "合作方广告ID")
    private String partnerAdvertisementId;
    @ApiModelProperty(value = "三方方广告ID")
    private String thirdPartId;
    @ApiModelProperty(value = "请求次数")
    private Long requestTimes;
    @ApiModelProperty(value = "展示次数")
    private Long displayTimes;
    @ApiModelProperty(value = "有效展示次数")
    private Long validTimes;
    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;
    @ApiModelProperty(value = "有效比例")
    private String validRatio;
    @ApiModelProperty(value = "广告类型")
    private Integer materialType;
    @ApiModelProperty(value = "更新时间")
    @JsonIgnore
    protected Long updateTime;


    public String getValidRatio() {
        return validTimes>0? NumberFormatUtil.format(validTimes.longValue(), requestTimes.longValue(), Constant.RATIO_PATTERN): Constant.ZERO_PERCENT;
    }


}
