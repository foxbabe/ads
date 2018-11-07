package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.system.MaterialLinkTypeEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementMaterialUrlStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateAdvertisementMaterialUrlStepOperation implements IActionOperation<Advertisement> {

    @Value(value = "${advertisement.qrCode.phone.url}")
    private String qrCodePhoneUrl;

    @Autowired
    private AdvertisementMaterialRepository advertisementMaterialRepository;
    @Autowired
    private AdvertisementMaterialUrlStepRepository advertisementMaterialUrlStepRepository;

    private static final QAdvertisementMaterial qAdvertisementMaterial = QAdvertisementMaterial.advertisementMaterial;

    @Override
    public void operateAction(Advertisement advertisement) {
        List<AdvertisementMaterialUrlStep> stepList = new ArrayList<>();
        List<AdvertisementMaterial> materialList = advertisementMaterialRepository.findAll(q -> q.selectFrom(qAdvertisementMaterial)
                .where(qAdvertisementMaterial.advertisementId.eq(advertisement.getId()).and(qAdvertisementMaterial.materialQRCodeUrl.isNotNull().or(qAdvertisementMaterial.materialClickUrl.isNotNull()))));

        if (!CollectionUtils.isEmpty(materialList)) {
            materialList.forEach(m -> {
                //生成Url点击步骤
                if(!StringUtils.isEmpty(m.getMaterialClickUrl())){
                    AdvertisementMaterialUrlStep partnerStepInfo = new AdvertisementMaterialUrlStep();
                    partnerStepInfo.setUrl(m.getMaterialClickUrl());
                    partnerStepInfo.setStepType(AdvertisementMaterialUrlStepTypeEnum.Promotion.getValue());
                    partnerStepInfo.setAdvertisementMaterialId(m.getId());
                    partnerStepInfo.setLinkType(MaterialLinkTypeEnum.MaterialClick.getValue());
                    partnerStepInfo.setFirst(true);
                    stepList.add(partnerStepInfo);
                }
                //生成二维码步骤
                if(!StringUtils.isEmpty(m.getMaterialQRCodeUrl())){
                    if (m.getIsNotePhoneNumber()) {//记录手机号码
                        AdvertisementMaterialUrlStep internalStep = new AdvertisementMaterialUrlStep();
                        internalStep.setUrl(qrCodePhoneUrl);
                        internalStep.setStepType(AdvertisementMaterialUrlStepTypeEnum.FillPhone.getValue());
                        internalStep.setAdvertisementMaterialId(m.getId());
                        internalStep.setLinkType(MaterialLinkTypeEnum.MaterialQRCode.getValue());
                        internalStep.setRequiredParams(String.valueOf(AdvertisementMaterialUrlParamEnum.Phone.getValue()));
                        internalStep.setFirst(true);
                        stepList.add(internalStep);

                        AdvertisementMaterialUrlStep partnerStepInfo = new AdvertisementMaterialUrlStep();
                        partnerStepInfo.setUrl(m.getMaterialQRCodeUrl());
                        partnerStepInfo.setStepType(AdvertisementMaterialUrlStepTypeEnum.Promotion.getValue());
                        partnerStepInfo.setAdvertisementMaterialId(m.getId());
                        partnerStepInfo.setLinkType(MaterialLinkTypeEnum.MaterialQRCode.getValue());
                        internalStep.setNextStepId(partnerStepInfo.getId());
                        stepList.add(partnerStepInfo);
                    } else {//不记录手机号码
                        AdvertisementMaterialUrlStep partnerStepInfo = new AdvertisementMaterialUrlStep();
                        partnerStepInfo.setUrl(m.getMaterialQRCodeUrl());
                        partnerStepInfo.setStepType(AdvertisementMaterialUrlStepTypeEnum.Promotion.getValue());
                        partnerStepInfo.setAdvertisementMaterialId(m.getId());
                        partnerStepInfo.setLinkType(MaterialLinkTypeEnum.MaterialQRCode.getValue());
                        partnerStepInfo.setFirst(true);
                        stepList.add(partnerStepInfo);
                    }
                }
            });
            advertisementMaterialUrlStepRepository.save(stepList);
        }
    }
}
