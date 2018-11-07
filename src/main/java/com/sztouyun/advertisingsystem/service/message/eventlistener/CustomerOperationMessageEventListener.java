package com.sztouyun.advertisingsystem.service.message.eventlistener;

import com.sztouyun.advertisingsystem.model.customer.CustomerOperationLog;
import com.sztouyun.advertisingsystem.model.customer.CustomerOperationTypeEnum;
import com.sztouyun.advertisingsystem.model.message.MessageCategoryEnum;
import com.sztouyun.advertisingsystem.model.message.MessageTypeEnum;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.customer.CustomerService;
import com.sztouyun.advertisingsystem.service.message.event.CustomerOperationEvent;
import com.sztouyun.advertisingsystem.service.message.eventlistener.base.BaseMessageEventListener;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.viewmodel.message.customer.CustomerMessageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerOperationMessageEventListener extends BaseMessageEventListener<CustomerOperationEvent,CustomerOperationLog,CustomerMessageViewModel> {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Override
    protected String getObjectId(CustomerOperationLog customerOperationLog) {
        return customerOperationLog.getId();
    }

    @Override
    protected MessageTypeEnum getMessageType(CustomerOperationLog customerOperationLog) {
        return MessageTypeEnum.Customer;
    }

    @Override
    protected MessageCategoryEnum getMessageCategory(CustomerOperationLog customerOperationLog) {
        CustomerOperationTypeEnum customerOperationTypeEnum= EnumUtils.toEnum(customerOperationLog.getOperation(),CustomerOperationTypeEnum.class);
        switch (customerOperationTypeEnum){
            case Distribute:
                return MessageCategoryEnum.DistributeCustomer;
            case Delete:
                return MessageCategoryEnum.DeleteCustomer;
            case Edit:
                return MessageCategoryEnum.EditCustomer;
            default:
                return null;
        }
    }

    @Override
    protected CustomerMessageViewModel getMessageViewModel(CustomerOperationLog customerOperationLog) {
        String creatorName=userService.getNicknameFromCache(customerOperationLog.getCreatorId());
        String customerName=customerService.getCustomerNameFromCache(customerOperationLog.getCustomerId());
        String oldOwnerId=customerOperationLog.getOldOwnerId();
        String newOwnerId=customerOperationLog.getNewOwnerId();
        String oldOwnerName=userService.getNicknameFromCache(oldOwnerId);
        String newOwnerName=userService.getNicknameFromCache(newOwnerId);
        CustomerMessageViewModel customerMessageViewModel = (CustomerMessageViewModel)getMessageCategory(customerOperationLog).getMessageViewModel();
        customerMessageViewModel.setCustomerId(customerOperationLog.getCustomerId());
        customerMessageViewModel.setOwnerIds(oldOwnerId,newOwnerId);
        customerMessageViewModel.setNames(creatorName,customerName,oldOwnerName,newOwnerName);
        return customerMessageViewModel;
    }

    @Override
    protected List<String> getMessageReceiverIds(CustomerOperationLog customerOperationLog) {
        List<String> receiverIds=new ArrayList<>();
        if(CustomerOperationTypeEnum.Distribute.getValue().equals(customerOperationLog.getOperation()) && customerOperationLog.getOldOwnerId()!=null){
            receiverIds.add(customerOperationLog.getOldOwnerId());
        }
        if(customerOperationLog.getNewOwnerId()!=null){
            receiverIds.add(customerOperationLog.getNewOwnerId());
        }
        return receiverIds;
    }
}
