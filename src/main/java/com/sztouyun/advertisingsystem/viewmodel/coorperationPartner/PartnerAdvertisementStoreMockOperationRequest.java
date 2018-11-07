package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel
@Data
public class PartnerAdvertisementStoreMockOperationRequest {

    @ApiModelProperty(value = "广告门店Id")
    @NotBlank(message = "广告门店id不能为空")
    private String partnerAdvertisementStoreId;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = Constant.DATETIME)
    @NotNull(message = "时间不能为空")
    private Date date;

}
