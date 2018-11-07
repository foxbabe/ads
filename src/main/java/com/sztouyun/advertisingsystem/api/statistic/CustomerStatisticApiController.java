package com.sztouyun.advertisingsystem.api.statistic;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.mapper.CustomerMapper;
import com.sztouyun.advertisingsystem.model.mongodb.DailyStatistic;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.statistic.CustomerStatisticService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.DayPeriodRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageOffsetRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.customer.*;
import com.sztouyun.advertisingsystem.viewmodel.index.CustomerAreaMapViewModel;
import com.sztouyun.advertisingsystem.viewmodel.index.CustomerAreaStatistic;
import com.sztouyun.advertisingsystem.viewmodel.index.CustomerAreaStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerAreaStatisticResult;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerAreaStatisticViewModel;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerCityAreaStatistic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Api(value = "客户统计数据接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerStatisticApiController extends BaseApiController {

    @Autowired
    private CustomerStatisticService customerStatisticService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContractService contractService;

    @ApiOperation(value = "客户地区分布统计", notes = "创建人: 文丰")
    @GetMapping(value = "/allCustomerAreaMapDistributionStatistic")
    public InvokeResult<CustomerAreaMapViewModel>  allCustomerAreaMapDistributionStatistic(){
        long totalCustomer=customerStatisticService.getCustomerStatistic();
        List<CustomerAreaStatisticDto> list=customerStatisticService.getCustomerAreaStatistic();
        return InvokeResult.SuccessResult(getCustomerAreaMapViewModel(totalCustomer,list));
    }

    @ApiOperation(value = "客户地域省级统计详细信息展示", notes = "创建人: 王伟权")
    @PostMapping(value = "/provinceDetailStatistic")
    public InvokeResult<PageList<CustomerAreaStatisticViewModel>> getCustomerAreaDetailStatistic(@Validated @RequestBody BasePageInfo pageInfo, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        MyPageOffsetRequest pageOffsetRequest = new MyPageOffsetRequest(pageInfo.getPageIndex() * pageInfo.getPageSize(), pageInfo.getPageSize());

        Pageable pageable = new MyPageRequest(pageInfo.getPageIndex(), pageInfo.getPageSize());
        Page<CustomerAreaStatisticResult> page = customerStatisticService.getCustomerAreaDetailStatistic(pageOffsetRequest, pageable);
        PageList<CustomerAreaStatisticViewModel> customerAreaStatisticViewModelPageList = ApiBeanUtils.convertToPageList(page, queryResult -> {
            return convertToCustomerAreaStatisticViewModel(queryResult, customerStatisticService.getCustomerStatistic());
        });
        return InvokeResult.SuccessResult(customerAreaStatisticViewModelPageList);
    }

    @ApiOperation(value = "客户地域市级区统计详细信息展示", notes = "创建人: 王伟权")
    @GetMapping(value = "/cityDetailStatistic/{provinceId}")
    public InvokeResult<List<CustomerAreaStatisticViewModel>> getCustomerCityAreaDetailStatistic(@PathVariable String provinceId) {
        if(StringUtils.isEmpty(provinceId))
            return InvokeResult.Fail("省市ID不能为空");
        if(areaService.isMunicipality(provinceId))
            return InvokeResult.SuccessResult();
        CustomerCityAreaStatistic customerCityAreaStatistic = new CustomerCityAreaStatistic();
        customerCityAreaStatistic.setProvinceId(provinceId);
        return InvokeResult.SuccessResult(getCustomerAreaStatisticResultViewModel(customerStatisticService.getCustomerCityAreaDetailStatistic(customerCityAreaStatistic), customerStatisticService.getCustomerStatistic(), customerService.getAllCustomersByProvince(provinceId)));
    }

    private CustomerAreaMapViewModel getCustomerAreaMapViewModel(long totalCustomer,List<CustomerAreaStatisticDto> list ){
        CustomerAreaMapViewModel customerAreaMapViewModel=new CustomerAreaMapViewModel();
        customerAreaMapViewModel.setTotalAmount(totalCustomer);
        Integer size=list.size();
        customerAreaMapViewModel.setMaxAmount(list.get(0).getCustomerCount());
        customerAreaMapViewModel.setMinAmount(list.get(size-1).getCustomerCount());
        customerAreaMapViewModel.setList(getCustomerAreaStatistic((int)totalCustomer ,list));
        return customerAreaMapViewModel;

    }

    private List<CustomerAreaStatistic> getCustomerAreaStatistic(Integer total,List<CustomerAreaStatisticDto> list){
        List<CustomerAreaStatistic> result =new ArrayList<>();
        list.stream().forEach(customerAreaStatisticDto->{
            CustomerAreaStatistic customerAreaStatistic=new CustomerAreaStatistic();
            BeanUtils.copyProperties(customerAreaStatisticDto,customerAreaStatistic);
            customerAreaStatistic.setCustomerRatio(NumberFormatUtil.format(customerAreaStatisticDto.getCustomerCount().longValue(),total.longValue(),Constant.RATIO_PATTERN));
            customerAreaStatistic.setSignRatio(NumberFormatUtil.format(customerAreaStatisticDto.getSignedCount().longValue(),customerAreaStatisticDto.getCustomerCount().longValue(),Constant.RATIO_PATTERN));
            result.add(customerAreaStatistic);
        });
        return result;
    }

    private List<CustomerAreaStatisticViewModel> getCustomerAreaStatisticResultViewModel (List<CustomerAreaStatisticResult> queryReturnList, Long totalCustomer, Long totalCustomerInProvince) {
        List<CustomerAreaStatisticViewModel> resultList = new ArrayList<> ();
        queryReturnList.forEach(customerAreaStatisticResult -> {
            customerAreaStatisticResult.setTotalCustomerInProvince(totalCustomerInProvince);
            resultList.add(convertToCustomerAreaStatisticViewModel(customerAreaStatisticResult, totalCustomer));
        });
        return resultList;
    }

    private CustomerAreaStatisticViewModel convertToCustomerAreaStatisticViewModel(CustomerAreaStatisticResult result, Long totalCustomer) {
        CustomerAreaStatisticViewModel customerAreaStatisticViewModel = new CustomerAreaStatisticViewModel();
        customerAreaStatisticViewModel.setAreaName(getAreaName(result.getAreaId()));

        Long areaTotalCustomer = result.getTotalCustomerInProvince().equals(0L) ? result.getTotalCustomer() : result.getTotalCustomerInProvince();

        customerAreaStatisticViewModel.setCustomerRatio(NumberFormatUtil.format(result.getTotalCustomer(),totalCustomer,Constant.RATIO_PATTERN));
        customerAreaStatisticViewModel.setSignRatio(NumberFormatUtil.format(result.getTotalSignedCustomer(), areaTotalCustomer, Constant.RATIO_PATTERN));
        customerAreaStatisticViewModel.setCustomerCount(result.getTotalCustomer().intValue());
        customerAreaStatisticViewModel.setAreaId(result.getAreaId());
        return customerAreaStatisticViewModel;
    }

    @ApiOperation(value = "客户行业排名", notes = "创建人: 毛向军")
    @PostMapping(value = "/rankingIndustry")
    public InvokeResult<List<CustomerRankingIndustryViewModel>> rankingIndustry() {
        List<CustomerRankingIndustryViewModel> list = customerStatisticService.getRankingIndustry();
        Long customerTotalCount = customerStatisticService.getCustomerTotalCount();
        for (CustomerRankingIndustryViewModel customerRankingIndustryViewModel : list) {
            customerRankingIndustryViewModel.setCustomerRatio(NumberFormatUtil.format(customerRankingIndustryViewModel.getCustomerCount(), customerTotalCount, Constant.RATIO_PATTERN));
            customerRankingIndustryViewModel.setIndustry(getDataDictName(customerRankingIndustryViewModel.getIndustryId()));
        }
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "客户统计顶部概要统计", notes = "创建人: 王伟权")
    @GetMapping("/customerOverViewStatistic")
    public InvokeResult<CustomerOverViewStatisticViewModel> customerOverViewStatistic() {
        return InvokeResult.SuccessResult(customerStatisticService.customerOverViewStatistic());
    }

	@ApiOperation(value = "客户每日数量查询")
    @PostMapping("dailyCountStatistic")
    public InvokeResult<List<DailyStatistic>> getDailyCount(@Validated @RequestBody DayPeriodRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Long startDate=request.getStartTime()==null?null:request.getStartTime().getTime();
        Long endDate=request.getEndTime()==null?null:request.getEndTime().getTime();
        List<DailyStatistic> list=customerStatisticService.getCustomerDailyStatistic(startDate,endDate);
        list.sort(Comparator.comparing(DailyStatistic::getDate));
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "客户收益排名", notes = "创建人: 李海峰")
    @GetMapping("/customerProfitRanking")
    public InvokeResult<List<CustomerProfitRankingViewModel>> customerProfitRanking() {
        double totalProfit = contractService.getExecutedContractTotalProfit();
        val list = customerStatisticService.getCustomerProfitRanking();
        list.forEach(viewModel -> {
            viewModel.setTotalProfit(totalProfit);
        });
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "行业收益排名", notes = "创建人: 王伟权")
    @GetMapping("/industryProfitRanking")
    public InvokeResult<List<IndustryProfitRankingViewModel>> industryProfitRanking() {
        double customerTotalProfit = customerMapper.getCustomerTotalProfit();
        List<IndustryProfitRankingViewModel> viewModelList = Linq4j.asEnumerable(customerStatisticService.industryProfitRanking()).select(e -> {
            e.setIndustryName(getDataDictName(e.getIndustryId()));
            e.setTotalProfit(customerTotalProfit);
            return e;
        }).toList();
        return InvokeResult.SuccessResult(viewModelList);
    }

    @ApiOperation(value = "城市收益排名", notes = "创建人: 李海峰")
    @GetMapping("/cityProfitRanking")
    public InvokeResult<List<CityProfitRankingViewModel>> cityProfitRanking() {
        val list = customerStatisticService.getCityProfitRanking();
        list.forEach(viewModel -> {
            viewModel.setCityName(getAreaName(viewModel.getCityId()));
        });
        return InvokeResult.SuccessResult(list);
    }
}
