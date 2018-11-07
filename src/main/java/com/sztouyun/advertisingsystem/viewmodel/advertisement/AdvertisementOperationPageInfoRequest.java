package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationStatusEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
@ApiModel
public class AdvertisementOperationPageInfoRequest extends BasePageInfo {
    @ApiModelProperty(value = "广告ID)")
    @NotBlank(message = "广告ID不能为空")
    private String id;

    @ApiModelProperty(value = "操作状态(0:全部状态,1:提交审核,2:审核通过,3:审核驳回,4:投放中,5:已下架,6:广告完成,7:待提交)")
    @EnumValue(enumClass = AdvertisementOperationStatusEnum.class,message = "广告操作状态不匹配")
    private Integer operationStatus;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value="结束时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date endTime;
}
