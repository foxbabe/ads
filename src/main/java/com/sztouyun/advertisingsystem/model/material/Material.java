package com.sztouyun.advertisingsystem.model.material;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Material extends BaseModel {

    @Column(nullable = false, length = 255)
    private String data;

    @Column(nullable = false, length = 36)
    private String customerId;

    @Column(nullable = false)
    private Integer materialType;

    @Column(nullable = false, length = 255)
    private String fileName = "";

    @Column(length = 50)
    private String materialSpecification = "";

    @Column(length = 50)
    private String materialSize = "";

    public String getMaterialSpecification() {
        return materialSpecification;
    }

    public void setMaterialSpecification(String materialSpecification) {
        this.materialSpecification = materialSpecification;
    }

    public String getMaterialSize() {
        return materialSize;
    }

    public void setMaterialSize(String materialSize) {
        this.materialSize = materialSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }
}
