package com.sztouyun.advertisingsystem.viewmodel.monitor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by wenfeng on 2017/11/1.
 */
@ApiModel
public class MonitorStatusStatistic {
    @ApiModelProperty(value = "状态 0: 全部; 1: 监控中; 2: 已完成")
    private Integer status;
    @ApiModelProperty(value = "总数")
    private Long count;

    public MonitorStatusStatistic(){}
    public MonitorStatusStatistic(Integer status,Long count){
        this.status=status;
        this.count=count;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
