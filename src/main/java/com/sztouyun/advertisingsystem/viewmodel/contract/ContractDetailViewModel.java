package com.sztouyun.advertisingsystem.viewmodel.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.common.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by szty on 2017/8/2.
 */
@ApiModel
@Data
public class ContractDetailViewModel extends BaseContractViewModel{

    @ApiModelProperty(value = "合同ID")
    private String id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "合同状态名称")
    private String contractStatusName;

    @ApiModelProperty(value = "合同状态")
    private Integer contractStatus;

    @ApiModelProperty(value = "是否审核中")
    private boolean auditing;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "签约人")
    private String signer;

    @ApiModelProperty(value = "维护人")
    private String owner;

    @ApiModelProperty(value = "广告总额")
    private double totalCost;

    @ApiModelProperty(value = "广告位总数")
    private int totalStore;

    @ApiModelProperty(value = "客户头像")
    private String headPortrait;

    @ApiModelProperty(value = "折扣金额")
    private double discountMoney;

    @ApiModelProperty(value = "合作周期开始时间", required = true)
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "合作周期截止时间", required = true)
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "广告配置尺寸名称")
    private String sizeName;

    @ApiModelProperty(value = "广告配置时长")
    private Integer duration;

    @ApiModelProperty(value = "广告配置时长单位名称")
    private String durationUnitName;

    @ApiModelProperty(value = "广告配置展示次数")
    private Integer  displayTimes;

    @ApiModelProperty(value = "广告配置展示次数单位名称")
    private String timeUnitName;

    @ApiModelProperty(value = "合同模板更新时间")
    @JsonFormat(pattern = Constant.DATA_YMD_CN, timezone = "GMT+8")
    private Date contractTemplateUpdatedTime;

    @ApiModelProperty(value = "合同模板类型: 1: 收费模板 2:免费模板 3:通用模板")
    private Integer contractTemplateType;

    @ApiModelProperty(value = "合同广告投放配置列表")
    private List<ContractAdvertisementDetailPositionViewModel> contractAdvertisementDetailPositionViewModelList = new ArrayList<>();

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
