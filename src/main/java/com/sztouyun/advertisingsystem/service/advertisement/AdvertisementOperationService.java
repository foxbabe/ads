package com.sztouyun.advertisingsystem.service.advertisement;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.operation.BaseOperationService;
import com.sztouyun.advertisingsystem.common.operation.IOperationCollection;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.*;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractOperationLog;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementOperationLogRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.service.advertisement.operations.*;
import com.sztouyun.advertisingsystem.service.advertisement.operations.data.AdvertisementContractActionInfo;
import com.sztouyun.advertisingsystem.service.common.operations.ValidateAuditingPermissionOperation;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.StringUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementOperationPageInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdvertisementOperationService extends BaseOperationService<AdvertisementOperationLog,Void> {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementOperationLogRepository advertisementOperationLogRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    private final QAdvertisementOperationLog qAdvertisementOperationLog = QAdvertisementOperationLog.advertisementOperationLog;

    @Override
    protected void onOperating(AdvertisementOperationLog advertisementOperationLog, IOperationCollection<AdvertisementOperationLog,Void> operationCollection) {
        if(advertisementOperationLog.getAdvertisementStatusEnum() == null)
            throw new BusinessException("操作类型错误!");

        Advertisement advertisement = advertisementService.getAdvertisementAuthorized(advertisementOperationLog.getAdvertisementId());
        Contract contract = advertisement.getContract();
        advertisementOperationLog.setAdvertisement(advertisement);
        operationCollection.add(ValidateAdvertisementStatusOperation.class);
        switch (advertisementOperationLog.getAdvertisementOperationEnum()){
            case Submit:
                operationCollection.add(ValidateAdvertisementProfitModeOperation.class,advertisement);
                break;
            case Auditing:
                operationCollection.add(ValidateAuditingPermissionOperation.class,null);
                break;
            case Delivery:
                if(!org.springframework.util.StringUtils.isEmpty(advertisementOperationLog.getRemark())){

                }else if(contract.getContractAdvertisementPeriod()-contract.getUsedContractPeriod()<advertisement.getAdvertisementPeriod()) {
                    advertisementOperationLog.setRemark("广告高风险投放");
                }else{
                    advertisementOperationLog.setRemark("广告正常投放");
                }
                operationCollection.add(ValidateMostHasOneDeliveringAdvertisementOperation.class,advertisement.getContractId());
                operationCollection.add(CreateAdvertisementMaterialUrlStepOperation.class,advertisement);
                break;
        }
        operationCollection.add(SaveAdvertisementOperationLogOperation.class);
        operationCollection.add(UpdateAdvertisementStatusOperation.class);
    }

    @Override
    protected void onOperated(AdvertisementOperationLog advertisementOperationLog, IOperationCollection<AdvertisementOperationLog,Void> operationCollection) {
        Advertisement advertisement = advertisementOperationLog.getAdvertisement();
        switch (advertisementOperationLog.getAdvertisementOperationEnum()){
            case Delivery:
                operationCollection.add(UpdateDelivererAndDeliveryTimeOperation.class,advertisement);
                //投放后开始执行合同
                String remark =StringUtils.addQuotation(advertisement.getAdvertisementName())+"广告正在投放，合同开始执行";
                ContractOperationLog contractOperationLog = new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.BeginExecute.getValue(),true,remark);
                operationCollection.add(AdvertisementContractActionOperation.class,new AdvertisementContractActionInfo(contractOperationLog,advertisement.getId()));
                operationCollection.add(CreateAdvertisementMonitorOperation.class,advertisement);
                break;
            case Finish:
                operationCollection.add(UpdateEffectiveEndTimeAndPeriodOperation.class,advertisement);
                if(advertisementOperationLog.isFinishContract()){
                    //直接完成合同
                    Contract contract=advertisement.getContract();
                    remark = advertisementOperationLog.isSuccessed()?(contract.isAuditing()?"广告投放完成，合同执行完成，终止审核自动驳回":"广告投放完成，合同执行完成"):(contract.isAuditing()?"广告下架，合同执行完成，终止审核自动驳回":"广告下架，合同广告投放完成，合同执行完成");
                    contractOperationLog= new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.Finish.getValue(),true, StringUtils.addQuotation(advertisement.getAdvertisementName())+remark);
                    operationCollection.add(AdvertisementContractActionOperation.class,new AdvertisementContractActionInfo(contractOperationLog,advertisement.getId()));
                }else {
                    //完成投放后阶段完成合同
                    remark = advertisementOperationLog.isSuccessed() ?"广告执行完成，合同状态变更为待执行":"广告下架，合同状态变更为待执行";
                    contractOperationLog = new ContractOperationLog(advertisement.getContractId(), ContractOperationEnum.StageFinish.getValue(),true,StringUtils.addQuotation(advertisement.getAdvertisementName())+remark);
                    operationCollection.add(AdvertisementContractActionOperation.class,new AdvertisementContractActionInfo(contractOperationLog,advertisement.getId()));
                }
                operationCollection.add(UpdateAdvertisementMonitorEndTimeOperation.class,advertisement);
                operationCollection.add(CancelAdvertisementTaskOperation.class,advertisement);
                break;
        }
    }

    public AdvertisementOperationLog findTakeOffLog(String advertisementID){
        return advertisementOperationLogRepository.findByAdvertisementIdAndOperationAndSuccessed(advertisementID, AdvertisementOperationEnum.Finish.getValue(),false);
    }

    public Page<AdvertisementOperationLog> queryOperateAdvertisement(AdvertisementOperationPageInfoRequest request) {
       if(!advertisementRepository.exists(request.getId()))
           throw new BusinessException("广告ID无效");
        Pageable pageable = new PageRequest(request.getPageIndex(), request.getPageSize(), new QSort(qAdvertisementOperationLog.createdTime.desc()));
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qAdvertisementOperationLog.advertisementId.eq(request.getId()));
        if(request.getOperationStatus() != 0) {
            AdvertisementOperationStatusEnum operationStatusEnum = EnumUtils.toEnum(request.getOperationStatus(), AdvertisementOperationStatusEnum.class);
            if (Objects.nonNull(operationStatusEnum) || !operationStatusEnum.equals(AdvertisementOperationStatusEnum.All)) {
                predicate.and(qAdvertisementOperationLog.operation.eq(operationStatusEnum.getOperation())).and(qAdvertisementOperationLog.successed.eq(operationStatusEnum.getSuccessed()));
            }
        }
        if(Objects.nonNull(request.getStartTime())) {
            predicate.and(qAdvertisementOperationLog.createdTime.goe(request.getStartTime()));
        }
        if(Objects.nonNull(request.getEndTime())){
            predicate.and(qAdvertisementOperationLog.createdTime.loe(request.getEndTime()));
        }
        return advertisementOperationLogRepository.findAll(predicate, pageable);
    }
}
