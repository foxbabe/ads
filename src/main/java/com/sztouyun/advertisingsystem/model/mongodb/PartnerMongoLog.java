package com.sztouyun.advertisingsystem.model.mongodb;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartnerMongoLog {
    /**
     * 第三方ID
     */
    private String partnerId;

    /**
     * 上次成功时间
     */
    private Long successTime;

    /**
     *日志类型  PartnerMongoLogTypeEnum
     */
    private Integer logType;

    public PartnerMongoLog(String partnerId, Long successTime, Integer logType) {
        this.partnerId = partnerId;
        this.successTime = successTime;
        this.logType = logType;
    }
}
