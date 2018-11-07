package com.sztouyun.advertisingsystem.viewmodel.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by wenfeng on 2017/9/20.
 */

@ApiModel
public class DistributionStatisticsViewModel {
    @ApiModelProperty(value = "统计类型: 1:客户, 2:合同, 3:广告, 4:广告位")
    private Integer statisticsType;

    @ApiModelProperty(value = "统计明细")
    private List<DistributionStatistic<String>> list;

    public Integer getStatisticsType() {
        return statisticsType;
    }
    public DistributionStatisticsViewModel(){}

    public DistributionStatisticsViewModel(Integer statisticsType, List<DistributionStatistic<String>> list){
        this.statisticsType=statisticsType;
        this.list=list;
    }


    public void setStatisticsType(Integer statisticsType) {
        this.statisticsType = statisticsType;
    }

    public List<DistributionStatistic<String>> getList() {
        return list;
    }

    public void setList(List<DistributionStatistic<String>> list) {
        this.list = list;
    }

}
