package com.sztouyun.advertisingsystem.model.contract;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.model.customerStore.CustomerStorePlan;
import com.sztouyun.advertisingsystem.viewmodel.contract.ContractAdvertisementPositionConfigViewModel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class ContractExtension {

    @Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "contract"))
    @Column(name = "id", nullable = false, unique = true, length = 36)
    private String id;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private Contract contract;

    /**
     * 甲方名称
     */
    @Column(nullable = false, length = 128)
    private String firstPartyName;

    /**
     * 甲方责任联系人
     */
    @Column(nullable = false, length = 128)
    private String firstPartyResponsibilityPerson;

    /**
     * 甲方合同接收地址
     */
    @Column(nullable = false, length = 128)
    private String firstPartyContractReceiveAddress;

    /**
     * 甲方邮件
     */
    @Column(nullable = false, length = 128)
    private String firstPartyEmail;

    /**
     * 甲方电话
     */
    @Column(nullable = false, length = 20)
    private String firstPartyPhone;

    /**
     * 乙方名称
     */
    @Column(nullable = false, length = 128)
    private String secondPartyName;

    /**
     * 乙方责任联系人
     */
    @Column(nullable = false, length = 128)
    private String secondPartyResponsibilityPerson;

    /**
     * 乙方合同接收地址
     */
    @Column(nullable = false, length = 128)
    private String secondPartyContractReceiveAddress;

    /**
     * 乙方邮件
     */
    @Column(nullable = false, length = 128)
    private String secondPartyEmail;

    /**
     * 乙方电话
     */
    @Column(nullable = false, length = 20)
    private String secondPartyPhone;

    /**
     * 媒体费用
     */
    @Column(nullable = false)
    private Double mediumCost;


    /**
     * 制作费用
     */
    @Column(columnDefinition = "double default 0")
    private Double productCost = 0D;

    /**
     * 前述费用支付天数
     */
    private Integer signAfterDay;

    /**
     * 银行账户名称
     */
    @Column(nullable = false, length = 128)
    private String bankAccountName;

    /**
     * 银行账户号码
     */
    @Column(nullable = false, length = 30)
    private String bankAccountNumber;

    /**
     * 银行名称
     */
    @Column(nullable = false, length = 128)
    private String bankName;

    /**
     * 合作周期开始时间
     */
    @Column(nullable = false)
    private Date startTime;

    /**
     * 合作周期截止时间
     */
    @Column(nullable = false)
    private Date endTime;


    /**
     * 总天数
     */
    @Column(nullable = false)
    private Integer totalDays;

    /**
     * 备注
     */
    @Column(length = 2000)
    private String remark;

    /**
     * A类门店数量
     */
    @Column
    private Integer storeACount;

    /**
     * B类门店
     */
    @Column
    private Integer storeBCount;

    /**
     * C类门店
     */
    @Column
    private Integer storeCCount;

    /**
     * D类门店
     */
    @Column
    private Integer storeDCount;


    @Transient
    private Integer storeACountChecked;

    @Transient
    private Integer storeBCountChecked;

    @Transient
    private Integer storeCCountChecked;

    @Transient
    private Integer storeDCountChecked;

    @Column
    private Date saveTime;

    @Column(nullable = false, columnDefinition = "double default 1")
    private Double discount;

    /**
     * 合同补充条款
     */
    @Column(length = 2000)
    private String supplementaryTerms;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="contractExtension")
    private List<ContractAdvertisementPositionConfig> contractAdvertisementPositionConfigs;

    @Transient
    private List<ContractAdvertisementPositionConfigViewModel> contractAdvertisementPositionConfigViewModels = new ArrayList<>();


    /**
     * 是否含有收银机设备
     */
    @Column(nullable = false,columnDefinition = "tinyint(1) default 1")
    private Boolean hasCashRegister;

    /**
     * 客户选点id
     */
    @Column(name = "customer_store_plan_id",length = 36)
    private String customerStorePlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_store_plan_id",insertable = false,updatable = false)
    private CustomerStorePlan customerStorePlan;

    /**
     * 客户选点编号
     */
    @Column(nullable = false)
    private String code = "";

    /**
     * 客户选点门店数
     */
    @Column(nullable = false)
    private Integer storePlanCount = 0;

    public Double getProductCost() {
        return productCost == null ? 0 : productCost;
    }

    public Integer getSignAfterDay() {
        return signAfterDay==null?0:signAfterDay;
    }

    /**
     * 获取费用总数
     * @return
     */
    public double getTotalCost(){
        BigDecimal mediumCostDecimal = BigDecimal.valueOf(this.mediumCost);
        BigDecimal productCostDecimal = BigDecimal.valueOf(this.productCost == null ? 0 : this.productCost);
        BigDecimal discountDecimal = BigDecimal.valueOf(this.discount);
        return mediumCostDecimal.add(productCostDecimal).multiply(discountDecimal).setScale(Constant.SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取所有门店总数
     * @return
     */
    public Integer getTotalStoreCount() {
        return this.storeACount + this.storeBCount + this.storeCCount+this.storeDCount;
    }
}
