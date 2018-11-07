package com.sztouyun.advertisingsystem.viewmodel.adProfitShare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by szty on 2018/8/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreValidInfoRequest {
    private String advertisementId;
    private List<String> storeNos;
}
