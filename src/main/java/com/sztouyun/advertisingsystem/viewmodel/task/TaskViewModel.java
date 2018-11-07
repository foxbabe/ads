package com.sztouyun.advertisingsystem.viewmodel.task;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class TaskViewModel {
    @ApiModelProperty(value = "任务Id")
    private String id;
    @ApiModelProperty(value = "任务名称")
    private String name;
    @ApiModelProperty(value = "任务CODE")
    private String code;
    @ApiModelProperty(value = "任务类别")
    private int taskCategory;
    @ApiModelProperty(value = "预计完成时间")
    private Integer expectedCompletionTime;
    @ApiModelProperty(value = "预计完成时间单位")
    private Integer expectedCompletionUnit;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date createdTime;
    @ApiModelProperty(value = "任务描述")
    private String remark;
}
