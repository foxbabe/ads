package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.partner.PartnerAdvertisementOperationTimes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementBaseViewModel extends PartnerAdvertisementOperationTimes {

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "三方广告Id")
    private String thirdPartId;

    @ApiModelProperty(value = "广告类型")
    private Integer materialType;

    @ApiModelProperty(value = "广告类型名称")
    private String materialTypeName;

    @ApiModelProperty(value = "合作方")
    private String partnerName;

    @ApiModelProperty(value = "合作模式")
    private Integer cooperationPattern;

    @ApiModelProperty(value = "合作模式名称")
    private String cooperationPatternName;

    @ApiModelProperty(value = "合作模式时长")
    private Integer duration;

    @ApiModelProperty(value = "合作模式时长单位")
    private Integer durationUnit;

    @ApiModelProperty(value = "投放门店数量")
    private Integer storeCount;

    @ApiModelProperty(value = "有效门店数量")
    private Integer validStoreCount;

    @ApiModelProperty(value = "有效比例")
    private String validRatio;

    @ApiModelProperty(value = "广告状态名称")
    private String advertisementStatusName;

    @ApiModelProperty(value = "广告状态")
    private Integer advertisementStatus;

    @ApiModelProperty(value = "维护人")
    private String ownerName;

    @ApiModelProperty(value = "维护人Id")
    private String ownerId;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty("合作模式时长组合显示")
    private String durationName;

    public String getValidRatio() {
        return NumberFormatUtil.format(getValidDisplayTimes(),getRequestTimes(), Constant.RATIO_PATTERN);
    }
}
