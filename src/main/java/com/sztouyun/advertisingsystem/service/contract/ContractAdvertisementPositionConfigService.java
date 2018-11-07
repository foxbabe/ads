package com.sztouyun.advertisingsystem.service.contract;

import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.contract.QContractAdvertisementPositionConfig;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionTypeEnum;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementPositionConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ContractAdvertisementPositionConfigService {
    @Autowired
    private ContractAdvertisementPositionConfigRepository advertisementPositionConfigRepository;

    @Autowired
    private ContractRepository contractRepository;

    private final QContractAdvertisementPositionConfig qContractAdvertisementPositionConfig = QContractAdvertisementPositionConfig.contractAdvertisementPositionConfig;

    public List<ContractAdvertisementPositionConfig> getContractAdvertisementPositionConfigs(String contractId){
        if(!contractRepository.exists(contractId))
            throw new BusinessException("合同数据不存在");
        return advertisementPositionConfigRepository.findAll(qContractAdvertisementPositionConfig.contractId.eq(contractId),new JoinDescriptor().innerJoin(qContractAdvertisementPositionConfig.advertisementSizeConfig));
    }

    public String getMaterials(String contractId){
        return org.apache.commons.lang3.StringUtils.join(Linq4j.asEnumerable(getContractAdvertisementPositionConfigs(contractId)).select(a-> EnumUtils.toEnum(a.getAdvertisementPositionType(), AdvertisementPositionTypeEnum.class).getDisplayName()).toList(),",");
    }

    public Integer[] getDeliveryConfig(String contractId){
        Integer[] deliveryConfig=new Integer[2];
        Integer displayTimes=0;
        Integer duration=0;
        List<Integer> bigScreenPositionTypeList= Arrays.asList(AdvertisementPositionTypeEnum.FullScreen.getValue(),AdvertisementPositionTypeEnum.ScanPay.getValue());
        List<ContractAdvertisementPositionConfig> contractAdvertisementPositionConfigList=getContractAdvertisementPositionConfigs(contractId);
        ContractAdvertisementPositionConfig firstBigScreenPositionConfig=Linq4j.asEnumerable(contractAdvertisementPositionConfigList).where(a-> bigScreenPositionTypeList.contains(a.getAdvertisementPositionType()) && a.getTerminalType().equals(TerminalTypeEnum.CashRegister.getValue()) ).firstOrDefault();
        if(firstBigScreenPositionConfig!=null){
            displayTimes+=firstBigScreenPositionConfig.getDisplayTimes();
            duration=firstBigScreenPositionConfig.getDuration();
        }
        List<Integer> sellerScreenPositionTypeList= Arrays.asList(AdvertisementPositionTypeEnum.SellerFullScreen.getValue(),AdvertisementPositionTypeEnum.BusinessBanner.getValue());
        ContractAdvertisementPositionConfig firstSellerScreen=Linq4j.asEnumerable(contractAdvertisementPositionConfigList).where(a->sellerScreenPositionTypeList.contains(a.getAdvertisementPositionType()) && a.getTerminalType().equals(TerminalTypeEnum.CashRegister.getValue())).firstOrDefault();
        if(firstSellerScreen!=null){
            displayTimes+=firstSellerScreen.getDisplayTimes();
            if(duration==0){
                duration=firstSellerScreen.getDuration();
            }
        }
        deliveryConfig[0]=displayTimes;
        deliveryConfig[1]=duration;
        return deliveryConfig;
    }

    public List<Integer> getContractSelectedTerminalType(String contractId) {
        return advertisementPositionConfigRepository.findAll(q -> q.select(qContractAdvertisementPositionConfig.terminalType).from(qContractAdvertisementPositionConfig).where(qContractAdvertisementPositionConfig.contractId.eq(contractId)).groupBy(qContractAdvertisementPositionConfig.terminalType));
    }
}
