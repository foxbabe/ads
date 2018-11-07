package com.sztouyun.advertisingsystem.service.system;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.AuditReasonConfig;
import com.sztouyun.advertisingsystem.model.system.QAuditReasonConfig;
import com.sztouyun.advertisingsystem.repository.system.AuditReasonConfigRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "auditReasonConfigs")
@Transactional
public class AuditReasonConfigService extends BaseService {

    @Autowired
    private AuditReasonConfigRepository auditReasonConfigRepository;

    private final QAuditReasonConfig qAuditReasonConfig = QAuditReasonConfig.auditReasonConfig;

    public void createAuditReasonConfig(AuditReasonConfig auditReasonConfig) {
        if (auditReasonConfigRepository.existsByName(auditReasonConfig.getName())) {
            throw new BusinessException("已经存在对应的配置");
        }
        auditReasonConfigRepository.save(auditReasonConfig);
    }

    public void createSubAuditReasonConfig(AuditReasonConfig auditReasonConfig) {
        if(auditReasonConfigRepository
                .exists(qAuditReasonConfig.name.eq(auditReasonConfig.getName())
                        .and(qAuditReasonConfig.parentId.eq(auditReasonConfig.getParentId())))){
            throw new BusinessException("已经存在对应的配置");
        }
        auditReasonConfigRepository.save(auditReasonConfig);
    }

    public Page<AuditReasonConfig> queryAuditReasonConfigList(Pageable pageable) {
        return  auditReasonConfigRepository.findAll(pageable);
    }

    public List<AuditReasonConfig> queryAuditReasonConfigByParentIdAndCountDesc(String parentId) {
        List<AuditReasonConfig> result = new ArrayList<>();
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAuditReasonConfig.parentId.eq(parentId));
        auditReasonConfigRepository.findAll(predicate,
                new QSort(qAuditReasonConfig.count.desc())).forEach(r->{
            result.add(r);
        });
        return result;
    }

    public List<AuditReasonConfig> findAllParent() {
        List<AuditReasonConfig> result = new ArrayList<>();
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAuditReasonConfig.parentId.isNull());
         auditReasonConfigRepository.findAll(predicate,
            new QSort(qAuditReasonConfig.updatedTime.desc())).forEach(r->{
            result.add(r);
        });
        return result;
    }

    public List<AuditReasonConfig> findAllParentByCount() {
        List<AuditReasonConfig> result = new ArrayList<>();
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAuditReasonConfig.parentId.isNull());
        auditReasonConfigRepository.findAll(predicate,
                new QSort(qAuditReasonConfig.count.desc())).forEach(r->{
            result.add(r);
        });
        return result;
    }

    public String getAuditReasonFromCache(String id){
        AuditReasonConfig auditReasonConfig=AuditReasonConfigById(id);
        if(auditReasonConfig==null)
            return "";
        return  auditReasonConfig.getName();
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public AuditReasonConfig AuditReasonConfigById(String id) {
        return  auditReasonConfigRepository.findById(id);
    }

    @Transactional
    public void deleteAuditReasonConfigById(String id) {
        AuditReasonConfig auditReasonConfig = auditReasonConfigRepository.findById(id);
        if(StringUtils.isEmpty(auditReasonConfig.getParentId())){
            if(auditReasonConfigRepository.countByParentId(id)>0){
                throw new BusinessException("有子分类不能删除");
            }
        }
        if(auditReasonConfig.getCount() > 0) {
            throw new BusinessException("存在业务关联，不能删除");
        }

        auditReasonConfigRepository.deleteById(id);
    }

    public void updateAuditReasonConfig(AuditReasonConfig auditReasonConfig) {
        boolean exists;
        if (auditReasonConfig.getParentId() == null) {
            exists = auditReasonConfigRepository
                .exists(qAuditReasonConfig.name.eq(auditReasonConfig.getName())
                    .and(qAuditReasonConfig.id.ne(auditReasonConfig.getId())));
        } else {
            exists = auditReasonConfigRepository
                .exists(qAuditReasonConfig.name.eq(auditReasonConfig.getName())
                    .and(qAuditReasonConfig.parentId.eq(auditReasonConfig.getParentId()))
                    .and(qAuditReasonConfig.id.ne(auditReasonConfig.getId())));
        }
        if (exists) {
            throw new BusinessException("已经存在对应的配置");
        }

        auditReasonConfigRepository.save(auditReasonConfig);
    }

    @Transactional
    public void updateAuditReasonConfigCount(List<String> ids) {
        auditReasonConfigRepository.updateCountByIds(ids);
    }

}
