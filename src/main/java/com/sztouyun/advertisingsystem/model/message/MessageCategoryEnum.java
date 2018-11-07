package com.sztouyun.advertisingsystem.model.message;

import com.sztouyun.advertisingsystem.common.EnumMessage;
import com.sztouyun.advertisingsystem.viewmodel.message.MessageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.advertisement.AdvertisementMessageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.contract.ContractMessageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.customer.CustomerMessageViewModel;
import com.sztouyun.advertisingsystem.viewmodel.message.customer.DistributeCustomerMessageViewModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCategoryEnum implements EnumMessage<Integer> {
    DistributeCustomer(1, "分配客户", DistributeCustomerMessageViewModel.class),
    EditCustomer(2, "编辑客户信息", CustomerMessageViewModel.class),
    DeleteCustomer(3, "删除客户", CustomerMessageViewModel.class),

    SubmitContract(100, "合同提交审批", ContractMessageViewModel.class),
    AuditContractSucceed(101, "合同审核通过", ContractMessageViewModel.class),
    AuditContractFailed(102, "合同审核驳回", ContractMessageViewModel.class),
    SignContractSucceed(103, "合同签约成功", ContractMessageViewModel.class),
    SignContractFailed(104, "合同签约失败", ContractMessageViewModel.class),
    SubmitTerminatedAuditContract(105, "合同提交终止审核", ContractMessageViewModel.class),
    TerminatedAuditContractSucceed(106, "合同终止审核成功", ContractMessageViewModel.class),
    TerminatedAuditContractFailed(107, "合同终止审核驳回", ContractMessageViewModel.class),
    TerminateContract(108, "合同意外终止", ContractMessageViewModel.class),
    FinishedContract(109, "合同执行完成", ContractMessageViewModel.class),

    SubmitAdvertisement(200, "广告提交审核", AdvertisementMessageViewModel.class),
    FinishAuditingAdvertisement(201, "广告审核通过", AdvertisementMessageViewModel.class),
    RejectAuditingAdvertisement(202, "广告审核驳回", AdvertisementMessageViewModel.class),
    DeliveryAdvertisement(203, "广告投放", AdvertisementMessageViewModel.class),
    AutoTakeOffAdvertisementAndFinishContract(205, "广告自动下架(合同执行完成)", AdvertisementMessageViewModel.class),
    ManualTakeOffAdvertisementAndFinishContract(206, "广告非自动下架(合同广告投放完成)", AdvertisementMessageViewModel.class),
    ManualTakeOffAdvertisement(207, "广告非自动下架(下架广告继续投放)", AdvertisementMessageViewModel.class),
    FinishAdvertisement(208, "广告自动完成", AdvertisementMessageViewModel.class);

    private Integer value;
    private String displayName;
    private Class<? extends MessageViewModel> contentClass;

    public MessageViewModel getMessageViewModel() {
        try {
            return contentClass.newInstance();
        } catch (Exception e) {
            return new MessageViewModel();
        }
    }
}
