package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class SummaryStatisticViewModel {

    @ApiModelProperty(value = "统计类型: 1:客户总数, 2:合同签约, 3:投放中广告, 4:可用广告位")
    private Integer statisticType;

    @ApiModelProperty(value = "统计开始时间")
    @JsonFormat(pattern = "M/dd", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "统计结束时间")
    @JsonFormat(pattern = "M/dd", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "统计百分比")
    private double percentage;

    @ApiModelProperty(value = "总数")
    private Long total;

    @ApiModelProperty(value = "是否是增长百分比, true:是;  false:不是")
    private Boolean isIncrement = true;

    public Boolean getIncrement() {
        return isIncrement;
    }

    public void setIncrement(Boolean increment) {
        isIncrement = increment;
    }

    public Integer getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(Integer statisticType) {
        this.statisticType = statisticType;
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
