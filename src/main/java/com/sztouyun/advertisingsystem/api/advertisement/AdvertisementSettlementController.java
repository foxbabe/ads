package com.sztouyun.advertisingsystem.api.advertisement;

import com.mongodb.DBObject;
import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementSettlement;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitModeEnum;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementSettlementService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.*;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.InvalidEffectProfitStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.ExcelImportViewModel;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.StoreInvalidTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.*;

@Api("广告结算接口")
@RestController
@RequestMapping("/api/advertisement/settlement")
public class AdvertisementSettlementController extends BaseApiController {

    @Autowired
    private AdvertisementSettlementService advertisementSettlementService;


    @ApiOperation(value = "结算列表", notes = "创建人: 毛向军")
    @PostMapping("/list")
    public InvokeResult<PageList<AdvertisementSettlementListViewModel>> advertisementSettlementList(@Validated @RequestBody AdvertisementSettlementListRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(getAdvertisementSettlementListViewModelPageList(request));
    }

    private PageList<AdvertisementSettlementListViewModel> getAdvertisementSettlementListViewModelPageList(@RequestBody @Validated AdvertisementSettlementListRequest request) {
        Page<AdvertisementSettlement> pages = advertisementSettlementService.advertisementSettlementList(request);

        return ApiBeanUtils.convertToPageList(pages, advertisementSettlement -> {
            AdvertisementSettlementListViewModel viewModel = ApiBeanUtils.copyProperties(advertisementSettlement, AdvertisementSettlementListViewModel.class);
            viewModel.setOperator(getUserNickname(advertisementSettlement.getSettledUser()));
            return viewModel;
        });
    }

