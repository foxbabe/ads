package com.sztouyun.advertisingsystem.service.task;

import com.mongodb.BasicDBObject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.AdvertisementMapper;
import com.sztouyun.advertisingsystem.mapper.AttachmentMapper;
import com.sztouyun.advertisingsystem.mapper.TaskMapper;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.file.QAttachment;
import com.sztouyun.advertisingsystem.model.task.QTask;
import com.sztouyun.advertisingsystem.model.task.Task;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.repository.file.AttachmentRepository;
import com.sztouyun.advertisingsystem.repository.task.TaskRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.task.*;
import com.sztouyun.advertisingsystem.viewmodel.task.AdvertisementStoreTaskViewModel;
import com.sztouyun.advertisingsystem.viewmodel.task.TaskStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.task.TaskViewModel;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TaskService extends BaseService {
    private QTask qTask =QTask.task;
    private QAttachment qAttachment =QAttachment.attachment;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AdvertisementMapper advertisementMapper;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;

    @Transactional
    public void createTask(Task task){
        task.setCode(codeGenerationService.generateCode(CodeTypeEnum.DHC));
        task.setTaskStatus(TaskStatusEnum.PendingDistribute.getValue());
        taskRepository.saveAndFlush(task);
    }

    public Task getLastTask(String objectId,String subObjectId){
        return  taskRepository.findOne(q->q.selectFrom(qTask).where(qTask.objectId.eq(objectId).and(qTask.subObjectId.eq(subObjectId))).orderBy(qTask.createdTime.desc()));
    }

    public Page<AdvertisementTaskViewModel> getAdvertisementTaskList(AdvertisementTaskRequest request) {
        request.setAuthenticationSql(advertisementMapper.getUserAuthenticationFilterSql());
        return pageResult(advertisementMapper.getAdvertisementTaskList(request),new MyPageRequest(request.getPageIndex(), request.getPageSize()),advertisementMapper.getAdvertisementTaskListCount(request));
    }

    public AdvertisementTaskCountStatisticInfo getAdvertisementTaskStatusStatistics(AdvertisementTaskStatusStatisticsRequest request) {
        request.setAuthenticationSql(advertisementMapper.getUserAuthenticationFilterSql());
        return advertisementMapper.getAdvertisementTaskStatusStatistics(request);
    }

    public AdvertisementTaskDetailViewModel getAdvertisementTaskDetail(String advertisementId) {
        return advertisementMapper.getAdvertisementTaskDetail(advertisementId);
    }

    public Page<AdvertisementTaskDetailListViewModel> getAdvertisementTaskDetailList(AdvertisementTaskDetailListRequest request) {
        return pageResult(advertisementMapper.getAdvertisementTaskDetailList(request),new MyPageRequest(request.getPageIndex(), request.getPageSize()),advertisementMapper.getAdvertisementTaskDetailListCount(request));
    }

    public StoreTaskDetailViewModel getStoreTaskDetail(String taskId) {
        StoreTaskDetailViewModel viewModel = advertisementMapper.getStoreTaskDetail(taskId);
        viewModel.setRemarkPicture(attachmentRepository.findAll(q->q.select(qAttachment.url).from(qAttachment).where(qAttachment.objectId.eq(taskId))));
        return viewModel;
    }

    @Transactional
    public <TTaskViewModel extends TaskViewModel>  void saveTaskViewModel(TTaskViewModel  taskViewModel){
        mongoTemplate.insert(taskViewModel,"taskInfo");
    }

    public Page<BasicDBObject> getTaskList(StoreTasksRequest request){
        return  getTaskList(request,BasicDBObject.class);
    }

    public <T>  Page<T> getTaskList(BaseTasksRequest request, Class<T> clazz){
        MyPageRequest pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Long totalAmount=taskMapper.getTaskAmount(request);
        List<String> taskIds=taskMapper.getTaskIds(request);
        Query query= new Query(Criteria.where("_id").in(taskIds));
        List<T> result = mongoTemplate.find(query, clazz,"taskInfo");
        return pageResult(result,pageRequest, totalAmount);
    }

    public  Page<AdvertisementStoreTaskViewModel> getAdvertisementStoreTaskList(BaseTasksRequest request){
        MyPageRequest pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Long totalAmount=taskMapper.getTaskAmount(request);
        List<TaskStoreInfo> taskStores=taskMapper.getTaskStoreInfo(request);
        Map<String,TaskStoreInfo> taskMap=taskStores.stream().collect(Collectors.toMap(a->a.getId(), a->a));
        List<AdvertisementStoreTaskViewModel> result = getTaskList(taskMap.keySet(), AdvertisementStoreTaskViewModel.class);
        result.stream().forEach(item->{
            TaskStoreInfo taskStoreInfo=taskMap.get(item.getId());
            if(taskStoreInfo!=null){
                item.setStoreNo(taskStoreInfo.getStoreNo());
                item.setStoreName(taskStoreInfo.getStoreName());
            }
        });
        return pageResult(result,pageRequest, totalAmount);
    }

    private <T> List<T> getTaskList(List<String> taskIds, Class<T> clazz) {
        Query query= new Query(Criteria.where("_id").in(taskIds));
        return mongoTemplate.find(query, clazz,"taskInfo");
    }
    private <T> List<T> getTaskList(Set<String> taskIds, Class<T> clazz) {
        Query query= new Query(Criteria.where("_id").in(taskIds));
        return mongoTemplate.find(query, clazz,"taskInfo");
    }


    private BooleanBuilder filter(StoreTasksRequest request,List<String> storeIds){
        BooleanBuilder predicate = new BooleanBuilder(qTask.subObjectId.in(storeIds));
        predicate.and(qTask.taskType.eq(request.getTaskType()).and(qTask.taskStatus.eq(TaskStatusEnum.PendingDistribute.getValue())));
        if(request.getTaskType()==null)
            throw new BusinessException("请设置任务类别");
        if(request.getTaskSubType()!=null){
            predicate.and(qTask.taskSubType.eq(request.getTaskSubType()));
        }
        predicate.and(qTask.createdTime.goe(request.getStartTime()).and(qTask.createdTime.loe(request.getEndTime())));
        return predicate;
    }

    public void acceptTasks(AcceptTasksRequest request){
        taskMapper.acceptTasks(request);
    }

    @Transactional
    public <T extends CompleteTaskRequest> void  completeTask(T request){
        Task task=taskRepository.findOne(qTask.id.eq(request.getId()).and(qTask.taskStatus.eq(TaskStatusEnum.OnGoing.getValue())));
        if(task==null)
            throw new BusinessException("任务ID无效");
        task.setTaskResult(request.getTaskResult());
        task.setTaskStatus(TaskStatusEnum.Finished.getValue());
        task.setOwnerName(request.getOwnerName());
        task.setOwnerPhone(request.getOwnerPhone());
        task.setRemark(request.getRemark());
        if(request.getDate()==null){
            task.setEndTime(LocalDateTime.now().toDate());
        }else{
            task.setEndTime(request.getDate());
        }
        taskRepository.save(task);
        if(request.getAttachments()!=null && !request.getAttachments().isEmpty()){
            attachmentMapper.saveTaskAttachments(request);
        }
    }

    public Page<CanceledTasksViewModel> getCanceledAdvertisementStoreTasks(BaseTasksRequest request) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(qTask.taskType.eq(request.getTaskType())
                .and(qTask.taskSubType.eq(request.getTaskSubType())
                .and(qTask.taskStatus.eq(TaskStatusEnum.Cancel.getValue()))));

        if(request.getStartTime()!=null){
            predicate.and(qTask.updatedTime.goe(request.getStartTime()));
        }
        if(request.getEndTime()!=null){
            predicate.and(qTask.updatedTime.lt(request.getEndTime()));
        }
        if(request.getTaskIds()!=null&&request.getTaskIds().size()>0){
            predicate.and(qTask.id.in(request.getTaskIds()));
        }

        return taskRepository.findAll(q->q.select(Projections.bean(CanceledTasksViewModel.class,qTask.id.as("id"),qTask.remark.as("remark")))
                .from(qTask).where(predicate).orderBy(qTask.updatedTime.desc())
                ,new MyPageRequest(request.getPageIndex(),request.getPageSize()));
    }
}
