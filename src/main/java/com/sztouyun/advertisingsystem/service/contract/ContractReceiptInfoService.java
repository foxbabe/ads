package com.sztouyun.advertisingsystem.service.contract;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.contract.ContractReceiptInfo;
import com.sztouyun.advertisingsystem.repository.contract.ContractReceiptInfoRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by szty on 2017/8/2.
 */
@Service
public class ContractReceiptInfoService extends BaseService {
    @Autowired
    private ContractReceiptInfoRepository contractReceiptInfoRepository;

    @Transactional
    public void updateReceipt(ContractReceiptInfo receiptInfo) {
        if(null == receiptInfo)
            throw new BusinessException("收款消息不能为空");
        receiptInfo.setUpdatedTime(new Date());
        contractReceiptInfoRepository.save(receiptInfo);
    }

    public ContractReceiptInfo getContractReceiptInfo() {
        List<ContractReceiptInfo> receiptInfos= contractReceiptInfoRepository.findAll();
        if(null==receiptInfos || receiptInfos.size()== 0)
            return null;
        return receiptInfos.get(0);//只有一个收款信息
    }
}
