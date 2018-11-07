package com.sztouyun.advertisingsystem.viewmodel.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class OrderDetailViewModel {
    @ApiModelProperty(value = "日期列表",required = true)
    @NotNull(message = "日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private List<Date> dates;
    @ApiModelProperty(value = "门店ID列表",required = true)
    @NotNull(message = "门店不能为空")
    private List<String> storeIds;
}
