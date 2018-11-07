package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitConfig;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.MaterialItemViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2017/8/4.
 */
@Data
@ApiModel
public class BaseAdvertisementViewModel {
    @ApiModelProperty(value = "广告名称)")
    @NotBlank(message = "请输入广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "素材内容")
    private String data;
    @ApiModelProperty(value = "素材列表,update为必传项")
    private List<MaterialItemViewModel> materialList;

    @ApiModelProperty(value = "广告类型(图片:1，文本:2 ,视频:3,图片+视频:4)", required = true)
    @NotNull(message = "广告类型不能为空")
    @EnumValue(enumClass = MaterialTypeEnum.class, message = "广告类型不匹配")
    private Integer advertisementType;

    @ApiModelProperty(value = "广告投放开始时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @NotNull(message = "请选择有效的广告投放开始时间")
    @Future(message = "广告开始投放时间必须大于当前时间(精确到小时)")
    private Date startTime;

    @ApiModelProperty(value = "广告投放截止时间", required = true)
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    @NotNull(message = "请选择有效的广告投放结束时间")
    @Future(message = "广告投放结束时间必须大于当前时间(精确到小时)")
    private Date endTime;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000, message = "广告备注过长")
    private String remark;

    @ApiModelProperty(value = "是否开启分成", required = true)
    @NotNull(message = "是否开启分成不能为空")
    private boolean enableProfitShare;

    @ApiModelProperty(value = "分成标准金额")
    @Max(value = Constant.MONEY_MAX, message = "分成标准金额最大值不能超过" + Constant.MONEY_MAX)
    @Min(value = 0, message = "分成标准金额不能小于0")
    private Double profitShareStandardAmount;

    @ApiModelProperty(value = "分成标准金额单位")
    @EnumValue(enumClass = UnitEnum.class, nullable = true, message = "分成标准金额单位不匹配")
    private Integer profitShareStandardAmountUnit;

    @Valid
    @ApiModelProperty(value = "广告分成配置")
    private AdvertisementProfitConfig advertisementProfitConfig;
}
