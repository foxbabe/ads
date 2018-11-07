package com.sztouyun.advertisingsystem.service.advertisement.operations;

import com.sztouyun.advertisingsystem.common.operation.IActionOperation;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.monitor.AdvertisementMonitorStatistic;
import com.sztouyun.advertisingsystem.repository.contract.ContractExtensionRepository;
import com.sztouyun.advertisingsystem.repository.monitor.AdvertisementMonitorStatisticRepository;
import com.sztouyun.advertisingsystem.service.contract.ContractAdvertisementPositionConfigService;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAdvertisementMonitorOperation implements IActionOperation<Advertisement> {
    @Autowired
    private ContractExtensionRepository contractExtensionRepository;
    @Autowired
    private ContractService contractService;
    @Autowired
    private AdvertisementMonitorStatisticRepository advertisementMonitorStatisticRepository;
    @Autowired
    private ContractAdvertisementPositionConfigService contractAdvertisementPositionConfigService;

    @Override
    public void operateAction(Advertisement advertisement) {
        if(!advertisementMonitorStatisticRepository.exists(advertisement.getId())){
            AdvertisementMonitorStatistic advertisementMonitorStatistic=new AdvertisementMonitorStatistic();
            Contract contract=advertisement.getContract();
            String contractId=contract.getId();
            advertisementMonitorStatistic.setContractId(contractId);
            advertisementMonitorStatistic.setCustomerId(advertisement.getCustomerId());
            Integer [] deliveryConfig=contractAdvertisementPositionConfigService.getDeliveryConfig(contractId);
            //todo 后续ContractAdvertisementConfig废弃，duration的处理待产品确认
            Integer dailySingleStoreExpectedDisplayTimes=deliveryConfig[0];
            advertisementMonitorStatistic.setDuration(deliveryConfig[1]);
            advertisementMonitorStatistic.setDisplayFrequency(dailySingleStoreExpectedDisplayTimes);
            Integer storeCount=contractExtensionRepository.findOne(contractId).getTotalStoreCount();
            advertisementMonitorStatistic.setTotalDisplayTimes(dailySingleStoreExpectedDisplayTimes*storeCount*advertisement.getAdvertisementPeriod().longValue());
            advertisementMonitorStatistic.setTotalStoreCount(storeCount);
            advertisementMonitorStatistic.setTotalCityCount(contractService.getContractTotalCityCount(contractId));
            advertisementMonitorStatistic.setDeliveryCities(contractService.getCityNames(contractId));
            advertisementMonitorStatistic.setMaterials(contractAdvertisementPositionConfigService.getMaterials(advertisement.getContractId()));
            advertisementMonitorStatistic.setId(advertisement.getId());
            advertisementMonitorStatisticRepository.save(advertisementMonitorStatistic);
        }
    }


}
