package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.model.contract.CustomerStoreContractUseNum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractSelectedStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.customer.CityProfitRankingViewModel;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerProfitRankingViewModel;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.CustomerStorePlanChooseRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper {
    List<Area> getCitiesByContactId(String contractId);

    void clearDeletedContractStore(String contractId);

    void addContractStoreByCustomerStorePlanId(CustomerStorePlanChooseRequest request);

    void deleteByContractId(String contractId);

    List<CustomerStoreContractUseNum> getAllCustomerStoreContractUseNum();

    List<Area> getAreas(String contractId);

    void deleteByContractIdAndStoreType(ContractSelectedStoreInfo contractSelectedStoreInfo);

    List<CustomerProfitRankingViewModel> getCustomerProfitRanking();

    double getExecutedContractTotalProfit();

    List<CityProfitRankingViewModel> getCityProfitRanking();
}
