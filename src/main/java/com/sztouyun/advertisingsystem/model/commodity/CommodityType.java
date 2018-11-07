package com.sztouyun.advertisingsystem.model.commodity;


import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class CommodityType extends BaseAutoKeyEntity{
    @Column(nullable = false, length = 128)
    private String name;
}
