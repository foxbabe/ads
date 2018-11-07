package com.sztouyun.advertisingsystem.service.statistic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.ContractSignTendencyStatistic;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatistic;
import com.sztouyun.advertisingsystem.viewmodel.statistic.SummaryStatisticTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContractStatisticService extends BaseService {

    @Autowired
    private ContractRepository contractRepository;

    private final QContract qContract = QContract.contract;

    public SummaryStatistic getContractIncrementStatistics(Date startTime, Date endTime) {

        NumberExpression<Integer> incrementCaseWhen = new CaseBuilder().when(qContract.signTime.goe(startTime).and(qContract.signTime.lt(endTime))).then(1).otherwise(0);
        NumberExpression<Integer> pastTotalsCaseWhen = new CaseBuilder().when(qContract.signTime.lt(startTime)).then(1).otherwise(0);
        NumberExpression<Integer> totalCaseWhen = new CaseBuilder().when(qContract.signTime.lt(endTime)).then(1).otherwise(0);

        List<Integer> contractStatus = new ArrayList<Integer>() {{
            add(ContractStatusEnum.PendingExecution.getValue());
            add(ContractStatusEnum.AbruptlyTerminated.getValue());
            add(ContractStatusEnum.Finished.getValue());
            add(ContractStatusEnum.Executing.getValue());
        }};

        SummaryStatistic summaryStatistic = contractRepository.findOneAuthorized(queryFactory -> queryFactory.select(Projections.bean(SummaryStatistic.class,
                qContract.id.count().as("total"),
                totalCaseWhen.sum().as("totalBeforeZeroTime"),
                incrementCaseWhen.sum().as("increment"),
                pastTotalsCaseWhen.sum().as("oneWeekAgoTotal")
                ))
                .from(qContract)
                .where(qContract.contractStatus.in(contractStatus).and(AuthenticationService.getUserAuthenticationFilter(qContract.signerId)))
        );
        summaryStatistic.setStatisticType(SummaryStatisticTypeEnum.CONTRACT.getValue());
        summaryStatistic.setStartDate(startTime);
        summaryStatistic.setEndDate(endTime);
        return summaryStatistic;
    }

    public List<ContractSignTendencyStatistic> contractSignTendencyStatistic(Date startTime, Date endTime) {
        List<ContractSignTendencyStatistic> contractSignTendencyStatisticList = contractRepository.findAllAuthorized(queryFactory -> queryFactory.select(Projections.bean(ContractSignTendencyStatistic.class,
                qContract.signTime.as("signTime"),
                qContract.id.count().as("total")
                ))
                .from(qContract)
                .where(qContract.signTime.goe(startTime).and(qContract.signTime.lt(endTime)).and(AuthenticationService.getUserAuthenticationFilter(qContract.signerId)))
                .groupBy(Expressions.dateOperation(Date.class, Ops.DateTimeOps.DATE, new Expression[]{qContract.signTime}))
        );
        return contractSignTendencyStatisticList;
    }

	 public List<DistributionStatisticDto> getContractStatusStatistics() {
        List<DistributionStatisticDto> contractStatusList = contractRepository.findAllAuthorized(queryFactory -> queryFactory
                .select(Projections.bean(DistributionStatisticDto.class,
                        qContract.contractStatus.as("keyValue"),
                        qContract.id.count().as("value")))
                .from(qContract)
                .where(qContract.contractStatus.ne(ContractStatusEnum.PendingCommit.getValue()))
                .groupBy(qContract.contractStatus));
        return contractStatusList;
    }

    public Long getContractCount(){
        return contractRepository.countAuthorized(q->q.select(qContract)
            .from(qContract).where(qContract.contractStatus.ne(ContractStatusEnum.PendingCommit.getValue())));
    }

}
