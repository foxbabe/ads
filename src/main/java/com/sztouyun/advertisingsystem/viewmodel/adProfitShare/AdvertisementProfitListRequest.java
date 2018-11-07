package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;


@ApiModel
@Data
public class AdvertisementProfitListRequest  extends BasePageInfo {

    @ApiModelProperty(value = "实际开始投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "实际结束投放时间")
    @JsonFormat(pattern = Constant.DATETIME, timezone = "GMT+8")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "业务员")
    private String nickname;

    @ApiModelProperty(value = "是否开启分成，全部：null,是：true,否：false")
    private Boolean enableProfitShare;

    @ApiModelProperty(value = "广告状态类型列表，逗号分隔（这里支持多选，全选需传全部状态）")
    @NotBlank(message = "广告状态类型不能为空")
    private String advertisementStatus;

}
