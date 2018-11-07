package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.ComparisonTypeEnum;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigGroupEnum;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoricalParamConfigViewModel {
    @ApiModelProperty(value = "配置ID")
    private String configId;


    @ApiModelProperty(value = "带历史记录的参数配置类型(1:广告分成-开店天数,2:广告分成-开机时长,3:广告分成-月平均交易订单数量," +
            "4:广告分成-分成标准,5:广告分成-日交易订单数量,6:广告分成-容错率,7:广告结算-结算日期,8:广告结算-消息提醒日期,21:未上架-未激活天数,22:未激活-未激活天数,23:预计解决天数,31:合作方计费模式-ecpm)", required = true)
    @EnumValue(enumClass = HistoricalParamConfigTypeEnum.class,message = "带历史记录的参数配置类型不匹配")
    private Integer type;

    @ApiModelProperty(value = "比较符(1:等于=,2:大于>,3:小于<,4:大于等于>=,5:小于等于<=)")
    @EnumValue(enumClass = ComparisonTypeEnum.class,nullable = true,message = "输入不匹配")
    private Integer comparisonType;


    @ApiModelProperty(value = "单位(2:小时,3:日)")
    @EnumValue(enumClass = UnitEnum.class,nullable = true,message = "单位输入不匹配")
    private Integer unit;

    @ApiModelProperty(value = "标准值", required = true )
    @NotNull(message = "标准值不能为空")
    private Double  value;

    @ApiModelProperty(value = "objectId" )
    private String  objectId="";

    public HistoricalParamConfigViewModel(Integer type, Integer comparisonType, Integer unit, Double value, String objectId) {
        this(null,type,comparisonType,unit,value,objectId);
    }
}
