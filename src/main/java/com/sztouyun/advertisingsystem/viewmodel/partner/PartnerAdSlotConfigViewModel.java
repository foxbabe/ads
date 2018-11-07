package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.partner.PartnerAdSlotEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/8/21.
 */
@Data
public class PartnerAdSlotConfigViewModel {

    @ApiModelProperty("合作方广告位配置id")
    private Long id;

    @ApiModelProperty("广告位")
    private Integer adSlot;

    @ApiModelProperty("广告位名称")
    private String adSlotName;

    @ApiModelProperty("是否启用")
    private Boolean enabled ;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("操作人id")
    private String updaterId;

    @ApiModelProperty(value = "操作人姓名")
    private String updaterName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    public String getAdSlotName() {
        return EnumUtils.getDisplayName(adSlot, PartnerAdSlotEnum.class);
    }
}
