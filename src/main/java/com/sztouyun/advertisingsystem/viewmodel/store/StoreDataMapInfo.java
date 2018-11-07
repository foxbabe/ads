package com.sztouyun.advertisingsystem.viewmodel.store;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class StoreDataMapInfo {

    private Double leftTopLongitude;

    private Double leftTopLatitude;

    private Double rightTopLongitude;

    private Double rightTopLatitude;

    private Double leftBottomLongitude;

    private Double leftBottomLatitude;

    private Double rightBottomLongitude;

    private Double rightBottomLatitude;

    public StoreDataMapInfo(Double leftTopLongitude, Double leftTopLatitude, Double rightTopLongitude, Double rightTopLatitude, Double leftBottomLongitude, Double leftBottomLatitude, Double rightBottomLongitude, Double rightBottomLatitude) {
        this.leftTopLongitude = leftTopLongitude;
        this.leftTopLatitude = leftTopLatitude;
        this.rightTopLongitude = rightTopLongitude;
        this.rightTopLatitude = rightTopLatitude;
        this.leftBottomLongitude = leftBottomLongitude;
        this.leftBottomLatitude = leftBottomLatitude;
        this.rightBottomLongitude = rightBottomLongitude;
        this.rightBottomLatitude = rightBottomLatitude;
    }
}