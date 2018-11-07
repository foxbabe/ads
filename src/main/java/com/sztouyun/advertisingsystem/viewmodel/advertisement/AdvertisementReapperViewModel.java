package com.sztouyun.advertisingsystem.viewmodel.advertisement;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.DetailAdvertisementMaterialViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by wenfeng on 2017/8/4.
 */
@ApiModel
public class AdvertisementReapperViewModel extends BaseAdvertisementViewModel{
    @ApiModelProperty(value = "广告ID", required = true)
    @NotBlank(message = "广告ID不能为空")
    private String id;

    @ApiModelProperty(value = "合同ID", required = true)
    private String contractId;

    @ApiModelProperty(value = "素材列表", required = false)
    private  List<TerminalAdvertisementConfigInfo> materialItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public List<TerminalAdvertisementConfigInfo> getMaterialItems() {
        return materialItems;
    }

    public void setMaterialItems(List<TerminalAdvertisementConfigInfo> materialItems) {
        this.materialItems = materialItems;
    }
}
