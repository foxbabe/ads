package com.sztouyun.advertisingsystem.api.customerStore;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlan;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.customerStore.CustomerStorePlanService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DistanceUtil;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import com.sztouyun.advertisingsystem.utils.excel.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.*;
import com.sztouyun.advertisingsystem.viewmodel.store.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by szty on 2018/5/15.
 */
@Api(value = "客户选点接口")
@RestController
@RequestMapping("/api/customerStorePlan")
public class CustomerStorePlanApiController extends BaseApiController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private CustomerStorePlanService customerStorePlanService;
    @Autowired
    private ContractService contractService;

    @ApiOperation(value="客户选点门店筛选",notes = "创建人:文丰")
    @RequestMapping(value = "/stores", method = RequestMethod.POST)
    public InvokeResult<PageList<StoreInfoViewModel>> queryStores(@Validated @RequestBody CustomerStoreInfoPageInfoRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(request.getPageIndex(),request.getPageSize());
        request.setStoreSource(null);
        Page<StoreInfoQueryResult> page = storeService.findStoreListByArea(convertViewModelToRequestDto(request,CustomerStoreInfoQueryRequest.class), pageable);
        PageList<StoreInfoViewModel> resultList = ApiBeanUtils.convertToPageList(page, item-> {
            StoreInfoViewModel storeInfo = new StoreInfoViewModel();
            BeanUtils.copyProperties(item,storeInfo);
            storeInfo.setCityName(getAreaName(item.getCityId()));
            storeInfo.setRegionName(getAreaName(item.getRegionId()));
            return storeInfo;
        });

        Integer selectedStore = customerStorePlanService.getSelectedStoreCount(request.getCustomerStorePlanId());
        resultList.setChooseCount(selectedStore);
        return InvokeResult.SuccessResult(resultList);
    }


    private <IN extends CustomerStoreInfoPageInfoRequest ,OUT extends CustomerStoreInfoQueryRequest> OUT convertViewModelToRequestDto(IN viewModel,Class<OUT> clazz) {
        OUT queryRequest = ApiBeanUtils.copyProperties(viewModel, clazz);
        List<String> areaIds = Arrays.asList(viewModel.getAreaIds().split(","));
        queryRequest.setRegionIds(areaService.filterAreaIdsByLevel(areaIds, 3));
        queryRequest.setHasAbnormalNode(areaIds.contains(Constant.AREA_ABNORMAL_NODE_ID));//选中无省市区节点
        queryRequest.setOffset(viewModel.getPageIndex() * viewModel.getPageSize());
        queryRequest.setPageSize(viewModel.getPageSize());
        queryRequest.setTopCount((viewModel.getPageIndex() + 1) * viewModel.getPageSize());
        if (viewModel.getLongitude() != null && viewModel.getLatitude() != null && viewModel.getDistance() != null) {
            queryRequest.setStoreDataMapInfo(DistanceUtil.returnSquarePoint(viewModel.getLongitude(), viewModel.getLatitude(), viewModel.getDistance()));
        }
        return queryRequest;
    }


    @ApiOperation(value="返回临时选点记录ID",notes = "创建人:王伟权")
    @PostMapping(value = "/generateTempCustomerStorePlanId")
    public InvokeResult<String> generateTempCustomerStorePlanId(@RequestParam(required = false) String oldCustomerStorePlanId) {
        String tempCustomerStorePlanId = UUIDUtils.generateOrderedUUID();
        if(!StringUtils.isEmpty(oldCustomerStorePlanId)) {
            customerStorePlanService.copyTempCustomerStorePlan(tempCustomerStorePlanId, oldCustomerStorePlanId);
        }
        return InvokeResult.SuccessResult(tempCustomerStorePlanId);
    }

    @ApiOperation(value = "筛选查询选点记录", notes = "创建人：李海峰")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public InvokeResult<PageList<CustomerStorePlanViewModel>> queryCustomerStorePlanList(@Validated @RequestBody CustomerStorePlanPageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        Page<CustomerStorePlan> pages = customerStorePlanService.getCustomerStorePlanList(viewModel);
        Map<String, Integer> useNumMap = contractService.getAllCustomerStoreContractUseNum();

        PageList<CustomerStorePlanViewModel> pageList = ApiBeanUtils.convertToPageList(pages, customerStorePlan -> {
            CustomerStorePlanViewModel customerStorePlanViewModel = new CustomerStorePlanViewModel();
            BeanUtils.copyProperties(customerStorePlan, customerStorePlanViewModel);
            customerStorePlanViewModel.setCustomerName(customerStorePlan.getCustomer().getName());
            customerStorePlanViewModel.setCreatorName(getUserNickname(customerStorePlan.getCreatorId()));
            customerStorePlanViewModel.setOwnerName(getUserNickname(customerStorePlan.getCustomer().getOwnerId()));
            if (useNumMap.get(customerStorePlan.getId()) != null) {
                customerStorePlanViewModel.setUseNum(useNumMap.get(customerStorePlan.getId()));
            }
            return customerStorePlanViewModel;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "删除选点记录", notes = "创建者：李海峰")
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public InvokeResult deleteCustomerStorePlan(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        customerStorePlanService.deleteCustomerStorePlan(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "保存选点记录", notes = "创建者：王伟权")
    @PostMapping(value = "/save")
    public InvokeResult save(@Validated @RequestBody SaveCustomerStorePlanInfo saveCustomerStorePlanInfo, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        CustomerStorePlan customerStorePlan = customerStorePlanService.findCustomerStorePlanById(saveCustomerStorePlanInfo.getOldCustomerStorePlanId());
        if(customerStorePlan != null){
            saveCustomerStorePlanInfo.setCustomerId(customerStorePlan.getCustomerId());
        }
        if (StringUtils.isEmpty(saveCustomerStorePlanInfo.getCustomerId()) && !getUser().getRoleTypeEnum().equals(RoleTypeEnum.AdvertisementCustomer)) {
            return InvokeResult.Fail("客户ID不能为空");
        }
        if (getUser().getRoleTypeEnum().equals(RoleTypeEnum.AdvertisementCustomer)) {
            saveCustomerStorePlanInfo.setCustomerId(getUser().getCustomerId());
        }
        customerStorePlanService.save(saveCustomerStorePlanInfo);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "取消选点", notes = "创建人: 王伟权")
    @PostMapping(value = "/{tempCustomerStorePlanId}/cancel")
    public InvokeResult cancel(@PathVariable String tempCustomerStorePlanId) {
        if (StringUtils.isEmpty(tempCustomerStorePlanId)) {
            return InvokeResult.Fail("临时选点ID不能为空");
        }
        customerStorePlanService.cancel(tempCustomerStorePlanId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "一键清空", notes = "创建人: 王伟权")
    @PostMapping(value = "/{customerStorePlanId}/empty")
    public InvokeResult empty(@PathVariable String customerStorePlanId) {
        if (StringUtils.isEmpty(customerStorePlanId)) {
            return InvokeResult.Fail("选点记录ID不能为空");
        }
        customerStorePlanService.emptyCustomerStorePlan(customerStorePlanId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "勾选和删除门店选点", notes = "创建人: 王伟权")
    @PostMapping(value = "/{customerStorePlanId}/store/{storeId}/toggledSelected")
    public InvokeResult toggledSelected(@PathVariable String customerStorePlanId, @PathVariable String storeId) {
        if(StringUtils.isEmpty(customerStorePlanId)) {
            return InvokeResult.Fail("选点记录ID不能为空");
        }
        if(StringUtils.isEmpty(storeId)) {
            return InvokeResult.Fail("门店ID不能为空");
        }
        customerStorePlanService.toggledSelectedCustomerStore(customerStorePlanId, storeId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "客户选点一键选取",notes = "创建人：文丰")
    @RequestMapping(value = "oneKeySelectStore",method = RequestMethod.POST)
    public InvokeResult oneKeySelectStore(@Validated @RequestBody CustomerAutoSelectStoreRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (request.getIsCheck() != null && request.getIsCheck()) {
            return InvokeResult.SuccessResult(0);
        }
        OneKeyInsertCustomerStoreRequest oneKeyInsertStoreInfoRequest = convertViewModelToRequestDto(request,OneKeyInsertCustomerStoreRequest.class);
        oneKeyInsertStoreInfoRequest.setInsertCount(request.getRecordCount());
        oneKeyInsertStoreInfoRequest.setPreviousDate(new LocalDate().minusDays(1).toDate());
        customerStorePlanService.oneKeySelectStore(oneKeyInsertStoreInfoRequest);
        return InvokeResult.SuccessResult(customerStorePlanService.getSelectedStoreCount(request.getCustomerStorePlanId()));
    }

    @ApiOperation(value="选定选点记录",notes = "创建人:毛向军")
    @RequestMapping(value = "/choose",method = RequestMethod.POST)
    public InvokeResult chooseCustomerStorePlan(@Validated @RequestBody CustomerStorePlanChooseRequest request , BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        contractService.importCustomerStorePlan(request);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "导出客户选点记录的门店到excel", notes = "创建人：李海峰")
    @GetMapping("/{id}/exportStoreInfo")
    public InvokeResult exportStoreInfo(@PathVariable("id") String id, HttpServletResponse response) {
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header = new ExcelHeader(
                null,
                new String[]{"编号", "门店ID", "门店名称", "省份", "城市", "地区", "具体地址", "设备ID", "是否可用"},
                new String[]{"shopId", "storeName", "provinceName", "cityName", "regionName", "storeAddress", "deviceId", "available"},
                CellStyle.ALIGN_CENTER
        );
        Integer index = 0;
        Integer totalPage;
        String filename = "客户选点记录的门店报表.xlsx";
        do {
            PageList<StoreInfoStatisticViewModel> pageList = getStoreInfoStatisticViewModelPageList(id);
            totalPage = pageList.getTotalPageSize();
            ExcelData excelData = new ExcelData("第" + index + "页").addData(pageList.getList()).addHeader(header);
            ExcelSheetUtil.addSheet(wb, excelData);
            index++;
        } while (index < totalPage);
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    private PageList<StoreInfoStatisticViewModel> getStoreInfoStatisticViewModelPageList(String id) {
        Page<StoreInfoStatisticViewModel> page = this.customerStorePlanService.getStoreInfoToPage(id);
        Map<String, String> areaMap = areaService.getAllAreaNames();
        return ApiBeanUtils.convertToPageList(page, storeInfoStatisticViewModel -> {
            storeInfoStatisticViewModel.setProvinceName(getAreaName(storeInfoStatisticViewModel.getProvinceId(), areaMap));
            storeInfoStatisticViewModel.setCityName(getAreaName(storeInfoStatisticViewModel.getCityId(), areaMap));
            storeInfoStatisticViewModel.setRegionName(getAreaName(storeInfoStatisticViewModel.getRegionId(), areaMap));
            return storeInfoStatisticViewModel;
        });
    }

    @ApiOperation(value ="导入选点记录",notes = "创建人：文丰")
    @PostMapping(value = "/{tempCustomerStorePlanId}/import")
    public InvokeResult<ExcelImportViewModel> excelImport(@RequestParam MultipartFile file,@PathVariable("tempCustomerStorePlanId") String tempCustomerStorePlanId){
        validateFile(file);
        return  InvokeResult.SuccessResult(customerStorePlanService.importCustomerStoreFromExcel(file,tempCustomerStorePlanId));
    }



    @ApiOperation(value ="查询无效的选点记录",notes = "创建人：文丰")
    @PostMapping(value = "/invalidCustomerStoreInfo")
    public InvokeResult<PageList<InvalidCustomerStorePlanDetail>> getInvalidCustomerStoreInfo(@Validated @RequestBody InvalidCustomerStorePlanDetailRequest request,BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Long count=customerStorePlanService.getInvalidCustomerStoreImportCount(request);
        List<InvalidCustomerStorePlanDetail> list=customerStorePlanService.getInvalidCustomerStoreImportInfo(request);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pageResult(list.stream().map(item->{
            item.setRemark(EnumUtils.getDisplayName(item.getCustomerStoreInvalidType(),StoreInvalidTypeEnum.class));
            return item;
        }).collect(Collectors.toList()), pageable,count)));
    }

    @ApiOperation(value ="导出无效的选点记录",notes = "创建人：文丰")
    @PostMapping(value = "/exportInvalidCustomerStoreInfo")
    public InvokeResult  exportInvalidCustomerStoreInfo(@Validated @RequestBody InvalidCustomerStorePlanDetailRequest request,HttpServletResponse response,BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        new String[]{"编号","门店ID","门店名称","城市","地区","备注"},
                        new String[]{"shopId","storeName","cityName","regionName","remark"},
                        CellStyle.ALIGN_CENTER
                )
        ));
        request.setPageSize(Constant.QUERY_RECORD_SIZE);
        Long count=customerStorePlanService.getInvalidCustomerStoreImportCount(request);
        List<InvalidCustomerStorePlanDetail> list =null;
        Integer index=0;
        do{
            request.setPageIndex(index);
            list=customerStorePlanService.getInvalidCustomerStoreImportInfo(request).stream().map(item->{
                item.setRemark(EnumUtils.getDisplayName(item.getCustomerStoreInvalidType(),StoreInvalidTypeEnum.class));
                return item;
            }).collect(Collectors.toList());
            sheetConfig.setSheetNameTemplate("第%d页");
            myWorkbook.addData(list,sheetConfig);
        }while(count>(index++)*request.getPageSize());
        String filename="客户选点-无效门店列表"+".xlsx";
        outputStream(filename, response, myWorkbook);
        return InvokeResult.SuccessResult();
    }

}
