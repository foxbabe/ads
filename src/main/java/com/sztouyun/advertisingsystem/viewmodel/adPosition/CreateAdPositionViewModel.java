package com.sztouyun.advertisingsystem.viewmodel.adPosition;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

@ApiModel
public class CreateAdPositionViewModel {

    @ApiModelProperty(value = "门店类型", required = true)
    @Max(value= Constant.INTEGER_MAX,message = "门店类型最大值不能超过999999999")
    @NotNull(message = "门店类型不能为空")
    private Integer storeType;
    @ApiModelProperty(value = "最大广告位数量",required = true)
    @Min(value = 1,message = "最大广告位数量不能小于0")
    @Max(value= Constant.INTEGER_MAX,message = "最大广告位数量最大值不能超过999999999")
    @NotNull(message = "最大广告位数量为空")
    private Integer adCount;

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getAdCount() {
        return adCount;
    }

    public void setAdCount(Integer adCount) {
        this.adCount = adCount;
    }
}
