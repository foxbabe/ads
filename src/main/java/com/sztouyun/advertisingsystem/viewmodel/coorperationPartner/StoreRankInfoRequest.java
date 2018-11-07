package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by szty on 2018/7/25.
 */
@Data
@ApiModel
public class StoreRankInfoRequest {
    @ApiModelProperty(value = "合作方ID",required = true)
    @NotBlank(message = "合作方ID不能为空")
    private String partnerId;
    @ApiModelProperty(value = "排名方式1:按请求城市排序,2:按有效次数排序，3：按有效比例排序")
    @EnumValue(enumClass = RequestStoreRankSortTypeEnum.class,nullable = true)
    private Integer sortType=RequestStoreRankSortTypeEnum.Default.getValue();
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = Constant.DATA_YMD, timezone = "GMT+8")
    private Date endTime;

    public Date getEndTime() {
        return endTime==null?new Date():endTime;
    }
}
