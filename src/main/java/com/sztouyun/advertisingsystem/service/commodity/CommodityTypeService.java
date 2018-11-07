package com.sztouyun.advertisingsystem.service.commodity;

import com.sztouyun.advertisingsystem.mapper.CommodityMapper;
import com.sztouyun.advertisingsystem.model.commodity.CommodityType;
import com.sztouyun.advertisingsystem.repository.commodity.CommodityTypeRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.commodity.StoreCommodityTypeInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.commodity.StoreCommodityTypeOptionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityTypeService extends BaseService {

    @Autowired
    private CommodityTypeRepository commodityTypeRepository;

    @Autowired
    private CommodityMapper commodityMapper;

    public void update(CommodityType commodityType){
        commodityTypeRepository.save(commodityType);
    }

    public void delete(Long id){
        commodityTypeRepository.delete(id);
    }

    public List<StoreCommodityTypeOptionViewModel> queryStoreCommodityTypeOptionInfo(StoreCommodityTypeInfoRequest request) {
        return commodityMapper.queryStoreCommodityTypeOptionInfo(request);
    }
}
