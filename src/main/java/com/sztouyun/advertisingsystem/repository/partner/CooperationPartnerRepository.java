package com.sztouyun.advertisingsystem.repository.partner;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.partner.QCooperationPartner;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "partners")
public interface CooperationPartnerRepository extends BaseRepository<CooperationPartner> {
    @CacheEvict(key = "#p0.getId()")
    @Override
    CooperationPartner save(CooperationPartner cooperationPartner);

    @CacheEvict(key = "#p0")
    @Override
    void delete(String id);

    @Cacheable(key = "#p0",condition="#p0!=null")
    CooperationPartner findById(String id);

    boolean existsByName(String name);

    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getUserAuthenticationFilter(QCooperationPartner.cooperationPartner.ownerId);
    }

    boolean existsByOwnerId(String userId);

    boolean existsByCreatorId(String userId);
}
