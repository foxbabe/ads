package com.sztouyun.advertisingsystem.viewmodel.store.storePortrait;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
public class StorePortraitListRequest  extends BasePageInfo {

    @ApiModelProperty(value = "省份ID")
    @Size(max = 128,message ="省份id太长" )
    private String provinceId;

    @ApiModelProperty(value = "城市ID")
    @Size(max = 128,message ="城市id太长" )
    private String cityId;

    @ApiModelProperty(value = "地区ID")
    @Size(max = 128,message ="地区id太长" )
    private String regionId;

    @ApiModelProperty(value = "门店类型(多选时逗号隔开)")
    private String storeFrontType;

    @ApiModelProperty(value = "日销售额(多选时逗号隔开)")
    private String dailySales;

    @ApiModelProperty(value = "装修情况(多选时逗号隔开)")
    private String decoration;

    @ApiModelProperty(value = "周边环境(多选时逗号隔开)")
    private String surroundingsDistrict;

    @ApiModelProperty(value = "营业面积(多选时逗号隔开)")
    private String commercialArea;

    @ApiModelProperty(value = "订货比例(多选时逗号隔开)")
    private String orderRatio;


    public List<Integer> getAllStoreFrontTypes() {
        if(!StringUtils.isEmpty(storeFrontType)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(storeFrontType, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }

    public List<Integer> getAllDailySales() {
        if(!StringUtils.isEmpty(dailySales)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(dailySales, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }

    public List<Integer> getAllDecorations() {
        if(!StringUtils.isEmpty(decoration)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(decoration, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }

    public List<Integer> getAllCommercialAreas() {
        if(!StringUtils.isEmpty(commercialArea)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(commercialArea, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }

    public List<Integer> getAllSurroundingsDistricts() {
        if(!StringUtils.isEmpty(surroundingsDistrict)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(surroundingsDistrict, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }

    public List<Integer> getAllOrderRatios() {
        if(!StringUtils.isEmpty(orderRatio)) {
            return com.sztouyun.advertisingsystem.utils.StringUtils.stringToInts(orderRatio, Constant.SEPARATOR);

        }
        return new ArrayList<>();
    }
}
