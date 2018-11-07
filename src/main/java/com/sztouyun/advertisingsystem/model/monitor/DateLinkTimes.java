package com.sztouyun.advertisingsystem.model.monitor;

import lombok.Data;

import java.util.Date;

@Data
public class DateLinkTimes {
    /**
     * 日期
     */
    private Date date;

    /**
     * 素材链接类型 {@link com.sztouyun.advertisingsystem.model.system.MaterialLinkTypeEnum}
     */
    private Integer linkType;

    /**
     * 点击/扫码 次数
     */
    private Long totalTimes;
}
