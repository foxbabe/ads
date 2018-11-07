package com.sztouyun.advertisingsystem.viewmodel.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MessageStatisticViewModel {

    @ApiModelProperty(value = "未读消息总数")
    private Integer unReadCount = 0;

    @ApiModelProperty(value = "所有消息总数")
    private Long totalMessageCount = 0L;

    public Integer getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(Integer unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Long getTotalMessageCount() {
        return totalMessageCount;
    }

    public void setTotalMessageCount(Long totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
    }
}
