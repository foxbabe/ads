package com.sztouyun.advertisingsystem.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * 门店周边信息
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreNearBy {
    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(name = "property", value = "storeInfo"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private StoreInfo storeInfo;

    /**
     * 商业区数量
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer commercialDistrictCount = 0;

    /**
     * 居民区数量
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer residentialDistrictCount = 0;

    /**
     * 学校数量
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer schoolDistrictCount = 0;

    /**
     * 主要环境类型 EnvironmentTypeEnum
     */
    @Column(nullable = false,columnDefinition = "int default 0")
    private Integer mainEnvironmentType;

    @Column
    private Date updatedTime;

    public void updateMainEnvironmentType(){
        List<Integer> list= new ArrayList(){{
            add(commercialDistrictCount);
            add(residentialDistrictCount);
            add(schoolDistrictCount);
        }};
        Integer max= list.stream().max(Comparator.naturalOrder()).get();
        if(list.stream().filter(a->a.equals(max)).count()>1){
            mainEnvironmentType=EnvironmentTypeEnum.Others.getValue();
            return;
        }
        if(max.equals(commercialDistrictCount)){
            mainEnvironmentType=EnvironmentTypeEnum.CommercialDistrict.getValue();
        }else if(max.equals(residentialDistrictCount)){
            mainEnvironmentType=EnvironmentTypeEnum.ResidentialDistrict.getValue();
        }else {
            mainEnvironmentType=EnvironmentTypeEnum.SchoolDistrict.getValue();
        }
    }
}
