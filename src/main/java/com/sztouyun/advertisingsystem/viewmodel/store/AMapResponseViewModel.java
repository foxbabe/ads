package com.sztouyun.advertisingsystem.viewmodel.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AMapResponseViewModel {
    private Integer count = 0;

    @JsonProperty("infocode")
    private Integer infoCode;
}
