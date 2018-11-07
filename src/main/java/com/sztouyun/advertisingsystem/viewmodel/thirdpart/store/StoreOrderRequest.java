package com.sztouyun.advertisingsystem.viewmodel.thirdpart.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderRequest {
    private Date date;
    private List<String> shopIds;//门店ID
}
