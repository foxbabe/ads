package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TerminalProportion {
    @ApiModelProperty("终端类型枚举值: 1-收银机, 2-iOS, 3-Android")
    private Integer terminalType;
    @ApiModelProperty("终端名称")
    private String terminalName;
    @ApiModelProperty("已展示次数")
    private Integer displayTimes;
    @ApiModelProperty("已展示次数占比")
    private String proportion;// 占比 = 单个终端的已展示次数 / 当前广告所有终端下的已展示次数之和
}
