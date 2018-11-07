package com.sztouyun.advertisingsystem.viewmodel.account;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@ApiModel
public class BatchOperateUserViewModel {
    @ApiModelProperty(value = "人员ID列表", required = true)
    @NotEmpty(message = "人员ID列表不能为空")
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
