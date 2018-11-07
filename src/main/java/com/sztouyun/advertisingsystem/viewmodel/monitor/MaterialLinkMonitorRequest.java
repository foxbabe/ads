package com.sztouyun.advertisingsystem.viewmodel.monitor;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class MaterialLinkMonitorRequest extends BasePageInfo {

    @ApiModelProperty(value = "广告id")
    private String advertisementId;

    @ApiModelProperty(value = "广告投放位置; 1:待机全屏广告; 2:扫描支付页面; 3:商家待机全屏; 4:商家Banner; 5:Android-App开屏; 6:Android-Banner; 7:iOS-App开屏; 8:iOS-Banner")
    @EnumValue(enumClass = AdvertisementPositionCategoryEnum.class, message = "广告投放位置类型不正确",nullable = true)
    private Integer advertisementPositionCategory;

    @ApiModelProperty(value = "链接方式; 1:Url点击; 2:二维码; 多个用逗号隔开，全部为null")
    private String linkType;

    public List<Integer> getLinkTypes() {
        return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(linkType, Constant.SEPARATOR);
    }
}