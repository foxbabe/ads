package com.sztouyun.advertisingsystem.model.mongodb;

import com.sztouyun.advertisingsystem.model.partner.advertisement.store.PartnerAdvertisementDeliveryRecordStatusEnum;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class PartnerAdvertisementDeliveryRecord{
    private String id= UUIDUtils.generateBase58UUID();

    private String requestId;

    private Integer status= PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue();

    /**
     * 第三方广告ID
     */
    private String thirdPartId;

    /**
     * 合作方广告ID （合作方类型+'#'+thirdPartId）
     */
    private String partnerAdvertisementId;

    /**
     * 门店ID
     */
    private String storeId;

    /**
     * 广告位置, AdvertisementPositionCategoryEnum
     */
    private Integer advertisementPositionCategory;

    /**
     * 监播地址
     */
    private String monitorUrls;

    /**
     * 请求时间
     */
    private Long requestTime;

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
        this.requestDate = new DateTime(requestTime).withTimeAtStartOfDay().getMillis();
    }

    /**
     * 请求日期（天）
     */
    private Long requestDate;

    /**
     * 有效的完成时间
     */
    private Long effectiveFinishTime;

    /**
     * 完成播放时间
     */
    private Long finishTime;

    /**
     * 下架时间
     */
    private Long takeOffTime;

    /**
     * 下架操作人
     */
    private String operator;


    /**
     * 下架备注
     */
    private String remark;

    /**
     * 是否有效
     */
    private boolean valid = false;

    /**
     * 合作方ID
     */
    private String partnerId;

    /**
     * 素材类型
     */
    private Integer materialType;

    /**
     * 素材规格
     */
    private String materialSpecification;

    /**
     * 素材尺寸
     */
    private String materialSize;

    /**
     * 素材url
     */
    private String originalUrl;

    /**
     * 素材url md5
     */
    private String md5;

    /**
     * 播放时长
     */
    private Integer duration;

    /**
     * 扩展属性
     */
    private Object extend;

    /**
     * 开始播放时间
     */
    private Long startTime;

    /**
     * 开始播放的回调监播url, 多个url用"," 隔开
     */
    private String startDisplayMonitorUrls;
}
