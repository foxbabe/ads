package com.sztouyun.advertisingsystem.repository.adProfitShare;

import com.sztouyun.advertisingsystem.model.adProfitShare.PeriodStoreProfitStatistic;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PeriodStoreProfitStatisticRepository extends BaseRepository<PeriodStoreProfitStatistic> {

    @Modifying
    @Query(value = "UPDATE PeriodStoreProfitStatistic set settledStoreProfitId ='' WHERE settledStoreProfitId = ?1")
    void deleteBySettledStoreProfitId(String settledStoreProfitId);

}
