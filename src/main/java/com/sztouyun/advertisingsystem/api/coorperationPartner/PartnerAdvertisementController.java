package com.sztouyun.advertisingsystem.api.coorperationPartner;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.PartnerAdvertisementMapper;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.MonitorStatusEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.mongodb.PartnerAdvertisementOperationLog;
import com.sztouyun.advertisingsystem.model.partner.CooperationPartner;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisement;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.partner.advertisement.PartnerAdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.partner.advertisement.store.PartnerAdvertisementDeliveryRecordStatusEnum;
import com.sztouyun.advertisingsystem.model.system.AdvertisementPositionCategoryEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementDeliveryRecordService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementMaterialService;
import com.sztouyun.advertisingsystem.service.partner.advertisement.PartnerAdvertisementService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.excel.*;
import com.sztouyun.advertisingsystem.viewmodel.IdDatePageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.coorperationPartner.*;
import com.sztouyun.advertisingsystem.viewmodel.partner.StoreRequestPartnerInfo;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "第三方广告管理")
@RestController
@RequestMapping("/api/partnerAdvertisement")
public class PartnerAdvertisementController extends BaseApiController {

    @Autowired
    private PartnerAdvertisementService partnerAdvertisementService;
    @Autowired
    private PartnerAdvertisementDeliveryRecordService partnerAdvertisementDeliveryRecordService;
    @Autowired
    private PartnerAdvertisementMaterialService partnerAdvertisementMaterialService;
    @Autowired
    private PartnerAdvertisementMapper partnerAdvertisementMapper;

