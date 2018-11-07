package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigGroupEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by szty on 2018/9/12.
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class PartnerProfitConfigInfoRequest extends BasePageInfo {
    @ApiModelProperty(value = "配置组,4:合作方计费模式")
    @EnumValue(enumClass = HistoricalParamConfigGroupEnum.class)
    private Integer group;
    @ApiModelProperty(hidden = true)
    public Date getDate(){
        return new Date();
    }
}
