package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class CooperationPartnerLineChartStoreCountInfo {

    @ApiModelProperty(value = "请求门店数量")
    private Integer requestStoreCount=0;

    @ApiModelProperty(value = "未请求门店数量")
    private Integer unrequestedStoreCount=0;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "门店资源数量",hidden = true)
    private Integer configStoreCount=0;

    public Integer getUnrequestedStoreCount() {
        return configStoreCount-requestStoreCount;
    }

    public CooperationPartnerLineChartStoreCountInfo(Date date,Integer configStoreCount) {
        this.date = date;
        this.configStoreCount = configStoreCount;
    }

    public CooperationPartnerLineChartStoreCountInfo() {
    }
}
