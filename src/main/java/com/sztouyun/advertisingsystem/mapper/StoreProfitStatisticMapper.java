package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreProfitStatisticMapper {
    StoreProfitStatisticViewModel getAdvertisementStatistic(String storeId);

    Double sumProfitAmount(String storeId);

    ProfitOverview getProfitOverviewInfo();

    List<ProfitSettledInfo> getProfitOverviewStatistic(ProfitOverviewRequest profitOverviewRequest);

    void deleteStoreProfitStatistic(List<String> storeProfitStatisticIds);

    List<Area> getAreaBySettledStoreProfitId(String settledStoreProfitId);

    void supplementStoreProfitStatistic(SupplementaryProfitShareRequest request);

    void updatePeriodStoreProfitId(PeriodStoreProfitShareCalculateViewModel request);

    void settleStoreProfitStatistic(String id);

    void settlePeriodStoreProfitStatistic(String id);

    void cancelSettlePeriodStoreProfitStatistic(String id);

    void clearPeriodStoreProfitId(List<String> periodStoreProfitShareStatisticIds);

    List<Area> getAreasWithPeriodProfit();

    void supplementStoreProfitQualifiedInfo(SupplementProfitInfo info);

    void supplementStoreProfitAmountInfo(SupplementProfitInfo info);

    void supplementStoreProfitExtensionOrderInfo(SupplementProfitInfo info);

    void supplementStoreProfitOrderStandardIs(SupplementProfitInfo info);
}