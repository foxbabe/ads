package com.sztouyun.advertisingsystem.repository.system;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends BaseRepository<Organization> {
    Organization findById(String id);

    Organization findByName(String name);

    @Override
    default BooleanBuilder getAuthorizationFilter(){
        return AuthenticationService.getOrganizationAuthenticationFilter();
    }

    boolean existsByOwnerId(String ownerId);

    @Modifying
    @Query(value = "UPDATE Organization SET code = REPLACE(code, :oldCodePrefix, :newCodePrefix) WHERE code LIKE concat(:oldCodePrefix, '%') AND code <> :oldCodePrefix")
    void updateChildrenCode(@Param("oldCodePrefix")String oldCodePrefix, @Param("newCodePrefix")String newCodePrefix);
}
