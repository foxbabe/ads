package com.sztouyun.advertisingsystem.viewmodel.coorperationPartner;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.annotation.validation.EnumValue;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.model.partner.CooperationPatternEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
@Data
public class CooperationPartnerViewModel {

    @ApiModelProperty(value = "合作方名称", required = true)
    @Size(max = 128,message ="合作方名称太长" )
    @NotBlank(message = "合作方名称不能为空")
    private String name;

    @ApiModelProperty(value = "联系人", required = true)
    @Size(max = 128,message ="联系人太长" )
    @NotBlank(message = "联系人不能为空")
    private String contacts;

    @ApiModelProperty(value = "联系电话", required = true)
    @Pattern(regexp = Constant.REGEX_PHONE, message = "联系电话格式错误！")
    @Size(max = 20,message ="联系电话太长" )
    @NotBlank(message = "联系电话不能为空")
    private String contactNumber;


    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = Constant.REGEX_EMAIL, message = "邮箱格式错误！")
    @Size(max = 128,message ="邮箱太长" )
    private String mailAddress;

    @ApiModelProperty(value = "省份id")
    @Size(max = 128,message ="省份id太长" )
    private String provinceId;

    @ApiModelProperty(value = "城市id")
    @Size(max = 128,message ="城市id太长" )
    private String cityId;

    @ApiModelProperty(value = "区域id")
    @Size(max = 128,message ="区域id太长" )
    private String regionId;

    @ApiModelProperty(value = "具体地址")
    @Size(max = 128,message ="具体地址太长" )
    private String addressDetail;

    @ApiModelProperty(value = "备注")
    @Size(max = 2000,message ="备注太长" )
    private String remark;

    @ApiModelProperty(value = "头像")
    @Size(max = 128,message ="头像地址太长" )
    private String headPortrait;

    @ApiModelProperty(value = "合作模式", required = true)
    @EnumValue(enumClass = CooperationPatternEnum.class,message = "合作模式不匹配")
    private Integer cooperationPattern;

    @ApiModelProperty(value = "模式时长")
    private Integer duration;

    @ApiModelProperty(value = "合作方请求URL")
    private String apiUrl;

    @ApiModelProperty(value = "模式时长单位", required = true)
    @EnumValue(enumClass = UnitEnum.class,nullable = true,message = "合作模式时长单位错误")
    private Integer durationUnit;

}
