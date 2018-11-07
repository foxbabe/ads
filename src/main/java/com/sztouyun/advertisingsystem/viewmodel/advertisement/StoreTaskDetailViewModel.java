package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class StoreTaskDetailViewModel {

    @ApiModelProperty(value = "任务编号")
    private String code;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "任务开始时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "任务结束时间")
    @JsonFormat(pattern = Constant.TIME_YMDHM_CN,timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "预计解决天数")
    private Integer expectedCompletionTime;

    @ApiModelProperty(value = "预计解决天数的单位")
    private Integer expectedCompletionUnit;

    @ApiModelProperty(value = "任务状态")
    private Integer taskStatus;

    @ApiModelProperty(value = "任务状态名称")
    private String taskStatusName;

    @ApiModelProperty(value = "任务结果")
    private Integer taskResult;

    @ApiModelProperty(value = "任务结果名称")
    private String taskResultName;

    @ApiModelProperty(value = "任务分类")
    private Integer taskCategory;

    @ApiModelProperty(value = "任务分类名称")
    private String taskCategoryName;

    @ApiModelProperty(value = "实际解决天数")
    private Integer actualCompletionDays;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店Id")
    private String shopId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "省份ID")
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地区名称")
    private String regionName;

    @ApiModelProperty(value = "具体地址")
    private String storeAddress;

    @ApiModelProperty(value = "门店是否可用")
    private boolean available;

    @ApiModelProperty(value = "门店是否存在")
    private boolean deleted;

    @ApiModelProperty(value = "运维人员")
    private String ownerName;

    @ApiModelProperty(value = "联系电话")
    private String ownerPhone;

    @ApiModelProperty(value = "任务排查文字说明")
    private String remark;

    @ApiModelProperty(value = "任务排查图片说明")
    private List<String> remarkPicture=new ArrayList<>();

    @ApiModelProperty(value = "任务描述")
    private String name;

    public String getActualCompletionDays() {
        if(beginTime == null && taskStatus == TaskStatusEnum.Cancel.getValue()) {
            return "0天";
        }
        return endTime==null?"":DateUtils.formateYmd(DateUtils.getIntervalDays(beginTime,endTime));
    }

    public String getExpectedCompletionDays() {
        return expectedCompletionTime+ EnumUtils.getDisplayName(expectedCompletionUnit, UnitEnum.class);
    }
}
