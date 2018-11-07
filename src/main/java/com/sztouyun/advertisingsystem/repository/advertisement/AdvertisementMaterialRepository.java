package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementMaterial;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface AdvertisementMaterialRepository extends BaseRepository<AdvertisementMaterial> {
    void deleteByAdvertisementId(String advertisementId);
}
