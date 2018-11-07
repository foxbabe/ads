package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;


/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class DetailAdvertisementDisplayTimesConfigViewModel extends UpdateAdvertisementDisplayTimesConfigViewModel {
    @ApiModelProperty(value = "创建人", required = true)
    private String creator;

    @ApiModelProperty(value="时间单位名称",required = true)
    private String timeUnitName;

    @ApiModelProperty(value = "创建时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间", required = true)
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

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

    public String getTimeUnitName() {
        return timeUnitName;
    }

    public void setTimeUnitName(String timeUnitName) {
        this.timeUnitName = timeUnitName;
    }
}
