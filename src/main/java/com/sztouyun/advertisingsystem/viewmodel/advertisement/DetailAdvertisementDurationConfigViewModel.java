package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class DetailAdvertisementDurationConfigViewModel {
    @ApiModelProperty(value = "广告时长id", required = true)
    private String id;

    @ApiModelProperty(value = "时长单位名称", required = true)
    private String unitName;

    @ApiModelProperty(value = "时长单位", required = true)
    private Integer durationUnit;

    @ApiModelProperty(value = "时长", required = true)
    private Integer  duration;

    @ApiModelProperty(value = "创建人", required = true)
    private String creator;

    @ApiModelProperty(value = "创建时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(Integer durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
