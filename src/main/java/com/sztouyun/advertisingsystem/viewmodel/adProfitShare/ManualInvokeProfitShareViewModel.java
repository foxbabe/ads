package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel
public class ManualInvokeProfitShareViewModel {

    @ApiModelProperty(value = "需要计算的门店", required = true)
    private List<String> storeIds;

    @ApiModelProperty(value = "门店日收益的开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "门店日收益的结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "当前门店已经激活的广告ID集合")
    private List<String> activeAdvertisements;

    public List<String> getActiveAdvertisements() {
        return activeAdvertisements;
    }

    public void setActiveAdvertisements(List<String> activeAdvertisements) {
        this.activeAdvertisements = activeAdvertisements;
    }

    public List<String> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<String> storeIds) {
        this.storeIds = storeIds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
