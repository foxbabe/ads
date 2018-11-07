package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;

import javax.persistence.*;

/**
 * Created by szty on 2017/7/25.
 */
@Entity
public class ContractReceiptInfo extends BaseModel {
    /**
     *乙方用户
     */
    @Column(nullable = false,length=128)
    private String name;
    /**
     * 乙方开户行
     */
    @Column(nullable = false,length=128)
    private String bank;
    /**
     * 乙方开户账号
     */
    @Column(nullable = false,length=30)
    private String bankAccount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}
