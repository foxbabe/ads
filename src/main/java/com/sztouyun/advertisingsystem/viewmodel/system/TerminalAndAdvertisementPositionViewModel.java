package com.sztouyun.advertisingsystem.viewmodel.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel
public class TerminalAndAdvertisementPositionViewModel{

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATE_TIME_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updater;

    @ApiModelProperty(value = "终端类型和广告位的集合")
    private List<TerminalAndAdvertisementPositionInfo> list;

    public List<TerminalAndAdvertisementPositionInfo> getList() {
        return list;
    }

    public void setList(List<TerminalAndAdvertisementPositionInfo> list) {
        this.list = list;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
}
