package com.sztouyun.advertisingsystem.viewmodel.message;

import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MessagePageInfoPackage {

    @ApiModelProperty(value = "分页数据")
    private PageList<? extends MessageViewModel> pageInfoItemPageList;

    @ApiModelProperty(value = "是否含有未读消息")
    private Boolean hasUnReadMessage;

    public PageList<? extends MessageViewModel> getPageInfoItemPageList() {
        return pageInfoItemPageList;
    }

    public void setPageInfoItemPageList(PageList<? extends MessageViewModel> pageInfoItemPageList) {
        this.pageInfoItemPageList = pageInfoItemPageList;
    }

    public Boolean getHasUnReadMessage() {
        return hasUnReadMessage;
    }

    public void setHasUnReadMessage(Boolean hasUnReadMessage) {
        this.hasUnReadMessage = hasUnReadMessage;
    }
}
