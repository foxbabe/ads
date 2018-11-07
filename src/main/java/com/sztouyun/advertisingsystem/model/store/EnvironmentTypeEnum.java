package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.EnumMessage;

import java.util.Arrays;
import java.util.Collection;

public enum EnvironmentTypeEnum implements EnumMessage<Integer> {

    CommercialDistrict(1,"商业区", Arrays.asList("夜总会","KTV","迪厅","酒吧","影剧院","餐饮服务","商场","家电电子卖场","特色商业步行街","体育用品店","服装鞋帽皮具店","专卖店","银行")),
    ResidentialDistrict(2,"居民区", Arrays.asList("商务住宅","人才市场","摄影冲印店","洗浴推拿场所","搬家公司","婴儿服务场所")),
    SchoolDistrict(3,"学校", Arrays.asList("幼儿园","小学","中学","高中","大学","成人教育","职业技术学校")),
    Others(999,"其他", null)
    ;

    private Integer value;
    private String displayName;
    private Collection<String> poiTypes;

    EnvironmentTypeEnum(Integer value, String displayName,  Collection<String> poiTypes) {
        this.value = value;
        this.displayName = displayName;
        this.poiTypes = poiTypes;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public Collection<String> getPoiTypes() {
        return poiTypes;
    }
}
