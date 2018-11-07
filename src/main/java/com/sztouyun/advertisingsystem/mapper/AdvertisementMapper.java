package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.DeliveryAdvertisementStatistic;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementActivityInStoreRequest;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.monitor.AdvertisementStoreInfoRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdvertisementMapper {

    default String getUserAuthenticationFilterSql(){
        return AuthenticationService.getUserAuthenticationFilterSql("c.owner_id");
    }

    List<AdvertisementMaterialPositionViewModel> getAdvertisementMaterialPosition(String contractId);

    List<Advertisement> getAdvertisementsByStore(AdvertisementActivityInStoreRequest request);
    List<DeliveryAdvertisementStatistic> getDeliveryAdvertisementStatistic();

    List<AdvertisementTaskViewModel> getAdvertisementTaskList(AdvertisementTaskRequest request);

    Long getAdvertisementTaskListCount(AdvertisementTaskRequest request);

    AdvertisementTaskCountStatisticInfo getAdvertisementTaskStatusStatistics(AdvertisementTaskStatusStatisticsRequest request);

    AdvertisementTaskDetailViewModel getAdvertisementTaskDetail(String advertisementId);

    List<AdvertisementTaskDetailListViewModel> getAdvertisementTaskDetailList(AdvertisementTaskDetailListRequest request);

    Long getAdvertisementTaskDetailListCount(AdvertisementTaskDetailListRequest request);

    StoreTaskDetailViewModel getStoreTaskDetail(String taskId);

    long getTotalStoreCount(String advertisementId);
	List<StoreAdvertisementInfo> getStoreAdvertisements(AdvertisementStoreInfoRequest request);

    Long getStoreAdvertisementCount(AdvertisementStoreInfoRequest request);

    List<Advertisement> getDeliveryAdvertisement(AutoFinishAdvertisementInfo info);

    void updateAdvertisementShareAmount(List<ShareAmountInfo> shareAmountInfos );

    void updateSettlementInfo(@Param("advertisementSettlementId")String advertisementSettlementId,@Param("advertisementProfitMode") Integer advertisementProfitMode,@Param("settledUserId")String settledUserId);
}
