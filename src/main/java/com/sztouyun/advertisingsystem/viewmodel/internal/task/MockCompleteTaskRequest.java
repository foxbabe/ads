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
public class MockCompleteTaskRequest extends CompleteTaskRequest {
   @ApiModelProperty(value = "任务完成时间")
   @JsonFormat(pattern = Constant.DATETIME)
    private Date date;

   @Override
   public Date getDate(){
        return date;
   }
}
