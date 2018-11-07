package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.QArea;
import com.sztouyun.advertisingsystem.repository.system.AreaRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@CacheConfig(cacheNames = "areas")
public class AreaCacheService extends BaseService {
    @Autowired
    private AreaRepository areaRepository;

    private final QArea qArea = QArea.area;

    @Cacheable(key = "#p0",condition="#p0!=null")
    public Area getAreaFromCache(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return null;
        return areaRepository.findOne(qArea.id.eq(areaId));
    }

    @Cacheable(key = "'AllAreaNames'")
    public Map<String,String> getAllAreaNames() {
        return Linq4j.asEnumerable(areaRepository.findAll()).toMap(a->a.getId(), a->a.getName());
    }
}
