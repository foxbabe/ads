package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.common.BaseAuthenticationInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageOffsetRequest;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerOverViewStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.customer.IndustryProfitRankingViewModel;
import com.sztouyun.advertisingsystem.viewmodel.index.CustomerAreaStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerAreaStatisticResult;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerCityAreaStatistic;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface CustomerMapper {
    List<CustomerAreaStatisticDto> getCustomerAreaStatistic(BaseAuthenticationInfo baseAuthenticationInfo);

    default String getUserAuthenticationFilterSql(){
        return AuthenticationService.getUserAuthenticationFilterSql("customer.owner_id");
    }

    List<CustomerAreaStatisticResult> getCustomerProvinceAreaDetailStatistic(MyPageOffsetRequest pageInfoRequest);

    List<CustomerAreaStatisticResult> getCustomerCityAreaDetailStatistic(CustomerCityAreaStatistic customerCityAreaStatistic);

    Long getCustomerProvinceAreaDetailStatisticCount(BaseAuthenticationInfo baseAuthenticationInfo);

    CustomerOverViewStatisticViewModel customerOverViewStatistic();

    double getCustomerTotalProfit();

    Long getCustomerCount(Date date);

    List<IndustryProfitRankingViewModel> industryProfitRanking();
}
