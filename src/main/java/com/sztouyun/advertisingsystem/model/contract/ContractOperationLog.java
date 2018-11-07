package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@Data
@Entity
@NoArgsConstructor
public class ContractOperationLog extends BaseModel {
    public ContractOperationLog(String contractId, Integer operation, boolean successed, String remark) {
        this.contractId = contractId;
        this.operation = operation;
        this.successed = successed;
        this.remark = remark;
    }

    /**
     * 对应合同id
     */
    @Column(name = "contract_id", nullable = false, length = 36)
    private String contractId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", insertable = false, updatable = false)
    private Contract contract;

    /**
     * 操作
     */
    @Column(nullable = false)
    private Integer operation;

    /**
     * 是否成功
     */
    @Column(nullable = false)
    private boolean successed;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    @Transient
    private String advertisementId;

    @Transient
    private Iterable<Advertisement> unfinishedAdvertisements = new ArrayList<>();

    public ContractOperationEnum getContractOperationEnum() {
        return EnumUtils.toEnum(operation, ContractOperationEnum.class);
    }

    public boolean isFinishAuditing() {
        if (getContractOperationEnum().equals(ContractOperationEnum.TerminationAuditing))
            return true;
        if (getContractOperationEnum().equals(ContractOperationEnum.Finish) && !isSuccessed())
            return true;
        return false;
    }
}
