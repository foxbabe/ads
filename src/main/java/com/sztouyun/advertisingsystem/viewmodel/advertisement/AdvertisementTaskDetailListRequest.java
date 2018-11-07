package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.model.task.TaskResultEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementTaskDetailListRequest extends BasePageInfo {

    @ApiModelProperty(value = "任务状态")
    @EnumValue(enumClass = TaskStatusEnum.class,message = "任务状态类型不正确！",nullable = true)
    private Integer taskStatus;

    @ApiModelProperty(value = "任务结果")
    @EnumValue(enumClass = TaskResultEnum.class,message = "任务结果类型不正确！",nullable = true)
    private Integer taskResult;

    @ApiModelProperty(value = "任务分类")
    @EnumValue(enumClass = TaskCategoryEnum.class,message = "任务分类类型不正确！",nullable = true)
    private Integer taskCategory;

    @ApiModelProperty(value = "任务开始时间")
    @JsonFormat(pattern = Constant.DATETIME,timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "任务结束时间")
    @JsonFormat(pattern = Constant.DATETIME,timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "任务编号")
    private String code;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店Id")
    private String shopId;

    @ApiModelProperty(value = "广告Id")
    @NotBlank(message = "广告id不能为空")
    private String advertisementId;

}
