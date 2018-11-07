package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.system.AdvertisementSizeConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface AdvertisementSizeConfigRepository extends BaseRepository<AdvertisementSizeConfig> {
    AdvertisementSizeConfig findFirstByTerminalTypeAndAdvertisementPositionType(Integer terminalType,Integer advertisementPositionType);
}
