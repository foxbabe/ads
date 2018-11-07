package com.sztouyun.advertisingsystem.api.statistic;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.mapper.AdvertisementPositionMapper;
import com.sztouyun.advertisingsystem.service.statistic.AdvertisementPositionStatisticService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.index.DistributionStatisticDto;
import com.sztouyun.advertisingsystem.viewmodel.statistic.AdvertisementPositionAreaMapViewModel;
import com.sztouyun.advertisingsystem.viewmodel.statistic.AdvertisementPositionAreaStatisticResult;
import com.sztouyun.advertisingsystem.viewmodel.statistic.AdvertisementPositionAreaStatisticViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "广告位统计数据接口")
@RestController
@RequestMapping("/api/advertisementposition")
public class AdvertisementPositionStatisticApiController extends BaseApiController {

    @Autowired
    private AdvertisementPositionStatisticService advertisementPositionStatisticService;

    @Autowired
    private AdvertisementPositionMapper advertisementPositionMapper;

    @ApiOperation(value = "广告位地区分布统计", notes = "创建人: 王伟权")
    @GetMapping(value = "areaMapDistributionStatistic")
    public InvokeResult<AdvertisementPositionAreaMapViewModel> areaMapDistributionStatistic() {
        return InvokeResult.SuccessResult(getAdvertisementPositionViewMode(advertisementPositionStatisticService.getAdvertisementPositionDistribute(), advertisementPositionStatisticService.getTotalAdPosition()));
    }

    private AdvertisementPositionAreaMapViewModel getAdvertisementPositionViewMode(List<AdvertisementPositionAreaStatisticResult> queryResultList, Long totalAdvertisementPosition) {
        AdvertisementPositionAreaMapViewModel advertisementPositionAreaMapViewModel = new AdvertisementPositionAreaMapViewModel();
        Long totalStoreAmount=getAdvertisementPositionAreaStoreInfo(advertisementPositionAreaMapViewModel);
        if(CollectionUtils.isEmpty(queryResultList))
            return advertisementPositionAreaMapViewModel;

        advertisementPositionAreaMapViewModel.setMaxAmount(queryResultList.get(0).getAdvertisementPositionCount());
        advertisementPositionAreaMapViewModel.setMinAmount(queryResultList.get(queryResultList.size() - 1).getAdvertisementPositionCount());
        advertisementPositionAreaMapViewModel.setAdvertisementPositionCount(totalAdvertisementPosition.intValue());
        List<AdvertisementPositionAreaStatisticViewModel> resultList = new ArrayList<>();

        for (AdvertisementPositionAreaStatisticResult queryResult : queryResultList) {
            resultList.add(convertQueryResultToViewModel(queryResult, totalAdvertisementPosition,totalStoreAmount));
        }
        advertisementPositionAreaMapViewModel.setAvailableAdvertisementPositionCount(advertisementPositionMapper.getAllAvailableAdPosition().intValue());
        advertisementPositionAreaMapViewModel.setUseAdvertisementPositionCount(advertisementPositionAreaMapViewModel.getAdvertisementPositionCount() - advertisementPositionAreaMapViewModel.getAvailableAdvertisementPositionCount());
        advertisementPositionAreaMapViewModel.setList(resultList);
        return advertisementPositionAreaMapViewModel;
    }

    private AdvertisementPositionAreaStatisticViewModel convertQueryResultToViewModel(AdvertisementPositionAreaStatisticResult queryResult, Long totalAdvertisementPosition,Long totalStore) {
        AdvertisementPositionAreaStatisticViewModel advertisementPositionAreaStatisticViewModel = ApiBeanUtils.copyProperties(queryResult, AdvertisementPositionAreaStatisticViewModel.class);
        advertisementPositionAreaStatisticViewModel.setUseAdvertisementPositionRatio(NumberFormatUtil.format(queryResult.getUsedAdvertisementPositionCount().longValue(),queryResult.getAdvertisementPositionCount().longValue(), Constant.RATIO_PATTERN ));
        advertisementPositionAreaStatisticViewModel.setAdvertisementPositionRatio(NumberFormatUtil.format(queryResult.getAdvertisementPositionCount().longValue(), totalAdvertisementPosition.longValue(), Constant.RATIO_PATTERN));
        advertisementPositionAreaStatisticViewModel.setStoreAmount(queryResult.getStoreAmount().intValue());
        advertisementPositionAreaStatisticViewModel.setStoreRatio(NumberFormatUtil.format(queryResult.getStoreAmount(),totalStore,Constant.RATIO_PATTERN));
        return advertisementPositionAreaStatisticViewModel;
    }

    private  Long getAdvertisementPositionAreaStoreInfo(AdvertisementPositionAreaMapViewModel viewModel){
        List<DistributionStatisticDto> list=advertisementPositionMapper.getStoreCount();
        Long totalStoreAmount=0L;
        for (DistributionStatisticDto distributionStatisticDto:list
             ) {
            Integer type=(Integer) distributionStatisticDto.getKeyValue();
            if(type==null) {
                distributionStatisticDto.setKeyValue(0);
                totalStoreAmount=distributionStatisticDto.getValue();
            }
        }
        viewModel.setStoreAmountList(list);
        return totalStoreAmount;
    }
}
