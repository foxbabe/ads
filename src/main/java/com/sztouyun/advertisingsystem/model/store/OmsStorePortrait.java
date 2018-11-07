package com.sztouyun.advertisingsystem.model.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by wenfeng on 2018/3/21.
 */
@Data
public class OmsStorePortrait {
    /**
     *
     * eg:"StoreId": 29,
     */
    @JsonProperty("StoreId")
    private String storeId;
    /**
     * 门店编号 "StoreNo": "134368291255408"
     */
    @JsonProperty("StoreNo")
    private String storeNo;

    /**
     * 门店数类型 "StoreType": "烟酒专卖店"
     */
    @JsonProperty("StoreType")
    private String storeType;

    /**
     * 装修风格  "Decoration": "精超",
     */
    @JsonProperty("Decoration")
    private String decoration;

    /**
     * 营业面积 "UsageArea": "0-20",
     */
    @JsonProperty("UsageArea")
    private String usageArea;

    /**
     * 银行卡号
     */
    @JsonProperty("BankCardNo")
    private String bankCardNo;

    /**
     * 是否有冷藏柜  是，否
     */
    @JsonProperty("IsFreezer")
    private String IsFreezer;

    /**
     * 冷藏柜品类 "RefrigerationGoodsCategory": "水果;素菜",
     */
    @JsonProperty("RefrigerationGoodsCategory")
    private String refrigerationGoodsCategory;

    /**
     * 是否有冰冻柜  是，否
     */
    @JsonProperty("IsCabinetFreezer")
    private String isCabinetFreezer;

    /**
     * 是否有冰冻柜 "FrozenGoodsCategory": "冷冻肉",
     */
    @JsonProperty("FrozenGoodsCategory")
    private String frozenGoodsCategory;

    /**
     * 货架个数 "ShelfNum": "11-20"
     */
    @JsonProperty("ShelfNum")
    private String shelfNum;

    /**
     * 加盟店自行订货比例 "TheBookingRatioOfItsFranchisees": "20%以下"
     */
    @JsonProperty("TheBookingRatioOfItsFranchisees")
    private String theBookingRatioOfItsFranchisees;

    /**
     * 日销售额  "DailySales": "0-1000",
     */
    @JsonProperty("DailySales")
    private String DailySales;

    /**
     * 盈利状况
     */
    @JsonProperty("Profit")
    private String profit;

    /**
     * 地推人员的总结
     */
    @JsonProperty("PushSummary")
    private String pushSummary;

    /**
     * 是否居民区
     */
    @JsonProperty("Residential")
    private String residential;

    /**
     * 是否商业区
     */
    @JsonProperty("CommercialArea")
    private String commercialArea;

    /**
     * 是否有学校
     */
    @JsonProperty("School")
    private String school;

    /**
     * 周围居民区数量
     */
    @JsonProperty("ResidentialNum")
    private Integer residentialNum;

    @JsonProperty("Attachment")
    List<OmsStoreAttachment> omsStoreAttachments;
}
