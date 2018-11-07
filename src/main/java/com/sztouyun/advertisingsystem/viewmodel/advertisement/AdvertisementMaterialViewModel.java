package com.sztouyun.advertisingsystem.viewmodel.advertisement;

public class AdvertisementMaterialViewModel {

    private String url;

    private String positionType;

    public AdvertisementMaterialViewModel() {}

    public AdvertisementMaterialViewModel(String url, String positionType) {
        this.url = url;
        this.positionType = positionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }
}
