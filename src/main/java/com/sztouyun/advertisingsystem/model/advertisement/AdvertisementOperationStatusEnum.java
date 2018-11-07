package com.sztouyun.advertisingsystem.model.advertisement;

import com.sztouyun.advertisingsystem.common.EnumMessage;

public enum AdvertisementOperationStatusEnum implements EnumMessage<Integer> {


    All(0,null,true,"全部状态"),
    Submit(1,AdvertisementOperationEnum.Submit,true,"提交审核"),
    FinishAuditing(2,AdvertisementOperationEnum.Auditing,true,"审核通过"),
    RejectAuditing(3,AdvertisementOperationEnum.Auditing,false,"审核驳回"),
    Delivery(4,AdvertisementOperationEnum.Delivery,true,"投放中"),
    Discontinue(5,AdvertisementOperationEnum.Finish,false,"已下架"),
    Finish(6,AdvertisementOperationEnum.Finish,true,"广告完成"),
    PendingCommit(7,AdvertisementOperationEnum.Edit,true,"待提交");



    private Integer value;
    private String displayName;
    private Integer operation;
    private boolean successed;


    AdvertisementOperationStatusEnum(Integer value, AdvertisementOperationEnum operation, boolean successed, String displayName) {
        this.value = value;
        this.operation=operation==null?null:operation.getValue();
        this.successed=successed;
        this.displayName = displayName;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Integer getOperation() {
        return operation;
    }


    public boolean getSuccessed() {
        return successed;
    }

}
