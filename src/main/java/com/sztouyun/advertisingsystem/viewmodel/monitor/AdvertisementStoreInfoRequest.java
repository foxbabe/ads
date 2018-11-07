package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
@ApiModel
public class AdvertisementStoreInfoRequest extends BasePageInfo {
    @ApiModelProperty(value = "门店ID")
    @NotBlank(message = "门店ID不能为空")
    private String id;

    @ApiModelProperty(value = "广告状态类型列表，逗号分隔（这里支持多选，全选需传全部状态）")
    @NotBlank(message = "广告状态类型不能为空")
    private String statusTypes;

    @ApiModelProperty(value = "广告类型: 1 - 图片, 2 - 文本, 3 - 视频, 4 - 图片+视频, 全部传null")
    @EnumValue(enumClass = MaterialTypeEnum.class, nullable = true, message = "广告类型枚举值不正确")
    private Integer advertisementType;

    @ApiModelProperty(value = "门店是否激活, 全选传null")
    private Boolean active;

    @ApiModelProperty(value = "是否开启分成, 全选传null")
    private Boolean enableProfitShare;

    @ApiModelProperty(value = "广告名称")
    private String advertisementName;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    public List<Integer> getStatusTypes() {
        return StringUtils.stringToInts(statusTypes, Constant.SEPARATOR);
    }
}
