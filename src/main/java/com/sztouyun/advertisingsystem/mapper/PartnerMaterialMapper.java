package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.material.PartnerMaterial;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartnerMaterialMapper {

    void updatePartnerMaterialUrl(PartnerMaterial partnerMaterial);

    void batchUpdatePartnerMaterialUrl(List<PartnerAdvertisementMaterial> partnerMaterialList);

}
