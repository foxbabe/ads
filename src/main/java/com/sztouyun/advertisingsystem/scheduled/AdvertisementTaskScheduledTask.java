package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.mapper.TaskMapper;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.job.ScheduledJob;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.model.task.TaskTypeEnum;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.service.task.ICreateTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.AutoCancelAdvertisementStoreTaskOperationService;
import com.sztouyun.advertisingsystem.service.task.advertisement.data.AdvertisementStoreInfo;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoByAdvertisementIdInfo;
import org.apache.calcite.linq4j.Linq4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementTaskScheduledTask extends BaseScheduledTask {

    @Autowired
    private ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private StoreInfoMapper storeInfoMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private AutoCancelAdvertisementStoreTaskOperationService autoCancelTaskService;

    private final static QAdvertisement qAdvertisement = QAdvertisement.advertisement;

    @Scheduled(cron = "${advertisement.task.calculate.task.time}")
    public void calculateAdvertisementTaskScheduledTask() {
        AuthenticationService.setAdminLogin();
        ScheduledJob scheduledJob = new ScheduledJob(AdvertisementTaskScheduledTask.class.getName());
        String remark = "";
        DateTime taskStartTime = DateTime.now();
        try {
            List<Advertisement> deliveringAdvertisements
                    = advertisementRepository.findAll(q -> q.selectFrom(qAdvertisement).where(qAdvertisement.advertisementStatus.eq(AdvertisementStatusEnum.Delivering.getValue())));
            if (!CollectionUtils.isEmpty(deliveringAdvertisements)) {
                for (Advertisement advertisement : deliveringAdvertisements) {
                    calculateAdvertisementTask(advertisement, new LocalDate().toDate());
                }
            }
            remark = "生成广告任务定时任务成功";
        } catch (Exception e) {
            logger.error("生成广告任务定时任务失败",e);
            scheduledJob.setFailedTaskSize(scheduledJob.getFailedTaskSize()+1);
            remark = "生成广告任务定时任务失败";
            scheduledJob.setSuccessed(false);
        }
        String taskInfo ="用时"+ (DateTime.now().getMillis()-taskStartTime.getMillis())/1000+"秒"+"开始时间:"+taskStartTime.toString(Constant.DATE_TIME_CN)+"结束时间:"+DateTime.now().toString(Constant.DATE_TIME_CN);
        logger.info("生成广告任务定时任务成功,"+ taskInfo);
        scheduledJob.setRemark(remark+taskInfo);
        scheduledJobRepository.save(scheduledJob);
    }

    public void calculateAdvertisementTask(Advertisement advertisement, Date date) {
        taskMapper.cancelTaskByDeletedStore();
        int pageSize = 200, pageIndex = 0;
        while (true) {
            StoreInfoByAdvertisementIdInfo info = new StoreInfoByAdvertisementIdInfo(advertisement.getId());
            info.setPageIndex(pageIndex);
            info.setPageSize(pageSize);
            List<StoreInfo> storeInfos = storeInfoMapper.getStoreInfoByAdvertisement(info);
            if (CollectionUtils.isEmpty(storeInfos)) {
                break;
            }
            List<TaskCategoryEnum> taskCategoryEnums = Linq4j.asEnumerable(TaskCategoryEnum.values()).where(a -> a.getTaskType().equals(TaskTypeEnum.Advertisement) && a.getSubTaskType().equals(TaskTypeEnum.Store)).toList();
            taskCategoryEnums.sort(Comparator.comparing(TaskCategoryEnum::getPriority).reversed());
            for (StoreInfo storeInfo : storeInfos) {
                try {
                    AdvertisementStoreInfo advertisementStoreInfo = new AdvertisementStoreInfo(advertisement, storeInfo, date);
                    autoCancelTaskService.operate(advertisementStoreInfo);
                    for (TaskCategoryEnum taskCategoryEnum : taskCategoryEnums) {
                        ICreateTaskOperationService<AdvertisementStoreInfo> advertisementStoreTaskService = (ICreateTaskOperationService<AdvertisementStoreInfo>)SpringUtil.getBean(taskCategoryEnum.getTaskOperationService());
                        boolean result = advertisementStoreTaskService.createTask(advertisementStoreInfo);
                        if (result) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.error("创建广告任务失败, 广告ID" + advertisement.getId() + "门店ID" + storeInfo.getId(), e);
                }
            }
            pageIndex += 1;
        }
    }
}
