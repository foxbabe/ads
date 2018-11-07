package com.sztouyun.advertisingsystem.service.system;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.model.system.QArea;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.system.AreaRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.store.CustomerStoreInfoAreaSelectedRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoAreaSelectedViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaStoreInfo;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by szty on 2017/7/25.
 */
@Service
public class AreaService extends BaseService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private AreaCacheService areaCacheService;

    private final QArea qArea = QArea.area;

    private final QCustomer qCustomer = QCustomer.customer;

    @Value("${com.sztouyun.advertisingsystem.model.system.Area.municipality}")
    private String municipality;

    public List<Area> getExistsCustomerAreas() {
        return customerRepository.findAllAuthorized(queryFactory -> queryFactory.selectDistinct(qArea)
                .from(qCustomer, qArea).where(qCustomer.cityId.eq(qArea.id)
                        .or(qCustomer.provinceId.eq(qArea.id)).or(qCustomer.regionId.eq(qArea.id))));
    }

    public List<AreaStoreInfo> getAreaByContainStoreInfo(StoreInfoAreaSelectedViewModel queryReqquest) {
        return storeInfoMapper.getAreaByContainStoreInfo(queryReqquest);
    }

    public List<Area> getAreaInfo(StoreInfoAreaSelectedViewModel viewModel) {
        if(viewModel!=null&&viewModel.getContractId()!=null&&!contractRepository.exists(viewModel.getContractId()))
            throw new BusinessException("合同数据不存在");

        return storeInfoMapper.getAreaInfo(viewModel);
    }

    public Iterable<Area> getSubAreas(String parentId){
        return  areaRepository.findAll(qArea.parentId.eq(parentId));
    }

    public Map<String,String> getAllAreaNames() {
        return areaCacheService.getAllAreaNames();
    }

    public Area getAreaByCode(String code) {
        if (StringUtils.isEmpty(code))
            return null;
        return areaRepository.findOne(qArea.code.eq(code));
    }

    //在同一个类中调用方法，导致缓存不生效的,所以加上areaCacheService
    // http://www.bkjia.com/Javabc/1313710.html
    public Area getAreaFromCache(String areaId) {
        return areaCacheService.getAreaFromCache(areaId);
    }

    public String getAreaNameFromCache(String areaId) {
        if (StringUtils.isEmpty(areaId))
            return "";
        Area area = getAreaFromCache(areaId);
        if (area == null)
            return "";
        return area.getName();
    }

    public String getParentAreaNameFromCache(String areaId){
        if (StringUtils.isEmpty(areaId))
            return "";
        Area area = getAreaFromCache(areaId);
        if (area == null)
            return "";
        return getAreaNameFromCache(area.getParentId());
    }

    public boolean isMunicipality(String areaId) {
        Area area = getAreaFromCache(areaId);
        return area != null && municipality.contains(area.getCode());
    }

    public boolean isMunicipalityCode(String areaCode){
        if(StringUtils.isEmpty(areaCode))
            return false;

        return municipality.contains(areaCode);
    }

    public List<String> filterAreaIdsByLevel(List<String> areaIds,int level){
        return areaRepository.findAll(q->q.select(qArea.id).from(qArea).where(qArea.level.eq(level).and(qArea.id.in(areaIds))));
    }

    public List<AreaStoreInfo> getCustomerStoreArea(CustomerStoreInfoAreaSelectedRequest queryRequest) {
        return storeInfoMapper.getCustomerStoreArea(queryRequest);
    }
}
