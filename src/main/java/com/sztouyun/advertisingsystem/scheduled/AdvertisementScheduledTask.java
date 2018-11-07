package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementMapper;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationEnum;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementOperationService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AutoFinishAdvertisementInfo;
import org.apache.calcite.linq4j.Linq4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AdvertisementScheduledTask extends BaseScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private AdvertisementOperationService advertisementOperationService;
    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    private final QContract qContract = QContract.contract;



    @Scheduled(cron = "${advertisement.status.jobs.cron.minute}")
    public void autoOperateAdvertisement(){
        AuthenticationService.setAdminLogin();
        //定时检查待完成的广告
        Date currentTime=new Date();
        autoFinishAdvertisement(currentTime);
        autoDeliveryAdvertisement(currentTime);
    }
    

    private void autoFinishAdvertisement(Date currentTime) {
        List<Advertisement> list = advertisementMapper.getDeliveryAdvertisement(new AutoFinishAdvertisementInfo(AdvertisementStatusEnum.Delivering.getValue(), currentTime));
        List<String> contractIds = Linq4j.asEnumerable(list).select(a -> a.getContractId()).distinct().toList();
        Map<String, Contract> contractMap = Linq4j.asEnumerable(contractRepository.findAll(q -> q.selectFrom(qContract).where(qContract.id.in(contractIds)))).toMap(a -> a.getId(), b -> b);
        if(list.isEmpty()) {
            logger.info("执行完成Job：自动完成广告和合同，暂无数据！");
            return;
        }

        ScheduledJob scheduledJob=new ScheduledJob("自动完成广告和合同");
        StringBuffer remark=new StringBuffer();
        scheduledJob.setTaskSize(list.size());
        list.forEach(advertisement -> {
            try {
                advertisement.setContract(contractMap.get(advertisement.getContractId()));
                advertisementOperationService.operate(getFinishAdvertisementOperationLog(advertisement));
            }catch (Exception ex){
                scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
                remark.append(advertisement.getId()+" failed;");
                logger.error(ex.getMessage(),ex);
            }
        });
        remark.append("系统定时更新广告完成成功;");
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);

    }

    private AdvertisementOperationLog getFinishAdvertisementOperationLog(Advertisement advertisement){
        AdvertisementOperationLog operationLog = new AdvertisementOperationLog();
        Integer operation= AdvertisementOperationEnum.Finish.getValue();
        Contract contract=advertisement.getContract();
        boolean successed =true;
        boolean finishContract = false;
        //是否完成合同
        Integer usedContractPeriod=contract.getUsedContractPeriod()+ DateUtils.getIntervalDays(advertisement.getEffectiveStartTime(),new Date());
        if(usedContractPeriod>=contract.getContractAdvertisementPeriod()){
            finishContract=true;
        }
        operationLog.setAutoTakeOff(true);
        operationLog.setAdvertisementId(advertisement.getId());
        operationLog.setAdvertisement(advertisement);
        operationLog.setOperation(operation);
        operationLog.setSuccessed(successed);
        operationLog.setFinishContract(finishContract);
        return operationLog;
    }

    private void autoDeliveryAdvertisement(Date currentTime){
        List<Advertisement> advertisements=advertisementService.getPendingDeliveryAdvertisements(currentTime);
        logger.info("自动投放广告Job：自动投放广告，暂无数据！");
        if(advertisements.isEmpty()) {
            return;
        }
        ScheduledJob scheduledJob=new ScheduledJob("自动投放广告");
        StringBuffer remark=new StringBuffer();
        scheduledJob.setTaskSize(advertisements.size());
        advertisements.forEach(advertisement -> {
            try {
                advertisementOperationService.operate(getDeliveryAdvertisementOperationLog(advertisement));
            }catch (BusinessException e){
                logger.info(e.getMessage(),e);
            }
            catch (Exception ex){
                scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
                remark.append(advertisement.getId()+" failed;");
                logger.error(ex.getMessage(),ex);
            }
        });
        remark.append("系统定时自动投放广告完成;");
        scheduledJob.setRemark(remark.toString());
        scheduledJobRepository.save(scheduledJob);
    }

    private AdvertisementOperationLog getDeliveryAdvertisementOperationLog(Advertisement advertisement){
        AdvertisementOperationLog operationLog = new AdvertisementOperationLog();
        operationLog.setAdvertisementId(advertisement.getId());
        operationLog.setAdvertisement(advertisement);
        operationLog.setOperation(AdvertisementOperationEnum.Delivery.getValue());
        operationLog.setSuccessed(true);
        operationLog.setRemark("广告到投放开始时间，系统自动投放");
        return operationLog;
    }

}