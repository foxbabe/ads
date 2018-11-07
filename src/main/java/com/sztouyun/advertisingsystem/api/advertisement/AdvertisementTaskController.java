package com.sztouyun.advertisingsystem.api.advertisement;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.task.TaskCategoryEnum;
import com.sztouyun.advertisingsystem.model.task.TaskResultEnum;
import com.sztouyun.advertisingsystem.model.task.TaskStatusEnum;
import com.sztouyun.advertisingsystem.service.task.TaskService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by maoxiangjun on 2018/4/4.
 */

@Api("广告任务接口")
@RestController
@RequestMapping("/api/advertisementTask")
public class AdvertisementTaskController extends BaseApiController {


    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "查询广告任务列表", notes = "创建人：毛向军")
    @PostMapping(value = "")
    public InvokeResult<PageList<AdvertisementTaskViewModel>> getAdvertisementTaskList(@Validated @RequestBody AdvertisementTaskRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<AdvertisementTaskViewModel> pages = taskService.getAdvertisementTaskList(request);
        PageList<AdvertisementTaskViewModel> resultList= ApiBeanUtils.convertToPageList(pages, advertisementTaskViewModel ->
        {
            advertisementTaskViewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisementTaskViewModel.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            advertisementTaskViewModel.setCustomerName(getCustomerName(advertisementTaskViewModel.getCustomerId()));
            advertisementTaskViewModel.setOwner(getUserNickname(advertisementTaskViewModel.getOwnerId()));
            advertisementTaskViewModel.setAdvertisementTypeName(EnumUtils.getDisplayName(advertisementTaskViewModel.getAdvertisementType(),MaterialTypeEnum.class));
            advertisementTaskViewModel.setAbnormal(advertisementTaskViewModel.getTaskCount()>0);
            return advertisementTaskViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }


    @ApiOperation(value = "广告任务数量统计", notes = "创建人: 毛向军")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<AdvertisementTaskCountStatisticInfo> getAdvertisementTaskStatusStatistics(@Validated @RequestBody AdvertisementTaskStatusStatisticsRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(taskService.getAdvertisementTaskStatusStatistics(request));
    }


    @ApiOperation(value = "广告任务详情", notes = "创建人: 毛向军")
    @GetMapping(value = "/{advertisementId}")
    public InvokeResult<AdvertisementTaskDetailViewModel> getAdvertisementTaskDetails(@PathVariable("advertisementId") String advertisementId) {
        AdvertisementTaskDetailViewModel viewModel = taskService.getAdvertisementTaskDetail(advertisementId);
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(viewModel.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            viewModel.setCustomerName(getCustomerName(viewModel.getCustomerId()));
            viewModel.setOwner(getUserNickname(viewModel.getOwnerId()));
            viewModel.setCreatorName(getUserNickname(viewModel.getCreatorId()));
            viewModel.setAdvertisementTypeName(EnumUtils.getDisplayName(viewModel.getAdvertisementType(),MaterialTypeEnum.class));
            viewModel.setAbnormal(viewModel.getTaskCount()>0);
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "查询广告任务详情列表", notes = "创建人：毛向军")
    @PostMapping(value = "/detailList")
    public InvokeResult<PageList<AdvertisementTaskDetailListViewModel>> getAdvertisementTaskDetailList(@Validated @RequestBody AdvertisementTaskDetailListRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementTaskDetailsListViewModelPageList(request));
    }

    private PageList<AdvertisementTaskDetailListViewModel> getAdvertisementTaskDetailsListViewModelPageList(@Validated @RequestBody AdvertisementTaskDetailListRequest request) {
        Page<AdvertisementTaskDetailListViewModel> pages = taskService.getAdvertisementTaskDetailList(request);
        return ApiBeanUtils.convertToPageList(pages, viewModel ->
        {
            viewModel.setTaskStatusName(EnumUtils.getDisplayName(viewModel.getTaskStatus(), TaskStatusEnum.class));
            viewModel.setTaskResultName(EnumUtils.getDisplayName(viewModel.getTaskResult(), TaskResultEnum.class));
            viewModel.setTaskCategoryName(EnumUtils.getDisplayName(viewModel.getTaskCategory(), TaskCategoryEnum.class));
            return viewModel;
        });
    }

    @ApiOperation(value = "导出广告任务详情列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportAdvertisementTaskDetailList",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementTaskDetailList(@Validated  @RequestBody AdvertisementTaskDetailListRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","任务编号","任务分类","任务状态","任务开始时间","任务结束时间","实际解决天数","预计解决天数","任务结果","门店Id","门店名称","运维人员","联系电话","更新时间"},
                new String[]{"code","taskCategoryName","taskStatusName","beginTime","endTime","actualCompletionDays","expectedCompletionTime","taskResultName","shopId","storeName","ownerName","ownerPhone","updatedTime"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementTaskDetailListViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementTaskDetailsListViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("beginTime", TimeFormatEnum.Minute);
                        put("endTime", TimeFormatEnum.Minute);
                        put("updatedTime", TimeFormatEnum.Default);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="广告任务("+request.getAdvertisementId()+")下的任务详情列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "门店任务详情", notes = "创建人: 毛向军")
    @GetMapping(value = "/store/{taskId}")
    public InvokeResult<StoreTaskDetailViewModel> getStoreTaskDetail(@PathVariable("taskId") String taskId) {
        StoreTaskDetailViewModel viewModel = taskService.getStoreTaskDetail(taskId);
        Map<String,String> areaMap=areaService.getAllAreaNames();
        viewModel.setTaskStatusName(EnumUtils.getDisplayName(viewModel.getTaskStatus(), TaskStatusEnum.class));
        viewModel.setTaskResultName(EnumUtils.getDisplayName(viewModel.getTaskResult(), TaskResultEnum.class));
        viewModel.setTaskCategoryName(EnumUtils.getDisplayName(viewModel.getTaskCategory(), TaskCategoryEnum.class));
        viewModel.setCityName(getAreaName(viewModel.getCityId(), areaMap));
        viewModel.setProvinceName(getAreaName(viewModel.getProvinceId(), areaMap));
        viewModel.setRegionName(getAreaName(viewModel.getRegionId(), areaMap));
        return InvokeResult.SuccessResult(viewModel);
    }

}


