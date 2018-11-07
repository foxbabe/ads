package com.sztouyun.advertisingsystem.viewmodel.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel
public class TerminalAndAdvertisementPositionRequest {

    @ApiModelProperty(value = "终端类型和广告位id的集合")
    @NotNull(message = "收银机、iOS和Android三种终端类型，至少存在配置一个")
    @Size(min = 1, message = "收银机、iOS和Android三种终端类型，至少存在配置一个")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
