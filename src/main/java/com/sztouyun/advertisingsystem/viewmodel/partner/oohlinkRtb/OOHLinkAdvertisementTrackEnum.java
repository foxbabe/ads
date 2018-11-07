package com.sztouyun.advertisingsystem.viewmodel.partner.oohlinkRtb;


import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementTrackEnum;

public enum OOHLinkAdvertisementTrackEnum implements EnumMessage<Integer> {

    StartPlay(0, "开始播放", PartnerAdvertisementTrackEnum.StartPlay),
    EndPlay(1, "结束播放", PartnerAdvertisementTrackEnum.EndPlay);

    private Integer value;
    private String displayName;
    private PartnerAdvertisementTrackEnum partnerAdvertisementTrackEnum;

    OOHLinkAdvertisementTrackEnum(Integer value, String displayName, PartnerAdvertisementTrackEnum partnerAdvertisementTrackEnum) {
        this.value = value;
        this.displayName = displayName;
        this.partnerAdvertisementTrackEnum = partnerAdvertisementTrackEnum;

    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public PartnerAdvertisementTrackEnum getPartnerAdvertisementTrackEnum() {
        return partnerAdvertisementTrackEnum;
    }
}