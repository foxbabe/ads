package com.sztouyun.advertisingsystem.viewmodel.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalParamConfigInfo {

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updater;

    @ApiModelProperty(value = "带历史记录的参数配置的集合")
    private List<HistoricalParamConfigViewModel> list;
}
