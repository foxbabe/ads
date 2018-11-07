package com.sztouyun.advertisingsystem.viewmodel.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class ContractSignTendencyViewModel {

    @ApiModelProperty(value = "合同签约时间")
    @JsonFormat(pattern = "M/dd", timezone = "GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "每天的签约总数")
    private Long total;

    @ApiModelProperty(value = "合同详细签约时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date signTimeDetail;

    public Date getSignTimeDetail() {
        return signTimeDetail;
    }

    public void setSignTimeDetail(Date signTimeDetail) {
        this.signTimeDetail = signTimeDetail;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
