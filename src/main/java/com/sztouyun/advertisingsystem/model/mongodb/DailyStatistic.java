package com.sztouyun.advertisingsystem.model.mongodb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by szty on 2018/9/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DailyStatistic {
    private Long date;
    @ApiModelProperty(value = "数量")
    private Integer count;

    private Integer statisticType;

    @ApiModelProperty(value = "客户创建时间")
    @JsonFormat(pattern = Constant.TIME_DAY, timezone = "GMT+8")
    public Date getCreatedDate() {
        return new Date(date);
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
