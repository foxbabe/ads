package com.sztouyun.advertisingsystem.repository.partner;

import com.sztouyun.advertisingsystem.model.partner.PartnerAdSlotConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;

@CacheConfig(cacheNames = "partners")
public interface PartnerAdSlotConfigRepository extends BaseRepository<PartnerAdSlotConfig> {
    @CacheEvict(key = "'PrioritizedAdSlots_'+#p0.getPartnerId()")
    @Override
    PartnerAdSlotConfig save(PartnerAdSlotConfig config);
}