    @ApiOperation(value = "查询第三方广告列表", notes = "创建人:毛向军")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public InvokeResult<PageList<PartnerAdvertisementListViewModel>> queryPartnerAdvertisementList(@Validated @RequestBody PartnerAdvertisementListRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<PartnerAdvertisementListViewModel> page = partnerAdvertisementService.queryPartnerAdvertisementList(request);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(page,viewModel->{
            viewModel.setMaterialTypeName(EnumUtils.getDisplayName(viewModel.getMaterialType(), MaterialTypeEnum.class));
            viewModel.setCooperationPatternName(EnumUtils.getDisplayName(viewModel.getCooperationPattern(), CooperationPatternEnum.class));
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(viewModel.getAdvertisementStatus(), PartnerAdvertisementStatusEnum.class));
            viewModel.setOwnerName(getUserNickname(viewModel.getOwnerId()));
            return viewModel;
        }));
    }

    @ApiOperation(value = "第三方广告状态数量统计", notes = "创建人: 毛向军")
    @PostMapping(value = "/statusStatistics")
    public InvokeResult<PartnerAdvertisementCountStatisticInfo> getPartnerAdvertisementStatusStatistics(@Validated @RequestBody PartnerAdvertisementCountStatisticRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(partnerAdvertisementService.getPartnerAdvertisementStatusStatistics(request));
    }


    @ApiOperation(value="下架第三方广告",notes = "创建人:毛向军")
    @RequestMapping(value="/takeOff",method = RequestMethod.POST)
    public InvokeResult takeOff(@Validated @RequestBody PartnerAdvertisementOperationRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        partnerAdvertisementService.takeOff(request.getPartnerAdvertisementId(),request.getRemark());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询第三方广告详情", notes = "创建人:毛向军")
    @GetMapping(value = "/{partnerAdvertisementId}/detail")
    public InvokeResult<PartnerAdvertisementDetailInfo> queryPartnerAdvertisementDetail(@PathVariable("partnerAdvertisementId") String partnerAdvertisementId){
        if(StringUtils.isEmpty(partnerAdvertisementId))
            return InvokeResult.Fail("第三方广告ID不能为空");
        PartnerAdvertisement partnerAdvertisement = partnerAdvertisementService.getPartnerAdvertisement(partnerAdvertisementId);
        CooperationPartner cooperationPartner = partnerAdvertisement.getPartner();
        PartnerAdvertisementDetailInfo viewModel = ApiBeanUtils.copyProperties(partnerAdvertisement.getPartner(), PartnerAdvertisementDetailInfo.class);
        BeanUtils.copyProperties(partnerAdvertisement,viewModel);

        Map<String, PartnerAdvertisementDisplayInfo> storeCountMap = partnerAdvertisementService.getPartnerAdvertisementStatisticInfo(Arrays.asList(partnerAdvertisementId));
        var info = storeCountMap.get(partnerAdvertisementId);
        if(info !=null){
            viewModel.setStoreCount(info.getStoreCount());
            viewModel.setRequestTimes(info.getRequestTimes());
            viewModel.setDisplayTimes(info.getDisplayTimes());
            viewModel.setValidDisplayTimes(info.getValidTimes());
        }

        viewModel.setPartnerName(cooperationPartner.getName());
        viewModel.setMaterialTypeName(EnumUtils.getDisplayName(viewModel.getMaterialType(), MaterialTypeEnum.class));
        viewModel.setCooperationPatternName(EnumUtils.getDisplayName(cooperationPartner.getCooperationPattern(), CooperationPatternEnum.class));
        PartnerAdvertisementStatusEnum partnerAdvertisementStatusEnum=EnumUtils.toEnum(partnerAdvertisement.getAdvertisementStatus(),PartnerAdvertisementStatusEnum.class);
        viewModel.setAdvertisementStatus(partnerAdvertisementStatusEnum.getValue());
        viewModel.setAdvertisementStatusName(partnerAdvertisementStatusEnum.getDisplayName());
        viewModel.setOwnerName(getUserNickname(viewModel.getOwnerId()));
        viewModel.setStatus(partnerAdvertisement.getAdvertisementStatus().equals(PartnerAdvertisementStatusEnum.Delivering.getValue())? MonitorStatusEnum.OnWatching.getValue():MonitorStatusEnum.Finished.getValue());
        MonitorStatusEnum monitorStatusEnum = EnumUtils.toEnum(viewModel.getStatus(), MonitorStatusEnum.class);
        viewModel.setStartTime(partnerAdvertisement.getCreatedTime());
        viewModel.setStatusName(monitorStatusEnum.getDisplayName());
        viewModel.setDurationName(cooperationPartner.getDuration()+EnumUtils.getDisplayName(cooperationPartner.getDurationUnit(), UnitEnum.class));
        if(cooperationPartner.getDuration()==null||cooperationPartner.getDuration()==0)
            viewModel.setDurationName("");
        if(monitorStatusEnum.equals(MonitorStatusEnum.Finished)){
            viewModel.setEndTime(partnerAdvertisement.getUpdatedTime());
            viewModel.setMonitorPeriod(DateUtils.formateYmdhm(partnerAdvertisement.getCreatedTime(),partnerAdvertisement.getUpdatedTime()));
        }
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "素材列表", notes = "创建人:毛向军")
    @RequestMapping(value = "/materialList",method = RequestMethod.POST)
    public InvokeResult<PageList<PartnerAdvertisementMaterialListViewModel>> queryPartnerAdvertisementMaterialList(@Validated @RequestBody PartnerAdvertisementMaterialListRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<PartnerAdvertisementMaterial> page = partnerAdvertisementMaterialService.queryPartnerAdvertisementMaterialList(request);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(page,partnerMaterial->{
            PartnerAdvertisementMaterialListViewModel viewModel = ApiBeanUtils.copyProperties(partnerMaterial, PartnerAdvertisementMaterialListViewModel.class);
            MaterialTypeEnum materialTypeEnum = EnumUtils.toEnum(partnerMaterial.getMaterialType(), MaterialTypeEnum.class);
            viewModel.setMaterialTypeName(materialTypeEnum.getDisplayName());
            if(materialTypeEnum.equals(MaterialTypeEnum.Video)||materialTypeEnum.equals(MaterialTypeEnum.ImgVideo)){
                viewModel.setDurationUnitName("秒");
            }
            if(StringUtils.isEmpty(partnerMaterial.getUrl())){
                viewModel.setUrl(partnerMaterial.getOriginalUrl());
            }
            return viewModel;
        }));
    }

    @ApiOperation(value = "查询第三方广告门店列表的省市区筛选项", notes = "创建人:毛向军")
    @GetMapping(value = "/{partnerAdvertisementId}/area")
    public InvokeResult<List<AreaViewModel>> getPartnerAdvertisementStoreArea(@PathVariable("partnerAdvertisementId") String partnerAdvertisementId){
        PartnerAdvertisement partnerAdvertisement=partnerAdvertisementService.getPartnerAdvertisement(partnerAdvertisementId);
        if(partnerAdvertisement==null)
            throw new BusinessException("合作方广告ID无效");
        List<Area> areas = partnerAdvertisementService.getPartnerAdvertisementStoreArea(partnerAdvertisementId);
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询第三方广告门店列表", notes = "创建人:毛向军")
    @RequestMapping(value = "/storeList", method = RequestMethod.POST)
    public InvokeResult<PageList<PartnerAdvertisementStoreListViewModel>> queryPartnerAdvertisementStoreList(@Validated @RequestBody PartnerAdvertisementStoreListRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getPartnerAdvertisementStoreListViewModelPageList(request));
    }

    private PageList<PartnerAdvertisementStoreListViewModel> getPartnerAdvertisementStoreListViewModelPageList(@Validated @RequestBody PartnerAdvertisementStoreListRequest request) {
        PartnerAdvertisement partnerAdvertisement=partnerAdvertisementService.getPartnerAdvertisement(request.getPartnerAdvertisementId());
        if(partnerAdvertisement==null)
            throw new BusinessException("合作方广告ID无效");
        List<PartnerAdvertisementStoreListViewModel> list = partnerAdvertisementDeliveryRecordService.queryPartnerAdvertisementDeliveryRecordList(request);
        Pageable pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        list.stream().forEach(item -> {
            item.setCityName(getAreaName(item.getCityId()));
            item.setProvinceName(getAreaName(item.getProvinceId()));
            item.setRegionName(getAreaName(item.getRegionId()));
            item.setAdvertisementPositionTypeName(EnumUtils.getDisplayName(item.getAdvertisementPositionCategory(), AdvertisementPositionCategoryEnum.class));
            item.setAdvertisementStoreStatusName(EnumUtils.getDisplayName(item.getAdvertisementStoreStatus(), PartnerAdvertisementDeliveryRecordStatusEnum.class));
            item.setRequestDateTime(new Date(item.getRequestTime()));
            if(item.getAdvertisementStoreStatus().equals(PartnerAdvertisementDeliveryRecordStatusEnum.Delivering.getValue())){
                item.setEndTime(null);
            }else {
                item.setEndTime(getEndTime(item.getFinishTime(),item.getTakeOffTime()));
            }
            if(item.getFinishTime() != null){
                item.setDisplayTimes(1);
            }
            if (item.getStartTime() != null) {
                item.setStartPlayTime(new Date(item.getStartTime()));
            }
            item.setValidDisplayTimes(item.isValid()?1:0);
        });
        Page<PartnerAdvertisementStoreListViewModel> pages=pageResult(list, pageRequest,partnerAdvertisementDeliveryRecordService.queryPartnerAdvertisementDeliveryRecordListTotal(request));
        return ApiBeanUtils.convertToPageList(pages);
    }

    private Date getEndTime(Long finishTime,Long takeOffTime){
        if(finishTime == null)
            return new Date(takeOffTime);
        if(takeOffTime == null)
            return new Date(finishTime);
        return new Date(Math.min(finishTime,takeOffTime));
    }

    @ApiOperation(value = "导出第三方广告门店列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportStoreList",method = RequestMethod.POST)
    public InvokeResult exportPartnerAdvertisementStoreList(@Validated  @RequestBody PartnerAdvertisementStoreListRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店ID","门店名称","省份","城市","地区","具体地址","设备ID","投放位置","价格（元/次）","广告时长","广告请求时间","广告开始时间","广告结束时间","门店广告状态","展示次数","有效次数","是否有效"},
                new String[]{"shopId","storeName","provinceName","cityName","regionName","storeAddress","deviceId","advertisementPositionTypeName","price","duration","requestDateTime","startPlayTime","endTime","advertisementStoreStatusName","displayTimes","validDisplayTimes","valid"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<PartnerAdvertisementStoreListViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getPartnerAdvertisementStoreListViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("requestDateTime", TimeFormatEnum.Second);
                        put("endTime", TimeFormatEnum.Second);
                        put("startPlayTime", TimeFormatEnum.Second);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="第三方广告("+request.getPartnerAdvertisementId()+")详情下的门店列表.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询第三方广告操作记录",notes = "毛向军")
    @PostMapping(value = "/operationLogs")
    public InvokeResult<PageList<PartnerAdvertisementOperationLogsViewModel>> queryPartnerAdvertisementOperationLogs(@Validated @RequestBody PartnerAdvertisementOperationLogsRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
       List<PartnerAdvertisementOperationLog> list = partnerAdvertisementService.queryPartnerAdvertisementOperationLogs(request);
       PageRequest pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
       Long count=partnerAdvertisementService.queryPartnerAdvertisementOperationLogCount(request);
       Page<PartnerAdvertisementOperationLogsViewModel> pages=pageResult(list.stream().map(item->{
           PartnerAdvertisementOperationLogsViewModel viewModel=new PartnerAdvertisementOperationLogsViewModel();
           viewModel.setOperationStatusName(EnumUtils.toEnum(item.getAdvertisementStatus(),PartnerAdvertisementDeliveryRecordStatusEnum.class).getDisplayName());
           viewModel.setOperationStatus(item.getAdvertisementStatus());
           viewModel.setOperator(getUserNickname(item.getOperator()));
           viewModel.setRemark(item.getRemark());
           viewModel.setOperateTime(new Date(item.getCreatedTime()));
           return viewModel;
       }).collect(Collectors.toList()), pageRequest,count);
       return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pages));
    }

    @ApiOperation(value="下架第三方广告中的门店",notes = "创建人:毛向军")
    @RequestMapping(value="/store/takeOff",method = RequestMethod.POST)
    public InvokeResult storeTakeOff(@Validated @RequestBody PartnerAdvertisementStoreOperationRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        partnerAdvertisementDeliveryRecordService.manualTakeOff(request.getPartnerAdvertisementStoreId(),request.getRemark());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "导出合作方门店请求列排名",notes = "创建人：文丰")
    @RequestMapping(value="/exportStoreRequestPartnerInfo",method = RequestMethod.POST)
    public InvokeResult exportStoreRequestPartnerInfo(@Validated  @RequestBody IdDatePageRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        MyWorkbook wb = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        SheetConfig sheetConfig=new SheetConfig(
                Arrays.asList(new SheetHeader(
                        null,
                        new String[]{"门店ID","门店名称","省份","城市","地区","请求次数","请求成功次数","有效展示次数"},
                        new String[]{"shopId","storeName","provinceName","cityName","regionName","requestTimes","successfulTimes","validTimes"},
                        CellStyle.ALIGN_CENTER
                ))
        );
        List<StoreRequestPartnerInfo> list=null;
        Integer index=0;
        Long storeCount=partnerAdvertisementMapper.getPartnerConfigStoreCount(request.getId());
        request.setPageSize(Constant.QUERY_RECORD_SIZE);
        do{
            request.setPageIndex(index);
            list=partnerAdvertisementMapper.getStoreRequestPartnerInfo(request);
            if(!CollectionUtils.isEmpty(list)){
                list.forEach(a->{
                    a.setProvinceName(getAreaName(a.getProvinceId()));
                    a.setCityName(getAreaName(a.getCityId()));
                    a.setRegionName(getAreaName(a.getRegionId()));
                });
            }
            wb.addData(list,sheetConfig);
            index++;
        }while(index*Constant.QUERY_RECORD_SIZE<storeCount);
        String filename="合作方的请求门店排名数据.xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }
}
