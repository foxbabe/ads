package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CooperationPartnerChartRequestTimesViewModel {

    @ApiModelProperty(value = "合作方Id")
    private String id;

    @ApiModelProperty(value = "合作方名称")
    private String name;

    @ApiModelProperty(value = "请求次数")
    private Long requestTimes=0L;

    @ApiModelProperty(value = "请求成功次数")
    private Long requestSuccessTimes=0L;

    @ApiModelProperty(value = "请求成功占比")
    private String requestSuccessTimesRatio="0%";

    @ApiModelProperty(value = "请求失败次数")
    private Long requestFailTimes=0L;

    @ApiModelProperty(value = "请求失败占比")
    private String requestFailTimesRatio="0%";

    public Long getRequestFailTimes() {
        return requestTimes-requestSuccessTimes;
    }

    public String getRequestSuccessTimesRatio() {
        if(requestSuccessTimes!=0L)
            return NumberFormatUtil.format(requestSuccessTimes, getRequestTimes(), Constant.RATIO_PATTERN);
        return requestSuccessTimesRatio;
    }

    public String getRequestFailTimesRatio() {
        if(getRequestFailTimes()!=0L)
            return NumberFormatUtil.format(getRequestFailTimes(), getRequestTimes(), Constant.RATIO_PATTERN);
        return requestFailTimesRatio;
    }
}
