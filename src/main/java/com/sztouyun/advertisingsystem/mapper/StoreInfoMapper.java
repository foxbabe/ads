package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.contract.StoreCategoryStandardDto;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementSettlementSelectAllRequest;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.*;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaStoreInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

@Mapper
public interface StoreInfoMapper {
    List<StoreInfoQueryResult> getStoreInfo(StoreInfoQueryRequest queryRequest);

    Long getStoreInfoCount(StoreInfoQueryRequest queryRequest);

    Long oneKeyInsertStoreToContract(OneKeyInsertStoreInfoRequest oneKeyInsertStoreInfoRequest);

    void calculateStoreTypeByConfig(List<StoreCategoryStandardDto> list);

    List<ContractStoreCheckTreeViewModel> getContractStoreAreaId(StoreInfoAreaSelectedViewModel storeInfoAreaSelectedViewModel);

    Boolean existsTestContractStore(StoreInfoAreaSelectedViewModel storeInfoAreaSelectedViewModel);

    List<AreaStoreInfo> getAreaByContainStoreInfo(StoreInfoAreaSelectedViewModel queryRequest);

    List<ContractStoreInfo> queryStoreList(ContractStoreQueryRequest contractStoreQueryRequest);

    Long queryStoreListCount(ContractStoreQueryRequest contractStoreQueryRequest);

    List<Area> getAreaInfo(StoreInfoAreaSelectedViewModel queryRequest);

    Boolean hasAvailableAdPosition(String storeId);

    List<StoreInfoStatisticViewModel> getStoreInfoStatistic(StoreInfoStatisticQueryRequest request);

    Long getStoreInfoStatisticCount(StoreInfoStatisticQueryRequest request);

    Long storeInfoUseCountStatistic();

    /**
     * 1.选择门店 2.取消选择门店 3.一键选择门店 4.清空门店 5.合同执行终止、执行完成 6.合同导入选点记录
     * 以上场景维护门店占用数量
     */
    void increaseStoreUsedCount(String storeId);

    void decreaseStoreUsedCount(String storeId);

    void decreaseContractStoreUsedCount(String contractId);

    @Async
    void recalculateContractStoreUsedCount(String contractId);

    StoreAdvertisementInfoViewModel getStoreAdvertisementInfo(String storeId);

    List<Area> getUnsettledAreaInfo(String settledStoreProfitId);

    List<Area> getAdvertisementAreaInfo(String advertisementId);

    List<String> getCheckedUnsettledAreaIds(String settledStoreProfitId);

    List<StoreProfitBean> getUnsettledStoreProfitList(UnsettledPeriodStoreProfitListRequest request);

    List<StoreProfitBean> getUnsettledStoreProfitListWithSettleMonth(UnsettledPeriodStoreProfitListRequest request);

    Long getUnsettledStoreProfitCount(UnsettledPeriodStoreProfitListRequest request);

    Double getUnsettledStoreProfitAmount(UnsettledPeriodStoreProfitListRequest request);

    void updateUnsettledStoreProfit(UnsettledPeriodStoreProfitListRequest request);

    List<StoreProfitBean> getSubPeriodStoreProfitPageList(SubPeriodStoreProfitListRequest request);

    List<String> getStoreIdByAdvertisement(CalculateProfitByAdvertisementPageRequest request);

    List<StoreInfo> getStoreInfoByAdvertisement(StoreInfoByAdvertisementIdInfo info);

    List<StorePlacementViewModel> getStoreInfoByCoordinate(StoreInfoQueryRequest info);

    List<AreaStoreInfo> getCustomerStoreArea(CustomerStoreInfoAreaSelectedRequest queryRequest);

    List<StoreInfoQueryResult> getCustomerStoreInfo(CustomerStoreInfoQueryRequest queryRequest);

    Long getCustomerStoreInfoCount(CustomerStoreInfoQueryRequest queryRequest);

    PrimaryStoreInfoViewModel queryStoreInfo(PrimaryStoreInfoRequest request);

    List<CustomerStorePlacementViewModel> getCustomerStoreInfoByCoordinate(CustomerStoreInfoQueryRequest info);

    Long getAllAvailableStoreCount(String customerStorePlanID);

    Long getStoreCountInAbnormal(String customerStorePlanID);

    List<StoreInfoAreaStatistic> getStoreInfoUsedCountStatisticByCity(StoreAreaStatisticRequest request);

    AdvertisementStoreInfoStatisticViewModel getAdvertisementStatisticByStoreId(String storeId);

    @Async
    void updateStoreIsUsing(String contractId);

    List<StoreNameInfo> getStoreNameInfo(List<String> storeIds);

    StoreNumStatistics getStoreNumStatisticsByNow();

    List<String> getAdvertisementStoreIds(AdvertisementSettlementSelectAllRequest request);

    List<StoreValidInfo> getStoreValidInfo(StoreValidInfoRequest request);

    Boolean existStoreInAbnormal(String advertisementId);

    Long getTotalStoreCount(@Param("storeType") Integer storeType, @Param("contractId") String contractId);
}
