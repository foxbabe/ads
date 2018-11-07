package com.sztouyun.advertisingsystem.viewmodel.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
@ApiModel
public class PartnerProfitStreamRequestViewModel extends BasePageInfo {

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime=new Date();

    @ApiModelProperty("合作方ID")
    @NotBlank(message = "合作方ID不能为空")
    private String partnerId;

}

