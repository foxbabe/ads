package com.sztouyun.advertisingsystem.mapper;

import com.sztouyun.advertisingsystem.utils.excel.LoadInfo;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.*;
import com.sztouyun.advertisingsystem.viewmodel.store.CustomerStoreInfoAreaSelectedRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.OneKeyInsertCustomerStoreRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoStatisticViewModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerStorePlanMapper {
    List<String> getCustomerCheckedStoreAreaIds(CustomerStoreInfoAreaSelectedRequest request);

    Long getCityCount(String customerStorePlanId);

    void deleteCustomerStorePlanDetail(DeleteCustomerStorePlanInfo info);

    void cancelTempCustomerStoreInfo(String tempCustomerStorePlanId);

    void emptyCustomerStorePlan(String customerStorePlanId);

    void batchChooseCustomerStoreInfo(OneKeyInsertCustomerStoreRequest insertRequest);

    void copyTempCustomerStorePlanDetail(CopyCustomerStorePlanInfo info);

    void deleteByCustomerStorePlanId(String customerStorePlanId);

    void updateCustomerStorePlanId(SaveCustomerStorePlanInfo info);

    Long getStoreInfoCountByCustomerStorePlanId(String id);

    List<StoreInfoStatisticViewModel> getStoreInfoByCustomerStorePlanId(ExportStoreInfoQueryRequest viewModel);

    void loadCustomerStore(LoadInfo loadInfo);

    void batchInsertCustomerStoreDetail(String id);

    void cleanTempData(String oid);

    Long getAvailableStoreCount(String customerStorePlanId);

    Long getMatchedStoreCount(String customerStorePlanId);

    void deleteInValidCustomerStorePlanDetail(String customerStorePlanId);

    Long getInvalidCustomerStoreImportCount(InvalidCustomerStorePlanDetailRequest request);

    List<InvalidCustomerStorePlanDetail> getInvalidCustomerStoreImportInfo(InvalidCustomerStorePlanDetailRequest request);
}