    @ApiOperation(value = "导出结算列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportAdvertisementSettlementList",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementSettlementList(@Validated  @RequestBody AdvertisementSettlementListRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","门店数量","分成金额","是否结算","创建时间","结算时间","结算人"},
                new String[]{"storeCount","shareAmount","settled","createdTime","settledTime","operator"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementSettlementListViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementSettlementListViewModelPageList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header).addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                put("createdTime", TimeFormatEnum.Second);
                put("settledTime", TimeFormatEnum.Second);
            }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="结算列表";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "删除结算记录", notes = "创建人：毛向军")
    @RequestMapping(value="/{id}/delete",method = RequestMethod.POST)
    public InvokeResult deleteAdvertisementSettlementInfo(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("结算记录id不能为空");
        advertisementSettlementService.delete(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "确认结算",notes = "创建人：毛向军")
    @PostMapping(value = "/{id}/settle")
    public InvokeResult<String> settle(@PathVariable String id){
        advertisementSettlementService.settle(id);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value = "筛选查询日明细", notes = "创建人: 王伟权")
    @PostMapping("/advertisementStoreDailyItems")
    public InvokeResult<PageList<AdvertisementStoreDailyItem>> advertisementStoreDailyItems(@Validated @RequestBody AdvertisementStoreDailyRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<AdvertisementStoreDailyItem> list = advertisementSettlementService.advertisementStoreDailyItems(request);
        list.stream().forEach(e -> {
            e.setProvinceName(getAreaName(e.getProvinceId()));
            e.setCityName(getAreaName(e.getCityId()));
            e.setRegionName(getAreaName(e.getRegionId()));
            e.setProfitDate(new Date(e.getDate()));
            e.setAdvertisementId(request.getAdvertisementId());
            if (!StringUtils.isEmpty(e.getSettlementId()) && e.getSettlementId().equals(request.getSettlementId())) {
                e.setIsCheck(true);
            }
        });
        Pageable pageRequest=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Long itemCount = advertisementSettlementService.advertisementStoreDailyItemCount(request);
        PageList<AdvertisementStoreDailyItem> pageList = ApiBeanUtils.convertToPageList(pageResult(list, pageRequest, itemCount));

        AdvertisementSettlementInfo advertisementSettlementInfo = advertisementSettlementService.getAdvertisementSettlementInfo(request.getAdvertisementId(),request.getSettlementId());
        pageList.setChooseCount(advertisementSettlementInfo.getStoreCount());
        pageList.setTotalAmount(NumberFormatUtil.format(advertisementSettlementInfo.getShareAmount()/100.0, Constant.SCALE_TWO, RoundingMode.DOWN));
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "创建结算左侧树目录", notes = "创建人: 王伟权")
    @PostMapping("/settlementAreaTree")
    public InvokeResult<List<AreaViewModel>> settlementAreaTree(@Validated @RequestBody BaseAdvertisementSettlementRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        List<Area> areaList = advertisementSettlementService.getAdvertisementAreaInfo(request.getAdvertisementId());
        List<String> settlementCheckArea = advertisementSettlementService.getSettlementCheckArea(request.getAdvertisementId(),request.getSettlementId());
        advertisementSettlementService.addSpecialNode(areaList, request.getAdvertisementId());
        List<AreaViewModel> areaViewModels = ApiBeanUtils.convertToAreaTreeListWithRootNode(areaList, area -> {
            AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            if(!settlementCheckArea.isEmpty()){
                if(settlementCheckArea.contains(area.getId())){
                    areaViewModel.setChecked(Boolean.TRUE);
                }
                if(areaViewModel.getName().equals(Constant.AREA_ABNORMAL_NODE_NAME) && Linq4j.asEnumerable(settlementCheckArea).any(region -> region.isEmpty())) {
                    areaViewModel.setChecked(true);
                }
            }
            return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(areaViewModels);
    }

 	@ApiOperation(value ="导入按效果分成记录",notes = "创建人：文丰")
    @PostMapping(value = "/{advertisementId}/import")
    public InvokeResult<ExcelImportViewModel> excelImport(@RequestParam MultipartFile file,@PathVariable("advertisementId") String advertisementId){
        validateFile(file);
        return  InvokeResult.SuccessResult(advertisementSettlementService.importStoreProfitFromExcel(file,advertisementId));
    }

 	@ApiOperation(value ="查询导入的无效分成记录",notes = "创建人：文丰")
    @PostMapping(value = "/invalidList")
    public InvokeResult<PageList<InvalidEffectProfitStoreInfo>> queryInvalidImportList(@RequestBody @Validated InvalidEffectProfitStoreInfoRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable=new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Integer count=advertisementSettlementService.getEffectProfitStoreCount(request.getId(),false);
        List<InvalidEffectProfitStoreInfo> list=advertisementSettlementService.getInvalidEffectProfitStoreInfo(request);
        return  InvokeResult.SuccessResult( ApiBeanUtils.convertToPageList(pageResult(list,pageable,count)));
    }

    @ApiOperation(value = "选择或者取消单个日收益", notes = "创建人: 王伟权")
    @PostMapping("/toggleSettlementProfit")
    public InvokeResult toggleSettlementProfit(@Validated @RequestBody ToggleSettlementProfitRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        advertisementSettlementService.toggleSelectAdvertisementStoreDailyProfit(request.getId(),request.getSettlementId());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value ="导出广告分成结算明细",notes = "创建人：文丰")
    @PostMapping(value = "/{id}/export")
    public InvokeResult  exportAdvertisementURlMonitorPhone(@PathVariable String id,HttpServletResponse response){
        AdvertisementSettlement advertisementSettlement=advertisementSettlementService.findAdvertisementSettlementById(id);
        AdvertisementProfitModeEnum profitMode= EnumUtils.toEnum(advertisementSettlement.getAdvertisementProfitMode(),AdvertisementProfitModeEnum.class);
        SheetConfig config=getAdvertisementSettlementExportConfig(profitMode);
        String fileName="广告分成结算明细.xlsx";
        MyWorkbook workbook=getDeliveryEffectInfoWorkbook(advertisementSettlement,config);
        outputStream(fileName, response,workbook );
        return InvokeResult.SuccessResult();
    }

    private SheetConfig getAdvertisementSettlementExportConfig(AdvertisementProfitModeEnum profitMode) {
        String[]headerNames=null;
        String[]headerKeys=null;
        if(profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect)){
            headerNames=new String[]{"门店ID","门店名称","单价","数量","分成金额"};
            headerKeys=new String[]{"storeNo","storeName","unitPrice","count","shareAmount"};
        }else{
            headerNames=new String[]{"编号","门店ID","门店名称","省份","城市","地区","设备ID","分成时间","分成金额"};
            headerKeys=new String[]{"storeNo","storeName","provinceName","cityName","regionName","deviceId","profitTime","shareAmount"};
        }
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        headerNames,
                        headerKeys,
                        CellStyle.ALIGN_CENTER
                )
        )).addCurrencyKeys(Arrays.asList("unitPrice","shareAmount"));
        return sheetConfig;
    }

    private MyWorkbook getDeliveryEffectInfoWorkbook(AdvertisementSettlement advertisementSettlement, SheetConfig sheetConfig){
        AdvertisementProfitModeEnum profitMode=EnumUtils.toEnum(advertisementSettlement.getAdvertisementProfitMode(),AdvertisementProfitModeEnum.class);
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        List<DBObject> list =null;
        ObjectId lastId=null;
        do{
            list=advertisementSettlementService.getAdvertisementSettlementDetailList(lastId,advertisementSettlement.getId(),Constant.MONGODB_PAGESIZE,profitMode);
            sheetConfig.setSheetNameTemplate("第%d页");
            if(CollectionUtils.isEmpty(list)){
                myWorkbook.addData(list,sheetConfig);
               break;
            }
                if(!profitMode.equals(AdvertisementProfitModeEnum.DeliveryEffect)){
                    list.stream().forEach(a->{
                        setAreaName("provinceId","provinceName",a);
                        setAreaName("cityId","cityName",a);
                        setAreaName("regionId","regionName",a);
                        a.put("profitTime", DateUtils.dateFormat(new Date(Long.valueOf(a.get("date").toString())),Constant.DATA_YMD_CN));
                    });
                }


            myWorkbook.addData(list,sheetConfig);
            lastId=new ObjectId(list.get(list.size()-1).get("_id").toString());
        }while(true);
        return myWorkbook;
    }

    private void setAreaName(String areaId,String areaName,DBObject object){
        if(object.containsField(areaId)){
            object.put(areaName,getAreaName(object.get(areaId).toString()));
        }else{
            object.put(areaName,"");
        }
    }


    @ApiOperation(value = "一键全选", notes = "创建人: 王伟权")
    @PostMapping("/selectAll")
    public InvokeResult selectAll(@Validated @RequestBody AdvertisementSettlementSelectAllRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        advertisementSettlementService.selectAllAdvertisementStoreDailyProfit(request);
        return InvokeResult.SuccessResult();
    }


    @ApiOperation(value = "清空", notes = "创建人: 王伟权")
    @PostMapping("/empty")
    public InvokeResult empty(@Validated @RequestBody BaseAdvertisementSettlementRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        advertisementSettlementService.empty(request.getAdvertisementId(),request.getSettlementId());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value ="导出的无效的按效果分成明细",notes = "创建人：文丰")
    @PostMapping(value = "/{id}/invalidListExport")
    public InvokeResult queryInvalidImportList(@PathVariable String id,HttpServletResponse response){
        SheetConfig sheetConfig=new SheetConfig(Arrays.asList(
                new SheetHeader(
                        null,
                        new String[]{"编号","门店ID","门店名称","备注"},
                        new String[]{"storeNo","storeName","remark"},
                        CellStyle.ALIGN_CENTER
                )
        )).addCurrencyKeys(Arrays.asList("unitPrice","shareAmount"));
        MyWorkbook myWorkbook = new MyWorkbook(new XSSFWorkbook(),Constant.SHEET_RECORD_SIZE);
        List<DBObject> list =null;
        ObjectId lastId=null;
        do{
            list=advertisementSettlementService.getEffectProfitInvalidList(lastId,id,Constant.MONGODB_PAGESIZE);
            if(CollectionUtils.isEmpty(list))
                break;
            list.stream().forEach(a->{
                Integer key=Integer.valueOf(a.get("validType").toString());
                a.put("remark",EnumUtils.getDisplayName(key,StoreInvalidTypeEnum.class));
            });
            sheetConfig.setSheetNameTemplate("第%d页");
            myWorkbook.addData(list,sheetConfig);
            lastId=new ObjectId(list.get(list.size()-1).get("_id").toString());
        }while(true);
        String fileName="效果分成无效导入明细.xlsx";
        outputStream(fileName, response,myWorkbook );
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "非效果计费保存结算记录", notes = "创建人: 王伟权")
    @PostMapping("/save")
    public InvokeResult save(@Validated @RequestBody BaseAdvertisementSettlementRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        advertisementSettlementService.saveSettlement(request.getAdvertisementId(),request.getSettlementId());
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "非效果计费取消保存结算记录", notes = "创建人: 李川")
    @PostMapping("/cancel")
    public InvokeResult cancel(@Validated @RequestBody BaseAdvertisementSettlementRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        advertisementSettlementService.cancelSettlement(request.getAdvertisementId(),request.getSettlementId());
        return InvokeResult.SuccessResult();
    }
}
