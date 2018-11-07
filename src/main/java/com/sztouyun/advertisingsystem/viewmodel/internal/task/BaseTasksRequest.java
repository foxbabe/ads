package com.sztouyun.advertisingsystem.viewmodel.internal.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.model.task.TaskTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Past;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/4/8.
 */
@ApiModel
@Data
public class BaseTasksRequest extends BasePageInfo{
    @ApiModelProperty(hidden = true)
    private Integer taskType=TaskTypeEnum.Advertisement.getValue();

    @ApiModelProperty(hidden = true)
    private Integer taskSubType=TaskTypeEnum.Store.getValue();

    @ApiModelProperty(value = "开始时间，任务创建时间之前,格式yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = Constant.DATETIME)
    @Past(message = "开始时间必须在当前时间之前")
    private Date startTime;

    @ApiModelProperty(value = "结束时间，任务创建时间之后，格式yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = Constant.DATETIME)
    @Past(message = "结束时间必须在当前时间之前")
    private Date endTime;

    @ApiModelProperty(value = "门店来源类型")
    @EnumValue(enumClass = StoreSourceEnum.class,nullable = true)
    private Integer storeSource;

    @ApiModelProperty(value = "任务ID列表，逗号分隔")
    private String taskIds;

    public List<String> getTaskIds(){
        return StringUtils.isBlank(taskIds)?new ArrayList<>():Arrays.asList(StringUtils.split(taskIds, Constant.SEPARATOR));
    }
}
