package com.sztouyun.advertisingsystem.repository.material;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.material.PartnerMaterialStatusEnum;
import com.sztouyun.advertisingsystem.model.material.QPartnerMaterial;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;

public interface PartnerMaterialRepository extends BaseRepository<PartnerMaterial> {
    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getUserAuthenticationFilter(QPartnerMaterial.partnerMaterial.partner.ownerId);
    }
}
