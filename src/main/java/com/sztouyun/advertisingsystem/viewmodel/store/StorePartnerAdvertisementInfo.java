package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/7/26.
 */
@Data
@ApiModel
public class StorePartnerAdvertisementInfo extends PartnerAdvertisementRequestInfo {
    @ApiModelProperty(value = "合作方名称")
    private String partnerName;
    @ApiModelProperty(value = "合作模式")
    private String cooperationPatternName;
    @ApiModelProperty(value ="更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;
    @ApiModelProperty(value ="广告状态名称")
    private String partnerAdvertisementStatusName;
    @ApiModelProperty(value = "广告类型名称")
    private String materialTypeName;
    @ApiModelProperty(value = "能否查看")
    private boolean canView;

    public Date getUpdatedTime() {
        return new Date(updateTime);
    }
}
