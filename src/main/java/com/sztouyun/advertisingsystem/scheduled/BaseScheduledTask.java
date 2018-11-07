package com.sztouyun.advertisingsystem.scheduled;

import com.sztouyun.advertisingsystem.model.job.QScheduledJob;
import com.sztouyun.advertisingsystem.repository.job.ScheduledJobRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BaseScheduledTask  extends BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduledJobRepository scheduledJobRepository;

    private final static QScheduledJob qScheduledJob = QScheduledJob.scheduledJob;

    protected <T> Page<T> pageResult(List<T> resultList, Pageable pageable, long total){
        return  new PageImpl<>(resultList, pageable, total);
    }

    protected Date getLastSucceedDate(String jobName) {
        return scheduledJobRepository.findOne(q -> q.select(qScheduledJob.createdTime).from(qScheduledJob)
                .where(qScheduledJob.jobName.eq(jobName).and(qScheduledJob.isSuccessed.isTrue()))
                .orderBy(qScheduledJob.createdTime.desc())
                .limit(1)
        );
    }
}
