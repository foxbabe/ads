package com.sztouyun.advertisingsystem.api.index;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.service.statistic.AdvertisementPositionStatisticService;
import com.sztouyun.advertisingsystem.service.statistic.AdvertisementStatisticService;
import com.sztouyun.advertisingsystem.service.statistic.ContractStatisticService;
import com.sztouyun.advertisingsystem.service.statistic.CustomerStatisticService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.index.*;
import com.sztouyun.advertisingsystem.viewmodel.statistic.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Api(value = "首页展示数据接口")
@RestController
@RequestMapping("/api/index")
public class IndexApiController extends BaseApiController {

    @Autowired
    private ContractStatisticService contractStatisticService;
    @Autowired
    private CustomerStatisticService customerStatisticService;
    @Autowired
    private AdvertisementStatisticService advertisementStatisticService;
    @Autowired
    private AdvertisementPositionStatisticService advertisementPositionStatisticService;

    @ApiOperation(value = "系统概况统计", notes = "创建人: 王伟权")
    @GetMapping(value = "/summaryStatistic")
    public InvokeResult<List<SummaryStatisticViewModel>> summaryStatistic() {

        List<SummaryStatisticViewModel> summaryStatistics = new ArrayList<>();
        Date startTime = DateUtils.getDateStartByZero(DateUtils.addDays(new Date(), -1 * StatisticIntervalEnum.PAST_SEVEN_DAYS.getValue()));
        Date endTime = DateUtils.getDateStartByZero(new Date());

        List<SummaryStatisticTypeEnum> summaryStatisticTypeEnumList = Arrays.asList(
            SummaryStatisticTypeEnum.CUSTOMER,SummaryStatisticTypeEnum.CONTRACT,
            SummaryStatisticTypeEnum.DeliveringAdvertisement,SummaryStatisticTypeEnum.AVAILABLE_ADVERTISEMENT_POSITION);
        summaryStatisticTypeEnumList.forEach(summaryStatisticTypeEnum -> {
            summaryStatistics.add(getSummaryStatisticViewModel(summaryStatisticTypeEnum, startTime, endTime));
        });

        return InvokeResult.SuccessResult(summaryStatistics);
    }

    @ApiOperation(value = "合同签约趋势统计", notes = "创建人: 王伟权")
    @GetMapping(value = "/contractSignTendencyStatistic/{interval}")
    public InvokeResult<List<ContractSignTendencyViewModel>> contractSignTendencyStatistic(@PathVariable Integer interval) {

        if(!EnumUtils.isValidValue(interval, StatisticIntervalEnum.class))
            return InvokeResult.Fail("请输入有效的时间统计间隔");

        Date startTime = DateUtils.getDateStartByZero(DateUtils.addDays(new Date(), -1 * EnumUtils.toEnum(interval, StatisticIntervalEnum.class).getValue()));
        Date endTime = DateUtils.getDateStartByZero(new Date());

        List<ContractSignTendencyStatistic> contractSignTendencyStatistics = contractStatisticService.contractSignTendencyStatistic(startTime, endTime);
        fillEmptyDateData(contractSignTendencyStatistics, startTime, endTime);

        List<ContractSignTendencyViewModel> resultList = new ArrayList<>();
        contractSignTendencyStatistics.forEach(contractSignTendencyStatistic -> {
            ContractSignTendencyViewModel contractSignTendencyViewModel = ApiBeanUtils.copyProperties(contractSignTendencyStatistic, ContractSignTendencyViewModel.class);
            contractSignTendencyViewModel.setSignTimeDetail(contractSignTendencyViewModel.getSignTime());
            resultList.add(contractSignTendencyViewModel);
        });
        resultList.sort(Comparator.comparing(ContractSignTendencyViewModel::getSignTime));
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "饼状图统计", notes = "创建人: 文丰")
    @GetMapping(value = "/distributionStatistic/{statisticsType}")
    public InvokeResult<DistributionStatisticsViewModel> distributionStatistic(@PathVariable("statisticsType") Integer statisticsType) {
        if(!EnumUtils.isValidValue(statisticsType,DistributionStatisticTypeEnum.class))
            throw new BusinessException("统计类型无效");
        return InvokeResult.SuccessResult(getDistributionStatisticInfo(statisticsType));
    }

