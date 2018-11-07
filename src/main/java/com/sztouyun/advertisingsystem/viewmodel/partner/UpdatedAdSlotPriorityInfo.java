package com.sztouyun.advertisingsystem.viewmodel.partner;

import lombok.Data;

import java.util.Date;

@Data
public class UpdatedAdSlotPriorityInfo {

    private Integer startPriority;

    private Integer endPriority;

    private int movedStep;

    private Date updatedTime;

    private String updaterId;
}
