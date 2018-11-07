package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseRecordModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class HistoricalParamConfig extends BaseRecordModel {
    /**
     * 所属分组
     */
    @Column(nullable = false)
    private Integer configGroup;

    /**
     * 参数类型
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 比较符
     */
    @Column(name = "comparison_type")
    private Integer comparisonType;

    /**
     * 标准值
     */
    @Column(nullable = false)
    private Double  value;

    /**
     * 单位
     */
    private Integer unit;

    @Column(nullable = false)
    private Date updatedTime;

    /**
     * 配置所属方ID
     */
    @Column(nullable = false, columnDefinition = "varchar(36) default ''")
    private String objectId;


}
