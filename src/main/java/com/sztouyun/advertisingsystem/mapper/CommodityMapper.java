package com.sztouyun.advertisingsystem.mapper;


import com.sztouyun.advertisingsystem.viewmodel.commodity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommodityMapper {

    List<CommodityOptionViewModel> queryCommodityOptionInfo(CommodityOptionRequest request);

    List<CommodityTypeOptionViewModel> queryCommodityTypeOptionInfo(CommodityTypeOptionRequest request);

    List<SupplierOptionViewModel> queryStoreSupplierOptionInfo(String storeId);

    Long queryCommodityOptionInfoCount(CommodityOptionRequest request);

    Long queryCommodityTypeOptionInfoCount(CommodityTypeOptionRequest request);

    List<CommodityOptionViewModel> queryStoreCommodityInfo(StoreCommodityInfoRequest request);

    Long queryStoreCommodityInfoCount(StoreCommodityInfoRequest request);

    List<StoreCommodityTypeOptionViewModel> queryStoreCommodityTypeOptionInfo(StoreCommodityTypeInfoRequest request);
}
