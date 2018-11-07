package com.sztouyun.advertisingsystem.viewmodel.account;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserPageInfoViewModel extends BasePageInfo {
    @ApiModelProperty(value = "模糊查询用户昵称")
    private String nickname;

    public String getNickname() {
        if(this.nickname==null)
            return "";
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
