package com.sztouyun.advertisingsystem.mapper;


import com.sztouyun.advertisingsystem.viewmodel.internal.task.AcceptTasksRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.task.BaseTasksRequest;
import com.sztouyun.advertisingsystem.viewmodel.mock.task.MockAdvertisementTaskViewModel;
import com.sztouyun.advertisingsystem.viewmodel.task.CancelAdvertisementTaskInfo;
import com.sztouyun.advertisingsystem.viewmodel.task.TaskStoreInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    void acceptTasks(AcceptTasksRequest request);

    void deleteAdvertisementTaskByTime(MockAdvertisementTaskViewModel info);

    List<String> getTaskIds(BaseTasksRequest request);

    Long getTaskAmount(BaseTasksRequest request);

    List<TaskStoreInfo> getTaskStoreInfo(BaseTasksRequest request);

    void cancelTaskByAdvertisement(CancelAdvertisementTaskInfo info);

    void cancelTaskByDeletedStore();
}
