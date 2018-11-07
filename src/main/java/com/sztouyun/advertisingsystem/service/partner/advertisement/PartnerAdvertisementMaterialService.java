package com.sztouyun.advertisingsystem.service.partner.advertisement;

import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.partner.advertisement.QPartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.repository.partner.PartnerAdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.PartnerAdvertisementMaterialListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;

@Service
public class PartnerAdvertisementMaterialService extends BaseService {

    @Autowired
    private PartnerAdvertisementMaterialRepository partnerAdvertisementMaterialRepository;

    private static final QPartnerAdvertisementMaterial qPartnerAdvertisementMaterial = QPartnerAdvertisementMaterial.partnerAdvertisementMaterial;

    public Page<PartnerAdvertisementMaterial> queryPartnerAdvertisementMaterialList(PartnerAdvertisementMaterialListRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(),
                new QSort(qPartnerAdvertisementMaterial.id.asc()));
        return partnerAdvertisementMaterialRepository.findAll(q -> q.selectFrom(qPartnerAdvertisementMaterial).where(qPartnerAdvertisementMaterial.partnerAdvertisementId.eq(request.getPartnerAdvertisementId())).groupBy(qPartnerAdvertisementMaterial.md5),
                pageable);
    }
}
