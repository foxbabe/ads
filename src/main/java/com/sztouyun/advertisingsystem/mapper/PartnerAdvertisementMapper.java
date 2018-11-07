package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.monitor.PartnerDailyStoreMonitorStatistic;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.IdDatePageRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticInfo;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorCountStatisticRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.PartnerAdvertisementMonitorListRequest;
import com.sztouyun.advertisingsystem.viewmodel.partner.StoreRequestPartnerInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.UpdatedAdSlotPriorityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartnerAdvertisementMapper {

    default String getUserAuthenticationFilterSql(){
        return AuthenticationService.getUserAuthenticationFilterSql("cp.owner_id");
    }

    List<PartnerAdvertisementListViewModel> queryPartnerAdvertisementList(PartnerAdvertisementListRequest request);

    Long queryPartnerAdvertisementListCount(PartnerAdvertisementListRequest request);

    PartnerAdvertisementCountStatisticInfo getPartnerAdvertisementStatusStatistics(PartnerAdvertisementCountStatisticRequest request);

    List<PartnerAdvertisementListViewModel> queryPartnerAdvertisementMonitors(PartnerAdvertisementMonitorListRequest request);

    Long queryPartnerAdvertisementMonitorsCount(PartnerAdvertisementMonitorListRequest request);

    PartnerAdvertisementMonitorCountStatisticInfo getPartnerAdvertisementMonitorStatusStatistics(PartnerAdvertisementMonitorCountStatisticRequest request);

    void savePartnerAdvertisements(List<PartnerAdvertisement> partnerAdvertisements );

    void updatePartnerAdvertisementMaterialType(List<String> partnerAdvertisementIds );

    void savePartnerAdvertisementMaterials(List<PartnerAdvertisementMaterial> partnerAdvertisementMaterials );

    void updatePartnerRequestStatistic(List<PartnerDailyStoreMonitorStatistic> partnerDailyStoreMonitorStatistics );

    void updatePartnerAdvertisementDisplayStatistic(List<PartnerDailyStoreMonitorStatistic> partnerDailyStoreMonitorStatistics );

    void finishPartnerAdvertisement(List<String> partnerAdvertisementIds);

    void updatePriority(@Param("startPriority") Integer startPriority,@Param("endPriority") Integer endPriority,@Param("movedStep") Integer movedStep);

    List<CooperationPartnerChartRequestTimesViewModel> partnerAdvertisementRequestLogStatistic(CooperationPartnerChartBaseRequest request);

    List<BasePartnerInfo> getStorePartners(List<String> partnerIds);

    void reCalcPartnerAdvertisementDisplayStatistic(List<DailyPartnerAdvertisementStoreDisplayInfo> partnerDailyStoreMonitorStatistics );

    void updateAdSlotPriority(UpdatedAdSlotPriorityInfo updatedAdSlotPriorityInfo);

    List<StoreRequestPartnerInfo> getStoreRequestPartnerInfo(IdDatePageRequest request);

    Long getPartnerConfigStoreCount(String id);
}
