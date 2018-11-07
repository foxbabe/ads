package com.sztouyun.advertisingsystem.api.adProfitShare;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.adProfitShare.AdvertisementProfit;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.service.adProfitShare.AdvertisementProfitService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.utils.excel.ExcelData;
import com.sztouyun.advertisingsystem.utils.excel.ExcelHeader;
import com.sztouyun.advertisingsystem.utils.excel.ExcelSheetUtil;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitInfo;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitListViewModel;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.AdvertisementProfitStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 广告分成管理
 **/
@Api("广告分成管理接口")
@RestController
@RequestMapping("/api/AdvertisementProfit")
public class AdvertisementProfitController extends BaseApiController {

    @Autowired
    private AdvertisementProfitService advertisementProfitService;
    @Autowired
    private AdvertisementService advertisementService;

    @ApiOperation(value = "获取广告分成基本统计信息（概要统计和排行）", notes = "创建人：毛向军")
    @RequestMapping(value = "statistic", method = RequestMethod.GET)
    public InvokeResult<AdvertisementProfitStatisticViewModel> advertisementProfitStatistic() {
        AdvertisementProfit advertisementProfit = advertisementProfitService.sumAdvertisementProfitStatistic();
        AdvertisementProfitStatisticViewModel viewModel = new AdvertisementProfitStatisticViewModel();
        viewModel.setTotalShareAmount(NumberFormatUtil.format(advertisementProfit.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
        viewModel.setSettledAmount(NumberFormatUtil.format(advertisementProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
        viewModel.setUnsettledAmount(NumberFormatUtil.format(advertisementProfit.getUnsettledAmount(),Constant.SCALE_TWO, RoundingMode.HALF_UP));

        advertisementProfitService.getAdvertisementProfitRank().stream().forEach(a->{
            Advertisement advertisement = a.getAdvertisement();
            AdvertisementProfitInfo info = ApiBeanUtils.copyProperties(advertisement, AdvertisementProfitInfo.class);
            info.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            info.setProfitShareStandardAmountUnitName(EnumUtils.getDisplayName(advertisement.getProfitShareStandardAmountUnit(), UnitEnum.class));
            info.setShareAmount(NumberFormatUtil.format(a.getShareAmount(),Constant.SCALE_TWO, RoundingMode.HALF_UP));
            viewModel.getList().add(info);
        });
        return InvokeResult.SuccessResult(viewModel);
    }

    @ApiOperation(value = "获取广告分成列表",notes = "创建人：毛向军")
    @RequestMapping(value="/queryAdvertisementProfitList",method = RequestMethod.POST)
    public InvokeResult<PageList<AdvertisementProfitListViewModel>> queryAdvertisementProfitList(@Validated @RequestBody AdvertisementProfitListRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        PageList<AdvertisementProfitListViewModel> resultList = getAdvertisementProfitList(request);
        return InvokeResult.SuccessResult(resultList);
    }

    private PageList<AdvertisementProfitListViewModel> getAdvertisementProfitList(AdvertisementProfitListRequest request) {
        Page<AdvertisementProfit> pages = advertisementProfitService.getAdvertisementProfitList(request);
        Map<String, List<String>> advertisementDeliveryPlatforms = advertisementService.getAdvertisementDeliveryPlatforms(pages.map(d->d.getAdvertisement().getContractId()).getContent());
        PageList<AdvertisementProfitListViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementProfit ->
        {
            Advertisement advertisement = advertisementProfit.getAdvertisement();
            AdvertisementProfitListViewModel viewModel = ApiBeanUtils.copyProperties(advertisement, AdvertisementProfitListViewModel.class);
            viewModel.setOwner(getUserNickname(advertisementProfit.getAdvertisement().getContract().getOwnerId()));
            viewModel.setPeriod(DateUtils.formateYmd(DateUtils.getIntervalDays(advertisement.getEffectiveStartTime(),advertisement.getEffectiveEndTime()==null?new Date():advertisement.getEffectiveEndTime())));
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            viewModel.setAdvertisementTypeName(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
            viewModel.setProfitShareStandardAmountUnitName(EnumUtils.getDisplayName(advertisement.getProfitShareStandardAmountUnit(), UnitEnum.class));
            viewModel.setShareAmount(NumberFormatUtil.format(advertisementProfit.getShareAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            viewModel.setSettledAmount(NumberFormatUtil.format(advertisementProfit.getSettledAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            viewModel.setUnsettledAmount(NumberFormatUtil.format(advertisementProfit.getUnsettledAmount(), Constant.SCALE_TWO, RoundingMode.HALF_UP));
            List<String> platforms = advertisementDeliveryPlatforms.get(advertisement.getContractId());
            if(EnumUtils.toValueMap(TerminalTypeEnum.class).size()==platforms.size()){
                viewModel.setTerminalNames( Constant.ALL_PLATFORM);
            }else{
                viewModel.setTerminalNames( org.thymeleaf.util.StringUtils.join(platforms,Constant.DELIVERYPLATFORMSEPARATOR));
            }
            return viewModel;
        });
        resultList.setTotalAmount(NumberFormatUtil.format(advertisementProfitService.getTotalAmount(request), Constant.SCALE_TWO, RoundingMode.HALF_UP));
        return resultList;
    }

    @ApiOperation(value = "导出广告分成列表",notes = "创建人：毛向军")
    @RequestMapping(value="/exportAdvertisementProfitList",method = RequestMethod.POST)
    public InvokeResult exportAdvertisementProfitList(@Validated  @RequestBody AdvertisementProfitListRequest request, HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Workbook wb = new XSSFWorkbook();
        ExcelHeader header=new ExcelHeader(
                null,
                new String[]{"编号","广告ID","广告名称","投放平台","广告类型","广告状态","开始投放时间","实际已投放天数","是否开启分成","分成标准","分成金额","已结算","未结算","维护人"},
                new String[]{"id","advertisementName","terminalNames","advertisementTypeName","advertisementStatusName","effectiveStartTime","period","enableProfitShare","exportProfitShareStandardAmount","shareAmount","settledAmount","unsettledAmount","owner"},
                CellStyle.ALIGN_CENTER
        );
        request.setPageSize(Constant.SHEET_RECORD_SIZE);
        PageList<AdvertisementProfitListViewModel> pageList=null;
        Integer index=0;
        Integer totalPage=0;
        do{
            request.setPageIndex(index);
            pageList=getAdvertisementProfitList(request);
            totalPage=pageList.getTotalPageSize();
            ExcelData excelData=new ExcelData("第"+index+"页").addData(pageList.getList()).addHeader(header)
                    .addTimeFormatConfig(new HashMap<String, TimeFormatEnum>(){{
                        put("effectiveStartTime",TimeFormatEnum.Minute);
                    }});
            ExcelSheetUtil.addSheet(wb,excelData);
            index++;
        }while(index<totalPage);
        String filename="广告分成列表"+request.getPageIndex()+".xlsx";
        outputStream(filename, response, wb);
        return InvokeResult.SuccessResult();
    }



}
