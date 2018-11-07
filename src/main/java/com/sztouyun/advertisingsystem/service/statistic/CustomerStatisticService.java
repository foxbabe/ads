package com.sztouyun.advertisingsystem.service.statistic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.mapper.ContractMapper;
import com.sztouyun.advertisingsystem.mapper.CustomerMapper;
import com.sztouyun.advertisingsystem.model.common.DailyStatisticTypeEnum;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.mongodb.DailyStatistic;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.common.BaseAuthenticationInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageOffsetRequest;
import com.sztouyun.advertisingsystem.viewmodel.customer.*;
import com.sztouyun.advertisingsystem.viewmodel.index.CustomerAreaStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerAreaStatisticResult;
import com.sztouyun.advertisingsystem.viewmodel.statistic.CustomerCityAreaStatistic;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatistic;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatisticTypeEnum;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CustomerStatisticService extends BaseService {
    private final QCustomer qCustomer = QCustomer.customer;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    public SummaryStatistic getCustomerIncrementStatistics(Date startTime, Date endTime) {

        NumberExpression<Integer> incrementCaseWhen = new CaseBuilder().when(qCustomer.createdTime.goe(startTime).and(qCustomer.createdTime.lt(endTime))).then(1).otherwise(0);
        NumberExpression<Integer> pastTotalsCaseWhen = new CaseBuilder().when(qCustomer.createdTime.lt(startTime)).then(1).otherwise(0);
        NumberExpression<Integer> totalCaseWhen = new CaseBuilder().when(qCustomer.createdTime.lt(endTime)).then(1).otherwise(0);

        SummaryStatistic summaryStatistic = customerRepository.findOneAuthorized(queryFactory -> queryFactory.select(Projections.bean(SummaryStatistic.class,
                qCustomer.id.count().as("total"),
                totalCaseWhen.sum().as("totalBeforeZeroTime"),
                incrementCaseWhen.sum().as("increment"),
                pastTotalsCaseWhen.sum().as("oneWeekAgoTotal")
                ))
                .from(qCustomer)
        );
        summaryStatistic.setStatisticType(SummaryStatisticTypeEnum.CUSTOMER.getValue());
        summaryStatistic.setStartDate(startTime);
        summaryStatistic.setEndDate(endTime);
        return summaryStatistic;

    }
	
	  public List<DistributionStatisticDto> getDistributionCustomerAreaStatistics(){
        return customerRepository.findAllAuthorized(q ->q
                .select(Projections.bean(DistributionStatisticDto.class, qCustomer.cityId.as("keyValue"),
                        qCustomer.id.count().as("value")))
                .from(qCustomer)
                .groupBy(qCustomer.cityId)
                .orderBy( qCustomer.id.count().desc())
                .limit(8)
        );
    }

    public Page<Customer> queryLastestCustomerList(Pageable pageable){
        return  customerRepository.findAllAuthorized(new BooleanBuilder(),pageable);
    }

    public List<CustomerAreaStatisticDto> getCustomerAreaStatistic(){
        BaseAuthenticationInfo baseAuthenticationInfo=new BaseAuthenticationInfo();
        baseAuthenticationInfo.setAuthenticationSql(customerMapper.getUserAuthenticationFilterSql());
        return customerMapper.getCustomerAreaStatistic(baseAuthenticationInfo);
    }

    public long getCustomerStatistic(){
        return customerRepository.countAuthorized(q->q.select(qCustomer.id).from(qCustomer));
    }


    public Page<CustomerAreaStatisticResult> getCustomerAreaDetailStatistic(MyPageOffsetRequest pageOffsetRequest, Pageable pageable) {
        pageOffsetRequest.setAuthenticationSql(customerMapper.getUserAuthenticationFilterSql());
        return pageResult(customerMapper.getCustomerProvinceAreaDetailStatistic(pageOffsetRequest), pageable, getCustomerProvinceAreaDetailStatisticCount());
    }

    private Long getCustomerProvinceAreaDetailStatisticCount() {
        BaseAuthenticationInfo baseAuthenticationInfo = new BaseAuthenticationInfo();
        baseAuthenticationInfo.setAuthenticationSql(customerMapper.getUserAuthenticationFilterSql());
        return customerMapper.getCustomerProvinceAreaDetailStatisticCount(baseAuthenticationInfo);
    }


    public List<CustomerAreaStatisticResult> getCustomerCityAreaDetailStatistic(CustomerCityAreaStatistic customerCityAreaStatistic) {
        customerCityAreaStatistic.setAuthenticationSql(customerMapper.getUserAuthenticationFilterSql());
        return customerMapper.getCustomerCityAreaDetailStatistic(customerCityAreaStatistic);
    }

    public List<CustomerRankingIndustryViewModel> getRankingIndustry() {
        return customerRepository.findAll(q ->q
                .select(Projections.bean(CustomerRankingIndustryViewModel.class,
                        qCustomer.industryId.as("industryId"),
                        qCustomer.id.count().as("customerCount")))
                .from(qCustomer)
                .groupBy(qCustomer.industryId).orderBy(qCustomer.id.count().desc(),qCustomer.customerNumber.asc()).limit(20));
    }

    public Long getCustomerTotalCount(){
        return customerRepository.count();
    }

    public CustomerOverViewStatisticViewModel customerOverViewStatistic() {
        CustomerOverViewStatisticViewModel customerOverViewStatisticViewModel = customerMapper.customerOverViewStatistic();
        customerOverViewStatisticViewModel.setTotalProfit(customerMapper.getCustomerTotalProfit());
        return customerOverViewStatisticViewModel;
    }

	public List<DailyStatistic> getCustomerDailyStatistic(Long startDate, Long endDate){
        Query query=new Query();
        Long today= LocalDate.now().toDate().getTime();
        if(startDate!=null && endDate!=null){
            query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate).lt(today));
        }else if(startDate==null && endDate!=null){
            query.addCriteria(Criteria.where("date").lte(endDate).lt(today));
        }else if(startDate!=null && endDate==null){
            query.addCriteria(Criteria.where("date").gte(startDate).lt(today));
        }
        List<DailyStatistic> list= mongoTemplate.find(query,DailyStatistic.class);
        if(endDate!=null && endDate>=today){
            DailyStatistic todayStatistic=new DailyStatistic(today,customerMapper.getCustomerCount(LocalDate.now().plusDays(1).toDate()).intValue(), DailyStatisticTypeEnum.CustomerCount.getValue());
            list.add(todayStatistic);
        }
        return list;
    }

    public List<CustomerProfitRankingViewModel> getCustomerProfitRanking() {
        return contractMapper.getCustomerProfitRanking();
    }

    public List<IndustryProfitRankingViewModel> industryProfitRanking() {
        return customerMapper.industryProfitRanking();
    }

    public List<CityProfitRankingViewModel> getCityProfitRanking() {
        return contractMapper.getCityProfitRanking();
    }
}
