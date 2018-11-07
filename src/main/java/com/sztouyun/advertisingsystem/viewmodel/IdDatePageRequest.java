package com.sztouyun.advertisingsystem.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by szty on 2018/9/3.
 */
@Data
@ApiModel
public class IdDatePageRequest extends BasePageInfo {
    @ApiModelProperty
    @NotBlank(message = "Id不能为空")
    private String id;
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime= LocalDate.now().toDate();
}
