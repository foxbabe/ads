package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseAutoKeyEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 梯度参数配置
 */
@Data
@Table(indexes = {
        @Index(name = "index_object_id",columnList = "object_id")})
public class GradientParamConfig extends BaseAutoKeyEntity {

    /**
     * 归属对象
     */
    @Column(name = "object_id",nullable = false)
    private String objectId ="";

    /**
     * 维度
     */
    @Column
    private String dimension="";

    /**
     * 类别类型  @link GradientParamConfigEnum
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 左边界值
     */
    @Column
    private Double leftBound =0D;

    /**
     * 左侧运算符 ComparisonTypeEnum
     */
    @Column
    private Integer leftSymbol;

    /**
     * 右侧运算符 ComparisonTypeEnum
     */
    @Column
    private Integer rightSymbol;

    /**
     * 右边界值
     */
    @Column
    private Double rightBound =999999999999D;

    /**
     * 值
     */
    @Column
    private Double value =0D;
}