package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.model.BaseModel;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Contract extends BaseModel {
    /**
     * 合同编码
     */
    @Column(nullable = false, length = 128)
    private String contractCode;

    /**
     * 合同名称
     */
    @Column(nullable = false, length = 128)
    private String contractName;

    /**
     * 客户id
     */
    @Column(name = "customer_id",updatable = false,length = 36)
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",insertable = false,updatable = false)
    private Customer customer;

    /**
     * 负责人
     */
    @Column(name = "owner_id",updatable = false,length = 36)
    private String ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",insertable = false,updatable = false)
    private User owner;

    /**
     * 签约人
     */
    @Column(name = "signer_id",length = 36)
    private String signerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id",insertable = false, updatable = false)
    private User signer;

    /**
     * 合同状态
     */
    @Column(nullable = false)
    private Integer contractStatus;

    /**
     * 是否审批中
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 0")
    private boolean auditing;

    /**
     * 提交审批的说明
     */
    @Column(length = 2000)
    private String auditingRemark;

    /**
     * 签约时间
     */
    @Column
    private Date signTime;

    /**
     * 合同的收益金额
     */
    @Column(nullable = false, columnDefinition = "double default 0")
    private double totalCost = 0D;

    /**
     * 广告尺寸配置id
     */
    @Transient
    private String advertisementSizeConfigId;

    @Transient
    private String advertisementDurationConfigId;

    @Transient
    private String advertisementDisplayTimesConfigId;

    /**
     * 广告周期（天）
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer contractAdvertisementPeriod =0;

    /**
     * 合同已投放周期（天）
     */
    @Column(nullable = false,columnDefinition = "Integer default  0")
    private Integer usedContractPeriod=0;

    /**
     * 投放平台
     */
    @Column
    private String platform;

    /**
     * 详细的合同信息扩展懒加载
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "contract", fetch = FetchType.LAZY, optional = false)
    private ContractExtension contractExtension = new ContractExtension();

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="contract")
    private List<Advertisement> advertisements;

    /**
     * 合同模板ID
     */
    @Column(name = "contract_template_id",length = 36)
    private String contractTemplateId;

    /**
     * 合同模板懒加载
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_template_id",insertable = false,updatable = false)
    private ContractTemplate contractTemplate;

    public ContractStatusEnum getContractStatusEnum(){
        return EnumUtils.toEnum(contractStatus, ContractStatusEnum.class);
    }

    public boolean isChoseCashRegister() {
        return this.platform.contains(TerminalTypeEnum.CashRegister.getValue().toString());
    }
}
