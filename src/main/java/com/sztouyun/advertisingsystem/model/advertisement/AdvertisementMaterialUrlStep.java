package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AdvertisementMaterialUrlStep {

    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    /**
     * 广告素材中间表ID
     */
    @Column(length = 36, nullable = false)
    private String advertisementMaterialId;

    /**
     * 请求url
     */
    @Column(length = 200, nullable = false)
    private String url;

    /**
     * 步骤类型: AdvertisementMaterialUrlStepTypeEnum
     */
    @Column(nullable = false)
    private Integer stepType;

    @Column(length = 36)
    private String nextStepId;

    /**
     * 当前步骤需要的参数: AdvertisementMaterialUrlParamEnum
     */
    @Column(length = 50)
    private String requiredParams;

    /**
     * 素材url类型: MaterialLinkTypeEnum
     */
    @Column(nullable = false)
    private Integer linkType;

    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean isFirst;
}
