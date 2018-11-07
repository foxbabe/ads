package com.sztouyun.advertisingsystem.viewmodel.internal.task;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by wenfeng on 2018/4/8.
 */
@ApiModel
@Data
public class StoreTasksRequest extends BaseTasksRequest{
    @ApiModelProperty(value = "门店编号列表,逗号分隔",required = true)
    @NotBlank(message = "门店编号不能为空")
    private String storeNos;
}
