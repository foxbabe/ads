package com.sztouyun.advertisingsystem.internalapi.task;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.internalapi.BaseInternalApiController;
import com.sztouyun.advertisingsystem.model.task.TaskTypeEnum;
import com.sztouyun.advertisingsystem.service.task.TaskService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.internal.task.*;
import com.sztouyun.advertisingsystem.viewmodel.task.AdvertisementStoreTaskViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wenfeng on 2018/3/12.
 */
@Api(value = "任务接口")
@RestController
@RequestMapping("/internal/api/task")
public class TaskInternalController extends BaseInternalApiController {
    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "批量获取待领取的广告门店任务")
    @PostMapping(value = "/advertisementStore")
    public InvokeResult<PageList<AdvertisementStoreTaskViewModel>> getAdvertisementStoreTasks(@Validated  @RequestBody BaseTasksRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<AdvertisementStoreTaskViewModel> page=taskService.getAdvertisementStoreTaskList(request);
        PageList<AdvertisementStoreTaskViewModel> pageList= ApiBeanUtils.convertToPageList(page);
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "批量获取已取消的广告门店任务")
    @PostMapping(value = "/canceled")
    public InvokeResult<PageList<CanceledTasksViewModel>> getCanceledAdvertisementStoreTasks(@Validated  @RequestBody BaseTasksRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<CanceledTasksViewModel> page=taskService.getCanceledAdvertisementStoreTasks(request);
        PageList<CanceledTasksViewModel> pageList= ApiBeanUtils.convertToPageList(page);
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "批量接受任务")
    @PostMapping(value = "/accept")
    public InvokeResult acceptTasks(@Validated  @RequestBody AcceptTasksRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        taskService.acceptTasks(request);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "完成任务")
    @PostMapping(value = "/complete")
    public InvokeResult completeTask(@Validated @RequestBody CompleteTaskRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        taskService.completeTask(request);
        return InvokeResult.SuccessResult();
    }
}
