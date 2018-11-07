package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.ITree;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/***
 * 数据字典表
 */
@Data
@Entity
public class DataDict implements ITree {
    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();

    /**
     * 名称
     */
    @Column(name = "name",nullable = false,length = 50)
    private String name;

    /**
     * 数据字典类型  {@link DataDictTypeEnum}
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 层级
     */
    @Column(name = "level",nullable = false,length = 4)
    private Integer level;

    /**
     * 父ID 
     */
    @Column(name = "parent_id",nullable = false,length = 36)
    private String parentId;
}
