package com.sztouyun.advertisingsystem.viewmodel.advertisement.material;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel
public class MaterialItemViewModel {
    @ApiModelProperty(value = "素材ID", required = true)
    @NotNull(message = "素材ID不能为空")
    private String id;

    @ApiModelProperty(value = "投放位置ID,从配置接口读取")
    private String positionId;

    @ApiModelProperty(value = "分辨率")
    private String resolution;

    @ApiModelProperty(value = "素材点击Url")
    @Size(max = 200, message = "URL长度不能大于200")
    private String materialClickUrl;

    @ApiModelProperty(value = "素材二维码Url")
    @Size(max = 200, message = "URL长度不能大于200")
    private String materialQRCodeUrl;

    @ApiModelProperty(value = "二维码位置: 1 - 左下角, 2 - 右下角, 3 - 居中")
    private Integer qRCodePosition;

    @ApiModelProperty(value = "是否记录手机号码")
    private Boolean isNotePhoneNumber;
}
