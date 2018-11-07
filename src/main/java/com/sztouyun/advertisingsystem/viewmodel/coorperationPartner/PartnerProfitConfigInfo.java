package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.system.HistoricalParamConfigViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by szty on 2018/9/12.
 */
@ApiModel
@Data
public class PartnerProfitConfigInfo extends HistoricalParamConfigViewModel {
    @ApiModelProperty(value = "合作方名称")
    private String partnerName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date createdTime;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date updatedTime;

    @ApiModelProperty(value = "计费模式")
    private String profitModeName;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(hidden = true)
    private String creatorId;

    public String getProfitValue() {
        return NumberFormatUtil.format(getValue(),Constant.SCALE_TWO, RoundingMode.DOWN);
    }
}
