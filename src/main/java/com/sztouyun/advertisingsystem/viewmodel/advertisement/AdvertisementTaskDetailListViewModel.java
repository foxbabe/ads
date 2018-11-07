package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AdvertisementTaskDetailListViewModel extends BasePageInfo {

    @ApiModelProperty(value = "任务状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "任务状态名称")
    private String taskStatusName;

    @ApiModelProperty(value = "任务结果")
    private Integer taskResult;

    @ApiModelProperty(value = "任务结果名称")
    private String taskResultName;

    @ApiModelProperty(value = "任务开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "任务结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "任务编号")
    private String code;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店Id")
    private String shopId;

    @ApiModelProperty(value = "任务Id")
    private String id;

    @ApiModelProperty(value = "任务分类")
    private Integer taskCategory;

    @ApiModelProperty(value = "任务分类名称")
    private String taskCategoryName;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "运维人员")
    private String ownerName;

    @ApiModelProperty(value = "联系电话")
    private String ownerPhone;

    @ApiModelProperty(value = "实际解决天数")
    private Integer actualCompletionDays;

    @ApiModelProperty(value = "预计解决天数")
    private Integer expectedCompletionTime;

    @ApiModelProperty(value = "预计解决天数的单位")
    private Integer expectedCompletionUnit;

    public String getExpectedCompletionTime() {
        return expectedCompletionTime+ EnumUtils.getDisplayName(expectedCompletionUnit, UnitEnum.class);
    }

    public String getActualCompletionDays() {
        if(beginTime == null && taskStatus == TaskStatusEnum.Cancel.getValue()) {
            return "0天";
        }
        return endTime==null?"":DateUtils.formateYmd(DateUtils.getIntervalDays(beginTime,endTime));
    }


}
