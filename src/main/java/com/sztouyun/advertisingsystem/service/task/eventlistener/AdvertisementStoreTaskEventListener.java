package com.sztouyun.advertisingsystem.service.task.eventlistener;

import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.store.StoreSourceEnum;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.task.event.AdvertisementStoreTaskEvent;
import com.sztouyun.advertisingsystem.service.task.event.data.AdvertisementStoreTaskEventData;
import com.sztouyun.advertisingsystem.service.task.eventlistener.base.BaseTaskEventListener;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.TerminalAdvertisementConfigInfo;
import com.sztouyun.advertisingsystem.viewmodel.task.AdvertisementStoreTaskViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wenfeng on 2018/4/3.
 */
@Component
public class AdvertisementStoreTaskEventListener extends BaseTaskEventListener<AdvertisementStoreTaskEvent,AdvertisementStoreTaskEventData,AdvertisementStoreTaskViewModel> {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private ContractRepository contractRepository;
    private final QContract qContract=QContract.contract;
    @Override
    protected AdvertisementStoreTaskViewModel getTaskViewModel(AdvertisementStoreTaskEventData taskEventData) {
        AdvertisementStoreTaskViewModel storeAdvertisementTaskViewModel=new AdvertisementStoreTaskViewModel();
        Advertisement advertisement=taskEventData.getAdvertisement();
        StoreInfo storeInfo=taskEventData.getStoreInfo();
        BeanUtils.copyProperties(taskEventData,storeAdvertisementTaskViewModel);
        storeAdvertisementTaskViewModel.setAdvertisementName(advertisement.getAdvertisementName());
        storeAdvertisementTaskViewModel.setStoreName(storeInfo.getStoreName());
        storeAdvertisementTaskViewModel.setStoreNo(storeInfo.getStoreNo());
        Contract contract=contractRepository.findOne(qContract.id.eq(advertisement.getContractId()),new JoinDescriptor().innerJoin(qContract.contractExtension));
        storeAdvertisementTaskViewModel.setContractCode(contract.getContractCode());
        storeAdvertisementTaskViewModel.setContractName(contract.getContractName());
        List<TerminalAdvertisementConfigInfo> materials=advertisementService.getAdvertisementMaterialInfo(taskEventData.getObjectId(),contract.getContractExtension());
        storeAdvertisementTaskViewModel.setMaterialItems(materials);
        storeAdvertisementTaskViewModel.setNewStore(StoreSourceEnum.NEW_OMS.getValue().equals(storeInfo.getStoreSource()));
        return storeAdvertisementTaskViewModel;
    }
}
