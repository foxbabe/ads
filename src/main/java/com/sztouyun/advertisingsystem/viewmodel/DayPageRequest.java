package com.sztouyun.advertisingsystem.viewmodel;

import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by szty on 2018/8/14.
 */
@Data
@AllArgsConstructor
public class DayPageRequest extends BasePageInfo{
    private Date date;
}
