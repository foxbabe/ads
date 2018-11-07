package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class ReCalculateAdvertisementProfitActiveViewModel {
    @ApiModelProperty(value = "广告Ids", required = true)
    @NotNull(message = "广告Id列表不能为空")
    private List<String> advertisementIds;
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startDate;
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endDate;
}
