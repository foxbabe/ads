package com.sztouyun.advertisingsystem.viewmodel.system;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by szty on 2018/9/11.
 */
@Data
@ApiModel
public class GroupConfigRequest {
    @ApiModelProperty(value = "配置组(1:广告分成，2：广告结算，3：任务，4：合作方计费模式)")
    @EnumValue(enumClass = HistoricalParamConfigGroupEnum.class,nullable = true)
    private Integer group;
    @ApiModelProperty(value = "objectId")
    private String objectId="";
}
