package com.sztouyun.advertisingsystem.viewmodel.internal.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CanceledTasksViewModel{
    @ApiModelProperty(value = "任务Id")
    private String id;
    @ApiModelProperty(value = "任务描述")
    private String remark;
}
