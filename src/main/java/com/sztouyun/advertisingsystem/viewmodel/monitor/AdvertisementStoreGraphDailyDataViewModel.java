package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
@NoArgsConstructor
public class AdvertisementStoreGraphDailyDataViewModel {

    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    @ApiModelProperty("每日时间")
    private Date date;

    @ApiModelProperty("激活门店数量")
    private Integer activeStoreCount = 0;

    @ApiModelProperty("未激活门店数量")
    private Integer unActiveStoreCount = 0;

    @ApiModelProperty("激活比例")
    private String ratio;{
        ratio = "0%";
    }

    public AdvertisementStoreGraphDailyDataViewModel(Date date) {
        this.date = date;
    }
}
