package com.sztouyun.advertisingsystem.service.task.advertisement.data;

import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.service.task.BaseTaskData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.util.Date;

@Data
public class AdvertisementStoreInfo extends BaseTaskData {
    private Advertisement advertisement;
    private StoreInfo storeInfo;

    public AdvertisementStoreInfo(Advertisement advertisement, StoreInfo storeInfo) {
        super(LocalDate.now().toDate());
        this.advertisement = advertisement;
        this.storeInfo = storeInfo;
    }

    public AdvertisementStoreInfo(Advertisement advertisement, StoreInfo storeInfo,Date date) {
        super(date);
        this.advertisement = advertisement;
        this.storeInfo = storeInfo;
    }
}
