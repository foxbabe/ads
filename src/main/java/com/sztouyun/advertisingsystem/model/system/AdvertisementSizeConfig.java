package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class AdvertisementSizeConfig extends BaseModel{
    @Column(nullable = false)
    private Integer advertisementPositionType;

    /**
     * 终端类型
     */
    @Column(nullable = false,columnDefinition = "Integer default  1")
    private Integer terminalType;

    /**
     * 水平分辨率
     */
    @Column(nullable = false)
    private Integer horizontalResolution;

    /**
     * 垂直分辨率
     */
    @Column(nullable = false)
    private Integer verticalResolution;

    /**
     * 宽度比
     */
    @Column(nullable = false)
    private Integer widthRatio;

    /**
     * 高度比
     */
    @Column(nullable = false)
    private Integer highRatio;

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(Integer horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public Integer getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(Integer verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public Integer getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(Integer widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Integer getHighRatio() {
        return highRatio;
    }

    public void setHighRatio(Integer highRatio) {
        this.highRatio = highRatio;
    }

    public Integer getAdvertisementPositionType() {
        return advertisementPositionType;
    }

    public void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    public String getImgSpecification() {
        return this.getHorizontalResolution() + "*" + this.getVerticalResolution();
    }

    @Transient
    private  final Map<AdvertisementPositionTypeEnum,List<MaterialTypeEnum>> invalidMaterialTypeMap=new HashMap<AdvertisementPositionTypeEnum,List<MaterialTypeEnum>>(){
        {
            put(AdvertisementPositionTypeEnum.BusinessBanner, Arrays.asList(MaterialTypeEnum.Video));
        }
    };

    public void validateMaterialType(Integer  materialType){
        AdvertisementPositionTypeEnum positionTypeEnum=EnumUtils.toEnum(advertisementPositionType, AdvertisementPositionTypeEnum.class);
        MaterialTypeEnum materialTypeEnum=EnumUtils.toEnum(materialType, MaterialTypeEnum.class);
        List<MaterialTypeEnum> invalidMaterialTypeList= invalidMaterialTypeMap.get(positionTypeEnum);
        if(invalidMaterialTypeList!=null && invalidMaterialTypeList.contains(materialTypeEnum))
            throw new BusinessException(positionTypeEnum.getDisplayName()+",广告素材暂不支持"+materialTypeEnum.getDisplayName()+"格式");
    }

}
