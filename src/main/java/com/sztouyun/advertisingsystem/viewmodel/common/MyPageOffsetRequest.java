package com.sztouyun.advertisingsystem.viewmodel.common;

public class MyPageOffsetRequest extends BaseAuthenticationInfo {

    private Integer offset;

    private Integer pageSize;

    private Integer topCount;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public MyPageOffsetRequest() {

    }

    public MyPageOffsetRequest(Integer offset, Integer pageSize) {
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public Integer getTopCount() {
        return topCount;
    }

    public void setTopCount(Integer topCount) {
        this.topCount = topCount;
    }
}
