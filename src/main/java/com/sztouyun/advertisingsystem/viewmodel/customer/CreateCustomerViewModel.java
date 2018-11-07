package com.sztouyun.advertisingsystem.viewmodel.customer;

import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
@Data
public class CreateCustomerViewModel {

    @ApiModelProperty(value = "客户名称", required = true)
    @Size(max = 128,message ="客户名称太长" )
    @NotBlank(message = "客户名称不能为空")
    private String name;

    @ApiModelProperty(value = "联系人", required = true)
    @Size(max = 128,message ="联系人太长" )
    @NotBlank(message = "联系人不能为空")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @Pattern(regexp = Constant.REGEX_PHONE, message = "联系电话格式不正确")
    @Size(max = 20,message ="联系电话太长" )
    @NotBlank(message = "联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "从属行业")
    @NotBlank(message = "从属行业不能为空")
    @Size(max = 128,message ="从属行业太长" )
    private String industry;

    @ApiModelProperty(value = "从属子行业")
    @Size(max = 128,message ="从属子行业太长" )
    private String subIndustry;

    @ApiModelProperty(value = "从属行业标签Id")
    @NotBlank(message = "行业标签不能为空")
    private String industryId;

    @ApiModelProperty(value = "从属子行业标签Id")
    private String subIndustryId;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "邮箱格式不正确")
    @Size(max = 128,message ="邮箱太长" )
    private String mailAdress;

    @ApiModelProperty(value = "省份id")
    @Size(max = 128,message ="省份id太长" )
    @NotBlank(message = "省份不能为空")
    private String provinceId;

    @ApiModelProperty(value = "城市id")
    @NotBlank(message = "城市不能为空")
    @Size(max = 128,message ="城市id太长" )
    private String cityId;

    @ApiModelProperty(value = "区域id")
    @NotBlank(message = "地区不能为空")
    @Size(max = 128,message ="区域id太长" )
    private String regionId;

    @ApiModelProperty(value = "具体地址")
    @Size(max = 128,message ="具体地址太长" )
    private String adressDetail;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000,message ="备注太长" )
    private String remark;

    @ApiModelProperty(value = "头像")
    @Size(max = 128,message ="头像地址太长" )
    private String headPortrait;

    @ApiModelProperty(value = "甲方名称")
    @Size(max = 128,message ="甲方名称太长" )
    private String firstPartyName;

    @ApiModelProperty(value = "甲方责任联系人")
    @Size(max = 128,message ="甲方责任联系人太长" )
    private String firstPartyResponsibilityPerson;

    @ApiModelProperty(value = "甲方合同联系电话")
    @Size(max = 128,message ="甲方合同联系电话太长" )
    @Pattern(regexp = Constant.REGEX_PHONE, message = "甲方合同联系电话格式错误")
    private String firstPartyPhone;

    @ApiModelProperty(value = "甲方合同邮箱")
    @Size(max = 128,message ="甲方合同邮箱太长" )
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "甲方合同邮箱格式错误")
    private String firstPartyEmail;

    @ApiModelProperty(value = "甲方合同指定送达地址")
    @Size(max = 2000,message ="甲方合同指定送达地址太长" )
    private String firstPartyContractReceiveAddress;

}
