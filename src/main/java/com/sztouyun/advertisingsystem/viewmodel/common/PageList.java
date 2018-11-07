package com.sztouyun.advertisingsystem.viewmodel.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
public class PageList<T> extends BasePageInfo {
    @ApiModelProperty(value = "记录总数", required = true)
    private Long totalElement = 0L;

    @ApiModelProperty(value = "总页数", required = true)
    private Integer totalPageSize  = 0;

    @ApiModelProperty(value = "数据列表", required = true)
    private List<T> list = new ArrayList<>();

    @ApiModelProperty(value = "列表已选择记录数")
    private Integer chooseCount = 0;

    @ApiModelProperty(value = "总金额")
    private String totalAmount;

    @ApiModelProperty(value = "结算月份，用于编辑回显默认选中")
    @JsonFormat(pattern = Constant.DATA_YM, timezone = "GMT+8")
    private Date settledMonth;

    @ApiModelProperty(value = "是否可以新建结算")
    private Boolean canCreate=Boolean.TRUE;

    public PageList() {
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getChooseCount() {
        return chooseCount;
    }

    public void setChooseCount(Integer chooseCount) {
        this.chooseCount = chooseCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSettledMonth() {
        return settledMonth;
    }

    public void setSettledMonth(Date settledMonth) {
        this.settledMonth = settledMonth;
    }

    public Boolean getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(Boolean canCreate) {
        this.canCreate = canCreate;
    }
}
