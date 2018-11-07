package com.sztouyun.advertisingsystem.viewmodel.thirdpart.store;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewStoreOrderInfo implements Serializable {

    @JsonProperty("StoreNo")
    private String storeNo;

    @JsonProperty("OrderDate")
    private Date orderDate;

    @JsonProperty("OrderQty")
    private Integer orderQty;
}






