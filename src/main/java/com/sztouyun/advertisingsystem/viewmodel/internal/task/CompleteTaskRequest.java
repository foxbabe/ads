package com.sztouyun.advertisingsystem.viewmodel.internal.task;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.task.TaskResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2018/4/10.
 */
@ApiModel
@Data
public class CompleteTaskRequest {
    @ApiModelProperty(value = "任务ID",required = true)
    @NotEmpty(message = "任务ID不能为空")
    private String id;
    @ApiModelProperty(value = "任务结果，1：已解决，2：未解决",required = true)
    @EnumValue(enumClass = TaskResultEnum.class,message = "不支持该类型")
    private Integer taskResult;
    @ApiModelProperty(value = "任务责任人姓名",required = true)
    @NotEmpty(message = "任务责任人姓名不能为空")
    private String ownerName;
    @ApiModelProperty(value = "任务责任人电话",required = true)
    @NotEmpty(message = "任务责任人电话不能为空")
    private String ownerPhone;
    @ApiModelProperty(value = "任务排查说明",required = true)
    @NotEmpty(message = "任务排查说明不能为空")
    private String remark;
    @ApiModelProperty(value = "附件")
    private List<String> attachments;

    @ApiModelProperty(hidden = true)
    private Date date;

    public Date getDate(){
        return null;
    }

}
