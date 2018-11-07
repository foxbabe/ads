package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.*;

@Entity
public class Preview extends BaseModel {

    @Column(name = "contract_id",updatable = false,length = 36)
    private String  contractId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id",insertable = false,updatable = false)
    private Contract contract;
    @Column(nullable = false, length = 128)
    private String  filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }


}
