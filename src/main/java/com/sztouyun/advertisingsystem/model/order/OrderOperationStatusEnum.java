package com.sztouyun.advertisingsystem.model.order;

import com.sztouyun.advertisingsystem.common.EnumMessage;

/**
 * Created by wenfeng on 2018/2/8.
 */
public enum OrderOperationStatusEnum implements EnumMessage<Integer> {
    All(0,null,true,"全部状态"),
    PendingPublish(1,OrderOperationEnum.Create,true,"待上刊"),
    PublishAuditing(2,OrderOperationEnum.SubmitDeliveryAuditing,true,"待上刊审核"),
    PendingDelivery(3,OrderOperationEnum.DeliveryAuditing,true,"待投放"),
    RejectAuditing(4,OrderOperationEnum.DeliveryAuditing,false,"审核失败"),
    Delivering(5,OrderOperationEnum.AutoDelivery,true,"投放中"),
    Cancel(6,OrderOperationEnum.Cancel,true,"已取消"),
    TakeOff(7,OrderOperationEnum.TakeOff,true,"已下架"),
    Finish(8,OrderOperationEnum.Finish,true,"已完成");

    private Integer value;
    private String displayName;
    private Integer operation;
    private boolean successed;


    OrderOperationStatusEnum(Integer value, OrderOperationEnum operation, boolean successed, String displayName) {
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
