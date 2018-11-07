package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.model.BasePartnerModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PartnerMaterial extends BasePartnerModel {
    @Column(nullable = false)
    private String materialCode;

    @Column(nullable = false)
    private Integer materialType;

    @Column(nullable = false)
    private Integer materialStatus = PartnerMaterialStatusEnum.PendingAuditing.getValue();

    @Column(nullable = false)
    private Integer advertisementPositionCategory;

    @Column(nullable = false)
    private Integer advertisementPositionType;

    @Column(nullable = false)
    private Integer terminalType;

    @Column(length = 50)
    private String materialSpecification = "";

    @Column(length = 50)
    private String materialSize = "";

    @Column(nullable = false,length = 1024)
    private String originalUrl;

    @Column( nullable = false,length = 255)
    private String url = "";

    @Column(name = "auditor_id",updatable = true,length = 36)
    private String auditorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id",insertable = false,updatable = false)
    private User auditor;

    @Column
    private String md5;

    @Column
    private Integer duration;

    @Column
    private Date auditTime;
    public void setAdvertisementPositionCategory(Integer advertisementPositionCategory) {
        AdvertisementPositionCategoryEnum advertisementPositionCategoryEnum = EnumUtils.toEnum(advertisementPositionCategory,AdvertisementPositionCategoryEnum.class);
        setAdvertisementPositionType(advertisementPositionCategoryEnum.getAdvertisementPositionType().getValue());
        setTerminalType(advertisementPositionCategoryEnum.getTerminalType().getValue());
        this.advertisementPositionCategory = advertisementPositionCategory;
    }

    private void setAdvertisementPositionType(Integer advertisementPositionType) {
        this.advertisementPositionType = advertisementPositionType;
    }

    private void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }
}
