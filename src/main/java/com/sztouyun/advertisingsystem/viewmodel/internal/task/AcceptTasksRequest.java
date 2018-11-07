package com.sztouyun.advertisingsystem.viewmodel.internal.task;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.task.TaskResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wenfeng on 2018/4/10.
 */
@ApiModel
@Data
public class AcceptTasksRequest {
    @ApiModelProperty(value = "任务ID列表，逗号分隔",required = true)
    @NotBlank(message = "任务ID列表不能为空")
    private String taskIds;
    @ApiModelProperty(value = "任务责任人姓名",required = true)
    @NotBlank(message = "任务责任人姓名不允许为空")
    private String ownerName;
    @ApiModelProperty(value = "任务责任人电话",required = true)
    @NotBlank(message = "任务责任人电话不允许为空")
    private String ownerPhone;

    private List<String> getTaskIds(){
        return Arrays.asList(StringUtils.split(taskIds, Constant.SEPARATOR));
    }
}
