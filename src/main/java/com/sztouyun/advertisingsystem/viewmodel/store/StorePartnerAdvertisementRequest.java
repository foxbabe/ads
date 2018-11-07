package com.sztouyun.advertisingsystem.viewmodel.store;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by szty on 2018/7/26.
 */
@Data
@ApiModel
public class StorePartnerAdvertisementRequest extends BasePageInfo{
    @ApiModelProperty(value = "门店ID")
    @NotBlank(message = "门店ID不能为空")
    private String id;
    @ApiModelProperty(value = "合作方广告状态，1：投放中，2：已下架，3：已完成，多选逗号分隔")
    private String partnerAdvertisementStatus;
    @ApiModelProperty(value = "合作方ID")
    private String partnerId;
    @ApiModelProperty(value = "广告类型")
    @EnumValue(enumClass = MaterialTypeEnum.class,nullable = true)
    private Integer materialType;
    @ApiModelProperty(value = "合作模式")
    @EnumValue(enumClass = CooperationPatternEnum.class,nullable = true)
    private Integer cooperationPattern;
    @ApiModelProperty(value = "广告ID")
    private String thirdPartId;

    @ApiModelProperty(hidden = true)
    private Set<String> partnerAdvertisementIds;

    @ApiModelProperty(hidden = true)
    private String tempCollectionName;

    @ApiModelProperty(hidden = true)
    private List<String> partnerIds;

    public List<Integer> getPartnerAdvertisementStatus() {
        return org.springframework.util.StringUtils.isEmpty(partnerAdvertisementStatus)?new ArrayList<>():StringUtils.stringToInts(partnerAdvertisementStatus, Constant.SEPARATOR);
    }
}
