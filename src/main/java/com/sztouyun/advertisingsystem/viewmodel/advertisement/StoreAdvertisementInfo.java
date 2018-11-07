package com.sztouyun.advertisingsystem.viewmodel.advertisement;

import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/5/31.
 */
@Data
public class StoreAdvertisementInfo {
    /**
     * 广告ID
     */
    private String id;

    /**
     * 广告名称
     */
    private String advertisementName;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 广告类型 {@link com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum}
     */
    private Integer advertisementType;

    /**
     * 是否开启分成
     */
    private Boolean enableProfitShare;

    /**
     * 广告状态 {@link com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum}
     */
    private Integer advertisementStatus;

    /**
     * 门店是否激活
     */
    private Boolean active;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建者ID
     */
    private String creatorId;

    /**
     * 维护者ID
     */
    private String ownerId;

    public String getAdvertisementTypeName() {
        return EnumUtils.getDisplayName(advertisementType, MaterialTypeEnum.class);
    }

    public String getAdvertisementStatusName() {
        return EnumUtils.getDisplayName(advertisementStatus, AdvertisementStatusEnum.class);
    }
}
