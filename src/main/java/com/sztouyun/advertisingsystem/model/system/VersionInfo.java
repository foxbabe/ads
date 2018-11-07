package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.model.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class VersionInfo extends BaseEntity {
    /**
     * 版本号
     */
    @Column(unique = true, nullable = false)
    private String versionNumber;

    /**
     * 版本内容
     */
    @Column(nullable = false, length = 10000)
    private String versionContent;

    /**
     * 是否弹窗提醒
     */
    @Column(nullable = false, columnDefinition = "bit(1) default 1")
    private Boolean isTip = true;
}
