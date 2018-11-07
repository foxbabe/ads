package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.Between;
import com.sztouyun.advertisingsystem.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class BaseContractViewModel {

    @ApiModelProperty(value = "合同名称", required = true)
    @Size(max = 128, message = "合同名称太长")
    @NotBlank(message = "合同名称不能为空")
    private String contractName;

    @ApiModelProperty(value = "客户id", required = true)
    @NotBlank(message = "客户id不能为空")
    private String customerId;

    @ApiModelProperty(value = "甲方名称", required = true)
    @Size(max = 128, message = "甲方名称太长")
    @NotBlank(message = "甲方名称不能为空")
    private String firstPartyName;

    @ApiModelProperty(value = "甲方责任联系人", required = true)
    @Size(max = 128, message = "甲方责任联系人太长")
    @NotBlank(message = "甲方责任联系人不能为空")
    private String firstPartyResponsibilityPerson;

    @ApiModelProperty(value = "甲方合同接收地址", required = true)
    @Size(max = 128, message = "甲方责任联系人太长")
    @NotBlank(message = "甲方合同接收地址不能为空")
    private String firstPartyContractReceiveAddress;

    @ApiModelProperty(value = "甲方邮件信息", required = true)
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "甲方邮箱格式错误！")
    @Size(max = 128, message = "甲方邮件信息太长")
    @NotBlank(message = "甲方邮件地址不能为空")
    private String firstPartyEmail;

    @ApiModelProperty(value = "甲方电话信息", required = true)
    @Size(max = 20, message = "甲方电话信息太长")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "甲方电话信息错误！")
    private String firstPartyPhone;

    @ApiModelProperty(value = "乙方名称", required = true)
    @Size(max = 128, message = "乙方名称太长")
    @NotBlank(message = "乙方名称不能为空")
    private String secondPartyName;

    @ApiModelProperty(value = "乙方责任联系人", required = true)
    @Size(max = 128, message = "乙方名称太长")
    @NotBlank(message = "乙方责任联系人不能为空")
    private String secondPartyResponsibilityPerson;

    @ApiModelProperty(value = "乙方合同接收地址", required = true)
    @Size(max = 128, message = "乙方合同接收地址太长")
    @NotBlank(message = "乙方合同接收地址不能为空")
    private String secondPartyContractReceiveAddress;

    @ApiModelProperty(value = "乙方邮件信息", required = true)
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "乙方邮箱格式错误！")
    @Size(max = 128, message = "甲方邮件信息太长")
    @NotBlank(message = "乙方邮件地址不能为空")
    private String secondPartyEmail;

    @ApiModelProperty(value = "乙方电话信息", required = true)
    @Size(max = 20, message = "乙方电话信息太长")
    @Pattern(regexp = Constant.REGEX_PHONE, message = "乙方电话信息错误！")
    private String secondPartyPhone;

    @ApiModelProperty(value = "媒体费用", required = true)
    @Max(value = Constant.INTEGER_MAX, message = "媒体费用最大值不能超过999999999")
    @Min(value = 0, message = "媒体费用不能小于0")
    @NotNull(message = "媒体费用不能为空")
    private Double mediumCost;

    @ApiModelProperty(value = "制作费用")
    @Max(value = Constant.INTEGER_MAX, message = "制作费用最大值不能超过999999999")
    @Min(value = 0, message = "制作费用不能小于0")
    private Double productCost;

    @ApiModelProperty(value = "前述费用支付天数")
    @Max(value = Constant.INTEGER_MAX, message = "前述费用支付天数最大值不能超过999999999")
    @Min(value = 0, message = "前述费用支付天数不能小于0")
    private Integer signAfterDay;

    @ApiModelProperty(value = "银行账户名称", required = true)
    @Size(max = 128, message = "银行账户名称太长")
    @NotBlank(message = "银行账户名称不能为空")
    private String bankAccountName;

    @ApiModelProperty(value = "银行账户号码", required = true)
    @Pattern(regexp = Constant.REGEX_BANK_NUMBER, message = "银行账户号码格式错误！")
    @NotBlank(message = "银行账户号码不能为空")
    @Size(max = 30,message = "银行账户号码超过长度")
    private String bankAccountNumber;

    @ApiModelProperty(value = "银行名称", required = true)
    @Size(max = 128, message = "银行名称太长")
    @NotBlank(message = "银行名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "合作周期开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "合作周期开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "合作周期截止时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "合作周期截止时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "合作周期总天数")
    private Integer totalDays;

    @ApiModelProperty(value = "合同广告周期天数")
    @Min(value = 1,message = "广告周期天数至少大于1天")
    private Integer contractAdvertisementPeriod;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000, message = "备注太长")
    private String remark;

    @ApiModelProperty(value = "A类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "A类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "A类门店数量不能小于0")
    @NotNull(message = "A类门店数量不能为空")
    private Integer storeACount;

    @ApiModelProperty(value = "B类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "B类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "B类门店数量不能小于0")
    @NotNull(message = "B类门店数量不能为空")
    private Integer storeBCount;

    @ApiModelProperty(value = "C类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "C类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "C类门店数量不能小于0")
    @NotNull(message = "C类门店数量不能为空")
    private Integer storeCCount;

    @ApiModelProperty(value = "D类门店数量")
    @Max(value = Constant.INTEGER_MAX, message = "D类门店数量最大值不能超过999999999")
    @Min(value = 0, message = "D类门店数量不能小于0")
    @NotNull(message = "D类门店数量不能为空")
    private Integer storeDCount;

    @ApiModelProperty(value = "费用折扣, 保留两位小数")
    @Between(min = 0, max = 1, includeMin = false, message = "折扣必须在0和1之间(不包括0)")
    private Double discount;

    @ApiModelProperty(value = "合同补充条款")
    @Size(max = 2000)
    private String supplementaryTerms;

    @ApiModelProperty(value = "模板ID")
    private String contractTemplateId;

    @ApiModelProperty(value = "合同广告投放配置列表", required = true)
    @NotNull(message = "合同广告投放配置不能为空")
    private List<ContractAdvertisementPositionConfigViewModel> contractAdvertisementPositionConfigViewModels = new ArrayList<>();

    public Integer getStoreACount() {
        if (storeACount == null)
            return 0;
        return storeACount;
    }

    public Integer getStoreBCount() {
        if (storeBCount == null)
            return 0;
        return storeBCount;
    }

    public Integer getStoreCCount() {
        if (storeCCount == null)
            return 0;
        return storeCCount;
    }

    public Double getProductCost() {
        return productCost == null ? 0 : productCost;
    }

    public Integer getStoreDCount() {
        if (storeDCount == null)
            return 0;
        return storeDCount;
    }

}
