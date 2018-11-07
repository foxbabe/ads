package com.sztouyun.advertisingsystem.repository.job;


import com.sztouyun.advertisingsystem.model.job.StoreSyncLog;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface StoreSyncLogRepository extends BaseRepository<StoreSyncLog> {
    StoreSyncLog findFirstBySuccessedOrderByCreatedTimeDesc(boolean successed);

    StoreSyncLog findFirstBySuccessedAndJobNameOrderByCreatedTimeDesc(boolean successed,String jobName);
}