    @ApiOperation(value = "所有饼状图统计", notes = "创建人: 文丰")
    @GetMapping(value = "/allDistributionStatistics")
    public InvokeResult<List<DistributionStatisticsViewModel>> allDistributionStatistics() {
        List<DistributionStatisticsViewModel> resultList=new ArrayList<>();
        Iterator<Integer> iter=EnumUtils.toValueMap(DistributionStatisticTypeEnum.class).keySet().iterator();
        while(iter.hasNext()){
            resultList.add(getDistributionStatisticInfo(iter.next()));
        }
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "最新添加客户列表", notes = "创建人: 文丰")
    @PostMapping(value = "/lastestCustomers")
    public InvokeResult<List<CustomerListItem>> lastestCustomers() {
        Pageable pageable= new MyPageRequest(0,6,new QSort(QCustomer.customer.createdTime.desc()));
        Page<Customer> pages=customerStatisticService.queryLastestCustomerList(pageable);
        List<String> accessedUserIds = userService.getAccessedUserIds();
        List<CustomerListItem> list=new ArrayList<>();
        pages.getContent().stream().forEach( customer->{
            CustomerListItem item=new CustomerListItem();
            BeanUtils.copyProperties(customer,item);
            item.setOwner(getUserNickname(customer.getOwnerId()));
            item.setCityName(getAreaName(customer.getCityId()));
            item.setCanView(accessedUserIds.contains(customer.getOwnerId()));
            list.add(item);
        });
        return InvokeResult.SuccessResult(list);
    }

    private SummaryStatisticViewModel convertToSummaryStatisticViewModel(SummaryStatistic statistic) {
        SummaryStatisticViewModel viewModel = ApiBeanUtils.copyProperties(statistic, SummaryStatisticViewModel.class);

        if (statistic.getStatisticsTypeEnum().equals(SummaryStatisticTypeEnum.CUSTOMER) || statistic.getStatisticsTypeEnum().equals(SummaryStatisticTypeEnum.CONTRACT)) {
            double statisticsPercentage = getStatisticsPercentage(statistic);
            if(statisticsPercentage < 0) {
                viewModel.setIncrement(false);
                statisticsPercentage = Math.abs(statisticsPercentage);
            }
            viewModel.setPercentage(statisticsPercentage);
        }
        return viewModel;
    }

    private double getStatisticsPercentage(SummaryStatistic statistic) {
        if(statistic.getIncrement() == null || statistic.getIncrement().equals(0) )
            return 0;
        if(statistic.getOneWeekAgoTotal().equals(0))
            return 0;
        double incrementPercentage = (statistic.getTotalBeforeZeroTime() - statistic.getOneWeekAgoTotal()) / (double)statistic.getOneWeekAgoTotal();
        return new BigDecimal(incrementPercentage).setScale(Constant.SCALE_FIVE, BigDecimal.ROUND_DOWN).doubleValue();
    }

    private DistributionStatisticsViewModel getDistributionStatisticInfo(Integer statisticType){
        DistributionStatisticsViewModel item=new DistributionStatisticsViewModel();
        item.setStatisticsType(statisticType);
        DistributionStatisticTypeEnum typeEnum=EnumUtils.toEnum(statisticType,DistributionStatisticTypeEnum.class);
        Long total=0L;
        List<DistributionStatisticDto> list=new ArrayList<>();
        switch(typeEnum){
            case Customer:
                total=customerStatisticService.getCustomerStatistic();
                list=customerStatisticService.getDistributionCustomerAreaStatistics();
                item.setList(getDistributionCustomerStatistic(total,list));
                break;
            case Contract:
                total=contractStatisticService.getContractCount();
                list=contractStatisticService.getContractStatusStatistics();
                item.setList(convertDistributionStatistic(total,list, ContractStatusEnum.class));
                break;
            case Advertisement:
                total=advertisementStatisticService.getAdvertisementCount();
                list=advertisementStatisticService.getAdvertisementStatusStatistics();
                item.setList(convertDistributionStatistic(total,list, AdvertisementStatusEnum.class));
                break;
            case AdvertisementPosition:
                list=advertisementPositionStatisticService.getAvailableAdPositionStatistics();
                total=getTotalAvailableAdvertisementPositionCount(list);
                item.setList(convertDistributionStatistic(total,list,StoreTypeEnum.class));
                break;
        }
        return item;
    }

