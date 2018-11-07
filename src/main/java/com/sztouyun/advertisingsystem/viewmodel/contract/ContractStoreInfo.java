package com.sztouyun.advertisingsystem.viewmodel.contract;


import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel
public class ContractStoreInfo extends BasePageInfo {
    /**
     * 门店id
     */
    private String id;

    /**
     *  合同ID
     */
    @ApiModelProperty(value = "合同Id", required = true)
    @NotBlank(message = "合同Id不能为空")
    private String contractId;
    /**
     * 门店名称
     */
    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;
    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备ID", required = true)
    private String deviceId;
    /**
     * 门店地址
     */
    private String storeAddress;
    /**
     * 省份id
     */
    private String provinceId;
    /**
     * 城市id
     */
    private String cityId;
    /**
     * 区域id
     */
    private String regionId;
    /**
     *  已使用广告位数量
     */
    private Integer usedCount;
    /**
     *  可用广告位数量
     */
    private Integer availableCount;
    /**
     *  是否已选择
     */
    private Integer isChoose;

    /**
     * 是否可用
     * @return
     */
    private Boolean available;

    /**
     * 门店ID
     * @return
     */
    private String shopId;

    @ApiModelProperty(value = "可否查看")
    private Boolean canView;

    /**
     * 门店是否达标
     */
    private Boolean isQualified;

}
