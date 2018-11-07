package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.partner.AdPositionStatisticsTimes;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerLineChartProfitTrendInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerLineChartStoreCountInfo;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerPieChartRequest;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.CooperationPartnerProfitStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.StoreRankInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.monitor.RequestStoreRankInfo;
import com.sztouyun.advertisingsystem.viewmodel.partner.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PartnerDailyStoreMonitorStatisticMapper {
    List<AdPositionStatisticsTimes> getAdPositionStatisticsTimes(DisplayTimesProportionRequest request);

    List<CooperationPartnerLineChartStoreCountInfo> requestStoreCountLineChartStatistic(CooperationPartnerPieChartRequest request);

    List<DisplayTimesViewModel> getPartnerDisplayTimesStatistic(DisplayTimesRequest request);

	List<RequestStoreRankInfo> getRequestStoreRankInfo(StoreRankInfoRequest request);

    List<StoreRequestRankingViewModel> getAllPartnerStoreNum(StoreRequestRankingRequest request);

    CooperationPartnerProfitStatisticViewModel profitStatistic(@Param("partnerId") String partnerId, @Param("yesterday") Date yesterday, @Param("week") Date week, @Param("month") Date month);

    List<CooperationPartnerLineChartProfitTrendInfo> partnerProfitTrendLineChart(@Param("cooperationPartnerId") String cooperationPartnerId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    void updatePartnerDailyProfitAmount(UpdateDailyProfitAmountViewModel viewModel);
}
