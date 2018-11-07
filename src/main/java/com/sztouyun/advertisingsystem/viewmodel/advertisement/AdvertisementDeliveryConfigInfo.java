package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.model.material.Material;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wenfeng on 2017/12/13.
 */
@Data
@ApiModel
public class AdvertisementDeliveryConfigInfo {
    @ApiModelProperty(value = "positionID")
    private String positionId;
    @ApiModelProperty(value = "广告位置类型")
    private Integer positionType;
    @ApiModelProperty(value = "广告位置名称")
    private String positionTypeName;
    @ApiModelProperty(value = "广告尺寸")
    private String resolution;
    @ApiModelProperty(value = "广告时长")
    private String duration;
    @ApiModelProperty(value = "广告播放次数")
    private String displayTimes;
    @ApiModelProperty(value = "素材ID")
    private String materialId;
    @ApiModelProperty(value = "素材内容")
    private String data;
    @ApiModelProperty(value = "素材类型")
    private Integer materialType;

    @ApiModelProperty(value = "素材点击Url是否启用")
    private Boolean clickUrlEnable;

    @ApiModelProperty(value = "素材点击Url")
    private String materialClickUrl;

    @ApiModelProperty(value = "素材二维码Url是否启用")
    private Boolean qRCodeUrlEnable;

    @ApiModelProperty(value = "素材二维码Url")
    private String materialQRCodeUrl;

    @ApiModelProperty(value = "二维码位置枚举值: 1 - 左下角, 2 - 右下角, 3 - 居中")
    private Integer qRCodePosition;

    @ApiModelProperty(value = "二维码位置名称")
    private String qRCodePositionName;

    @ApiModelProperty(value = "是否记录手机号码")
    private Boolean isNotePhoneNumber;

    public AdvertisementDeliveryConfigInfo setMaterialInfo(Material material){
        if(material!=null){
            setMaterialId(material.getId());
            this.materialType=material.getMaterialType();
            setData(material.getData());
        }
        return this;
    }
}
