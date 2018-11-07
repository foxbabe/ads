package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.model.system.DataDict;
import com.sztouyun.advertisingsystem.model.system.DataDictTypeEnum;
import com.sztouyun.advertisingsystem.repository.system.DataDictRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "dataDict")
@Service
public class DataDictService extends BaseService{
    @Autowired
    private DataDictRepository dataDictRepository;

    @Cacheable(key = "#p0",condition="#p0!=null")
    public DataDict getDataDictFromCache(String key){
        return dataDictRepository.findOne(key);
    }

    @Cacheable(key = "'DataDictTypeEnum_'+#p0.value",condition="#p0!=null")
    public List<DataDict> findAllByType(DataDictTypeEnum dataDictTypeEnum) {
        return dataDictRepository.findAllByTypeOrderById(dataDictTypeEnum.getValue());
    }
}
