package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.index.AdPositionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.AdvertisementPositionAreaStatisticResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdvertisementPositionMapper {
    List<DistributionStatisticDto> getAvailableAdPositionInfo();

    Long getAllAvailableAdPosition();

    List<AdvertisementPositionAreaStatisticResult> getAdvertisementPositionDistribute();

    List<AdPositionStatisticDto> getAdPositionInfoStatisticInfo(BasePageInfo pageInfo);

    List<AdPositionStatisticDto> getAdPositionInfoStatisticInfoByProvince(String areaId );

    Long getTotalAdPosition();

    List<DistributionStatisticDto> getAvailableStoreCountWithStoreType(@Param("contractId")String contractId);

    List<DistributionStatisticDto> getStoreCount();

}
