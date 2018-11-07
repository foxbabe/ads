package com.sztouyun.advertisingsystem.viewmodel.order;

import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.order.OrderOperationStatusEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.AdvertisementOperationInfoViewModel;
import io.swagger.annotations.ApiModel;
import lombok.experimental.var;

/**
 * Created by wenfeng on 2018/2/8.
 */
@ApiModel
public class OrderOperationViewModel extends AdvertisementOperationInfoViewModel {
    @EnumValue(enumClass = OrderOperationStatusEnum.class)
    private Integer operationStatus;

    @Override
    public Integer getOperationStatus() {
        return operationStatus;
    }

    @Override
    public void setOperationStatus(Integer operationStatus,boolean successed) {
        var orderOperationStatusEnums = EnumUtils.getAllItems( OrderOperationStatusEnum.class);
        for(OrderOperationStatusEnum orderOperationStatusEnum:orderOperationStatusEnums){
            if(operationStatus.equals(orderOperationStatusEnum.getOperation()) && orderOperationStatusEnum.getSuccessed()==successed){
                this.operationStatus = orderOperationStatusEnum.getValue();
                setOperationStatusName(orderOperationStatusEnum.getDisplayName());
            }
        }
    }
}