    private List<DistributionStatistic<String>>  getDistributionCustomerStatistic(Long total,List<DistributionStatisticDto> list){
        List<DistributionStatistic<String>> result= new ArrayList<>();
        DistributionStatistic other=new DistributionStatistic();
        if(null!=list && !list.isEmpty()){
            int size=list.size();
            Long otherCount=0L;
            Long topSevenTotal=0L;
            for(int i=0;i<size;i++){
                DistributionStatisticDto item=list.get(i);
                if(i< Constant.OTHER_INDEX){
                    DistributionStatistic info=new DistributionStatistic();
                    info.setName(areaService.getAreaNameFromCache(item.getKeyValue().toString()));
                    info.setValue(item.getValue());
                    info.setRatio(NumberFormatUtil.format(item.getValue(),total,Constant.RATIO_PATTERN));
                    topSevenTotal+=item.getValue();
                    result.add(info);
                }else{
                    otherCount=total-topSevenTotal;
                }

            }
            if(otherCount>0){
                other.setName("其他");
                other.setValue(otherCount);
                other.setRatio(NumberFormatUtil.format(otherCount,total,Constant.RATIO_PATTERN));
                result.add(other);
            }
        }
        return result;
    }

    private  <T extends EnumMessage<Integer>>  List<DistributionStatistic<String>> convertDistributionStatistic(Long total,List<DistributionStatisticDto> src, Class<T> typeEnumClass){
        if(null==src)
            return null;
        List<DistributionStatistic<String>> result=new ArrayList<>();
        Map<Integer,String> map=EnumUtils.toValueMap(typeEnumClass);
        Set<Integer> all=map.keySet();
        Set<Integer> exist=new HashSet<>();
        src.forEach(item->{
            DistributionStatistic info=new DistributionStatistic();
            info.setKeyValue((int)item.getKeyValue());
            info.setValue(item.getValue());
            info.setName(map.get(item.getKeyValue()));
            info.setRatio(NumberFormatUtil.format(item.getValue(),total,Constant.RATIO_PATTERN));
            exist.add((Integer) item.getKeyValue());
            result.add(info);
        });
        all.removeAll(exist);
        if(!typeEnumClass.equals(StoreTypeEnum.class)){
            all.remove(1);
        }
        all.forEach(key->{
            DistributionStatistic info=new DistributionStatistic();
            info.setRatio(NumberFormatUtil.format(0L,total,Constant.RATIO_PATTERN));
            info.setKeyValue(key);
            info.setName(map.get(key));
            info.setValue(0L);
            result.add(info);
        });
       Collections.sort(result);
        return result;
    }

    private void fillEmptyDateData(List<ContractSignTendencyStatistic> contractSignTendencyStatisticList, Date startTime, Date endTime) {
        int intervalDays = DateUtils.getIntervalDays(startTime, endTime);
        for(int i = 0; i < intervalDays; i++) {
            Date everyDate = DateUtils.addDays(startTime, i);
            boolean hasDate = false;

            for(ContractSignTendencyStatistic contractSignTendencyStatistic : contractSignTendencyStatisticList) {
                if (DateUtils.isEqualsDateByYearMonthDay(everyDate, contractSignTendencyStatistic.getSignTime())) {
                    hasDate = true;
                }
            }
            if (!hasDate) {
                ContractSignTendencyStatistic contractSignTendencyStatistic = new ContractSignTendencyStatistic();
                contractSignTendencyStatistic.setSignTime(everyDate);
                contractSignTendencyStatistic.setTotal(0L);
                contractSignTendencyStatisticList.add(contractSignTendencyStatistic);
            }
        }
    }

    private SummaryStatisticViewModel getSummaryStatisticViewModel(SummaryStatisticTypeEnum summaryStatisticTypeEnum, Date startTime, Date endTime) {
        SummaryStatisticViewModel summaryStatisticViewModel = null;
        switch (summaryStatisticTypeEnum) {
            case CUSTOMER:
                summaryStatisticViewModel = convertToSummaryStatisticViewModel(customerStatisticService.getCustomerIncrementStatistics(startTime, endTime));
                break;
            case CONTRACT:
                summaryStatisticViewModel = convertToSummaryStatisticViewModel(contractStatisticService.getContractIncrementStatistics(startTime, endTime));
                break;
            case DeliveringAdvertisement:
                summaryStatisticViewModel = convertToSummaryStatisticViewModel(advertisementStatisticService.getTotalDeliveryAdvertisement());
                break;
            case AVAILABLE_ADVERTISEMENT_POSITION:
                summaryStatisticViewModel = convertToSummaryStatisticViewModel(advertisementPositionStatisticService.getTotalAvailableAdvertisementPosition());
                break;
        }
        return summaryStatisticViewModel;
    }

    private Long getTotalAvailableAdvertisementPositionCount(List<DistributionStatisticDto> list){
        if(list.isEmpty())
            return 0L;
         Long count=0L;
        for(DistributionStatisticDto distributionStatistic: list) {
            count += distributionStatistic.getValue();
        }
        return count;
    }
}
