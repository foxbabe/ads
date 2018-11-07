package com.sztouyun.advertisingsystem.repository.advertisement;

import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementOperationLog;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

public interface AdvertisementOperationLogRepository extends BaseRepository<AdvertisementOperationLog> {
    AdvertisementOperationLog findByAdvertisementIdAndOperationAndSuccessed(String advertisementId,Integer operation,boolean successed);
}
