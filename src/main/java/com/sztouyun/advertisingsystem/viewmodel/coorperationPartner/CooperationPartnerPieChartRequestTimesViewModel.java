package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CooperationPartnerPieChartRequestTimesViewModel {

    @ApiModelProperty(value = "广告位请求信息")
    private List<CooperationPartnerPieChartRequestTimesInfo> CooperationPartnerPieChartRequestTimesInfos;

    @ApiModelProperty(value = "请求次数")
    private Long requestTimes=0L;

    @ApiModelProperty(value = "请求成功次数")
    private Long requestSuccessTimes=0L;

    @ApiModelProperty(value = "请求成功占比")
    private String requestSuccessTimesRatio="0%";

    @ApiModelProperty(value = "未获取广告次数")
    private Long getNoAdTimes = 0L;

    @ApiModelProperty(value = "未获取广告占比")
    private String getNoAdTimesRatio="0%";

    @ApiModelProperty(value = "接口异常次数")
    private Long apiErrorTimes = 0L;

    @ApiModelProperty(value = "接口异常占比")
    private String apiErrorTimesRatio="0%";

    @ApiModelProperty(value = "请求失败次数")
    private Long requestFailTimes=0L;

    @ApiModelProperty(value = "请求失败占比")
    private String requestFailTimesRatio="0%";

    public Long getRequestTimes() {
        return requestSuccessTimes+getNoAdTimes+apiErrorTimes;
    }

    public Long getRequestFailTimes() {
        return getNoAdTimes+apiErrorTimes;
    }

    public String getRequestFailTimesRatio() {
        if(getRequestFailTimes()!=0L)
            return NumberFormatUtil.format(getRequestFailTimes(), getRequestTimes(), Constant.RATIO_PATTERN);
        return requestFailTimesRatio;
    }

    public String getRequestSuccessTimesRatio() {
        if(requestSuccessTimes!=0L)
            return NumberFormatUtil.format(requestSuccessTimes, getRequestTimes(), Constant.RATIO_PATTERN);
        return requestSuccessTimesRatio;
    }

    public String getGetNoAdTimesRatio() {
        if(getNoAdTimes!=0L)
            return NumberFormatUtil.format(getNoAdTimes, getRequestTimes(), Constant.RATIO_PATTERN);
        return getNoAdTimesRatio;
    }

    public String getApiErrorTimesRatio() {
        if(apiErrorTimes!=0L)
            return NumberFormatUtil.format(apiErrorTimes, getRequestTimes(), Constant.RATIO_PATTERN);
        return apiErrorTimesRatio;
    }
}
