package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UpdateDailyProfitAmountViewModel {
    private String partnerId;

    private Double price;

    private Date date;

    public UpdateDailyProfitAmountViewModel(String partnerId, Double price, Date date) {
        this.partnerId = partnerId;
        this.price = price;
        this.date = date;
    }
}
