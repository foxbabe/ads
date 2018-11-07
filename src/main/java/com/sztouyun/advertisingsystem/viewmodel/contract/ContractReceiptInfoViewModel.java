package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@ApiModel
public class ContractReceiptInfoViewModel {

    @ApiModelProperty(value = "乙方户名", required = true)
    @NotBlank(message = "乙方用户不能为空")
    @Size(max = 128,message = "乙方用户名超过长度")
    private String name;

    @ApiModelProperty(value = "乙方开户行", required = true)
    @NotBlank(message = "乙方开户行不能为空")
    @Size(max = 128,message = "乙方开户行超过长度")
    private String bank;

    @ApiModelProperty(value = "乙方开户账号", required = true)
    @Pattern(regexp = Constant.REGEX_BANK_NUMBER, message = "账号格式不正确！")
    @Size(max = 30,message = "乙方开户账号超过长度")
    @NotBlank(message = "乙方开户账号不能为空")
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
