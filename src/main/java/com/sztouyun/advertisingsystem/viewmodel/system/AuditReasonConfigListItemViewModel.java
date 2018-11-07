package com.sztouyun.advertisingsystem.viewmodel.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AuditReasonConfigListItemViewModel {
    @ApiModelProperty(value = "问题分类id")
    private String id;
    @ApiModelProperty(value = "问题分类名称")
    private String name;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    private Date updatedTime;
    @JsonFormat(pattern = Constant.DATA_YMD_CN)
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;
    @ApiModelProperty(value = "创建人")
    private String creator;


    @ApiModelProperty(value = "子分类")
    private String subName;

    @ApiModelProperty(value = "子分类id")
    private String subId;

    @ApiModelProperty(value = "父子分类标志")
    private boolean isParent;


}
