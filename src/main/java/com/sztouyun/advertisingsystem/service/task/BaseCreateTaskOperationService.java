package com.sztouyun.advertisingsystem.service.task;


import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfig;
import com.sztouyun.advertisingsystem.model.system.HistoricalParamConfigTypeEnum;
import com.sztouyun.advertisingsystem.model.task.Task;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.service.system.HistoricalParamConfigService;
import com.twelvemonkeys.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCreateTaskOperationService<TData extends BaseTaskData>  extends BaseTaskOperationService<TData> implements ICreateTaskOperationService<TData> {
    protected abstract TaskCategoryEnum getTaskCategory(TData data);
    protected abstract String getTaskName(TData data);

    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoricalParamConfigService historicalParamConfigService;
    @Override
    public boolean createTask(TData data) {
        Task lastTask = taskService.getLastTask(getObjectId(data),getSubObjectId(data));
        if(lastTask != null && !lastTask.getTaskStatus().equals(TaskStatusEnum.Finished.getValue()) && !lastTask.getTaskStatus().equals(TaskStatusEnum.Cancel.getValue()))
            return false;

        data.setLastTask(lastTask);
        try{
            TaskCategoryEnum taskCategoryEnum =getTaskCategory(data);
            String objectId = getObjectId(data);
            String subObjectId = getSubObjectId(data);
            if(!operate(data))
                return false;

            HistoricalParamConfig expectedSolveDaysConfig = historicalParamConfigService.getHistoricalParamConfigFromCache(HistoricalParamConfigTypeEnum.EXPECTED_SOLVE_DAYS, data.getDate());
            if (expectedSolveDaysConfig == null)
                throw new BusinessException("获取不到预计解决天数");

            Task task = new Task();
            task.setCreatedTime(data.getDate());
            task.setName(getTaskName(data));
            task.setTaskCategory(taskCategoryEnum.getValue());
            task.setTaskType(taskCategoryEnum.getTaskType().getValue());
            task.setTaskSubType(taskCategoryEnum.getSubTaskType().getValue());
            task.setObjectId(objectId);
            task.setPriority(taskCategoryEnum.getPriority());
            task.setExpectedCompletionTime(expectedSolveDaysConfig.getValue().intValue());
            task.setExpectedCompletionUnit(expectedSolveDaysConfig.getUnit());
            if(!StringUtil.isEmpty(subObjectId)){
                task.setSubObjectId(subObjectId);
            }
            beforeCreateTask(task,data);
            taskService.createTask(task);
            afterCreateTask(task,data);
        }catch (Exception e){
            logger.error("创建任务失败",e);
            return false;
        }
        return true;
    }
    protected  void beforeCreateTask(Task task,TData data){}
    protected  void afterCreateTask(Task task,TData data){}
}
