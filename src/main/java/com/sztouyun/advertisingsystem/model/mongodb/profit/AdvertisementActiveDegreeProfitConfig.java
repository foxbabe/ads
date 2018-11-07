package com.sztouyun.advertisingsystem.model.mongodb.profit;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.IntegerSpan;
import com.sztouyun.advertisingsystem.common.datatable.DataCell;
import com.sztouyun.advertisingsystem.common.datatable.DataRow;
import com.sztouyun.advertisingsystem.common.datatable.DataTable;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.service.system.AreaCacheService;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 广告活跃度计费分成配置
 */
public class AdvertisementActiveDegreeProfitConfig extends DataTable<AdvertisementActiveDegreeProfitConfigCellType> {
    private List<String> cityIds = new ArrayList<>();
    private List<IntegerSpan> storeOrderSpans = new ArrayList<>();
    private AreaCacheService areaCacheService = SpringUtil.getBean(AreaCacheService.class);

    @Override
    public void validate() {
        super.validate();
        cityIds =null;
        storeOrderSpans =null;
        areaCacheService =null;
    }

    @Override
    protected void onValidateCell(int rowIndex, int cellIndex, DataCell cell, AdvertisementActiveDegreeProfitConfigCellType cellType) {
        switch (cellType) {
            case StoreOrderSpan:
                IntegerSpan storeOrderSpan = cell.getData();
                if (Objects.isNull(storeOrderSpan) || Objects.isNull(storeOrderSpan.getStartValue()))
                    throw new BusinessException("订单数量不能为空");
                if (!storeOrderSpan.isPositiveInteger())
                    throw new BusinessException("日订单输入框只能输入正整数");
                if (!storeOrderSpan.isValidSpan())
                    throw new BusinessException("日订单配置不正确，左区间必须小于等于右区间");
                for (IntegerSpan orderSpan : storeOrderSpans) {
                    if (orderSpan.hasIntersection(storeOrderSpan))
                        throw new BusinessException("日订单配置不正确，区间不能重合");
                }
                storeOrderSpans.add(storeOrderSpan);
                break;

            case StoreMinBootTime:
                Integer storeMinBootTime = cell.getData();
                if (Objects.isNull(storeMinBootTime))
                    throw new BusinessException("开机时长不能为空");
                if (storeMinBootTime < 1 || storeMinBootTime > 24)
                    throw new BusinessException("开机时长只能选择 1 - 24");
                break;

            case StoreOrderProfitAmount:
            case StoreBootTimeProfitAmount:
                Double amount = cell.getData();
                if (Objects.isNull(amount))
                    throw new BusinessException("金额不能为空");
                if (amount < 0)
                    throw new BusinessException("金额输入框只能输入大于等于0的数字");
                break;

            case AreaList:
                HashMap<String,String> areaMap = cell.getData();
                val cityIds = areaMap.keySet();
                if (!(cityIds.size() == 1 && cityIds.contains(Constant.OTHER_CITY_ID))) {
                    cityIds.forEach(cityId -> {
                        if (Objects.isNull(areaCacheService.getAreaFromCache(cityId)))
                            throw new BusinessException("编号为" + cityId + "的城市不存在");
                    });
                }
                break;

            default:
        }
    }

    @Override
    protected void onValidateRow(int rowIndex, DataRow row) {
        val optional = row.getCells().stream().filter(e -> Objects.equals(AdvertisementActiveDegreeProfitConfigCellType.AreaList.getValue(), e.getType())).findAny();
        if (optional.isPresent()) {
            HashMap<String,String> areaMap = optional.get().getData();
            areaMap.entrySet().forEach(city -> {
                if (this.cityIds.contains(city.getKey())) throw new BusinessException(city.getValue() + "不能重复配置");
            });
            this.cityIds.addAll(cityIds);
        }
    }
}
