package com.sztouyun.advertisingsystem.service.commodity;

import com.sztouyun.advertisingsystem.mapper.CommodityMapper;
import com.sztouyun.advertisingsystem.model.commodity.Commodity;
import com.sztouyun.advertisingsystem.repository.commodity.CommodityRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.commodity.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityService extends BaseService {

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private CommodityRepository commodityRepository;

    public Page<CommodityOptionViewModel> queryCommodityOptionInfo(CommodityOptionRequest request) {
        return pageResult(commodityMapper.queryCommodityOptionInfo(request),new MyPageRequest(request.getPageIndex(), request.getPageSize()),commodityMapper.queryCommodityOptionInfoCount(request));

    }

    public Page<CommodityTypeOptionViewModel> queryCommodityTypeOptionInfo(CommodityTypeOptionRequest request) {
        return pageResult(commodityMapper.queryCommodityTypeOptionInfo(request),new MyPageRequest(request.getPageIndex(), request.getPageSize()),commodityMapper.queryCommodityTypeOptionInfoCount(request));
    }

    public List<SupplierOptionViewModel> queryStoreSupplierOptionInfo(String storeId) {
        return commodityMapper.queryStoreSupplierOptionInfo(storeId);
    }

    public void update(Commodity commodity){
        commodityRepository.save(commodity);
    }

    public void delete(Long id){
        commodityRepository.delete(id);
    }

    public Page<CommodityOptionViewModel> queryStoreCommodityInfo(StoreCommodityInfoRequest request) {
        return pageResult(commodityMapper.queryStoreCommodityInfo(request),new MyPageRequest(request.getPageIndex(), request.getPageSize()),commodityMapper.queryStoreCommodityInfoCount(request));
    }
}
