package com.sztouyun.advertisingsystem.service.statistic;

import com.sztouyun.advertisingsystem.mapper.AdvertisementPositionMapper;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.store.QStoreInfo;
import com.sztouyun.advertisingsystem.repository.store.StoreInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.index.AdPositionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.AdvertisementPositionAreaStatisticResult;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatistic;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatisticTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementPositionStatisticService extends BaseService {
    @Autowired
    private StoreInfoRepository storeInfoRepository;
    @Autowired
    private AdvertisementPositionMapper advertisementPositionMapper;
    private final QStoreInfo qStoreInfo = QStoreInfo.storeInfo;
    public List<DistributionStatisticDto> getAvailableAdPositionStatistics() {
        return advertisementPositionMapper.getAvailableAdPositionInfo();
    }

    public SummaryStatistic getTotalAvailableAdvertisementPosition() {
        List<DistributionStatisticDto> availableAdPositionInfo = advertisementPositionMapper.getAvailableAdPositionInfo();
        long total = 0L;
        for(DistributionStatisticDto distributionStatistic: availableAdPositionInfo) {
            total += distributionStatistic.getValue();
        }
        SummaryStatistic summaryStatistic = new SummaryStatistic();
        summaryStatistic.setTotal(total);
        summaryStatistic.setStatisticType(SummaryStatisticTypeEnum.AVAILABLE_ADVERTISEMENT_POSITION.getValue());
        return summaryStatistic;
    }



    public List<AdvertisementPositionAreaStatisticResult> getAdvertisementPositionDistribute() {
        return advertisementPositionMapper.getAdvertisementPositionDistribute();
    }

    /**
     * 广告位总数
     * @return
     */
    public Long getTotalAdPosition(){
        return advertisementPositionMapper.getTotalAdPosition();
    }
    /**
     * 以省分组的记录数
     * @return
     */
    private Long getAdPositionProvinceTotal(){
        return storeInfoRepository.count(q->q.select(qStoreInfo).from(qStoreInfo).where(qStoreInfo.deleted.eq(false).and(qStoreInfo.provinceId.isNotEmpty()).and(qStoreInfo.storeType.in(EnumUtils.toValueMap(StoreTypeEnum.class).keySet()))).groupBy(qStoreInfo.provinceId));
    }

    public List<AdPositionStatisticDto> getAdPositionInfoStatisticInfo(BasePageInfo pageInfo){
        return advertisementPositionMapper.getAdPositionInfoStatisticInfo(pageInfo);
    }

    public List<AdPositionStatisticDto> getAdPositionInfoStatisticInfoByProvince(String areaId){
        return advertisementPositionMapper.getAdPositionInfoStatisticInfoByProvince(areaId);
    }

    public Page<AdPositionStatisticDto> getAdPositionInfoStatisticPageInfo(BasePageInfo pageInfo){
        Long totalRecordSize=getAdPositionProvinceTotal();
        List<AdPositionStatisticDto> list = getAdPositionInfoStatisticInfo(pageInfo);
        Page<AdPositionStatisticDto> page=pageResult(list,new PageRequest(pageInfo.getPageIndex(),pageInfo.getPageSize()),totalRecordSize);
        return page;
    }

}
