package com.sztouyun.advertisingsystem.repository.system;

import com.sztouyun.advertisingsystem.model.system.AuditReasonConfig;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@CacheConfig(cacheNames = "auditReasonConfigs")
public interface AuditReasonConfigRepository extends BaseRepository<AuditReasonConfig> {
    boolean existsByName(String name);

    int countByParentId(String parentId);

    @CacheEvict(key = "#p0.getId()")
    @Override
    AuditReasonConfig save(AuditReasonConfig s);

    AuditReasonConfig findById(String id);

    @CacheEvict(key = "#p0")
    void deleteById(String id);

    @Modifying
    @Query(value = "UPDATE AuditReasonConfig t set t.count = (t.count+1) WHERE t.id in (:ids) ")
    void updateCountByIds(@Param("ids") List<String> ids);

}
