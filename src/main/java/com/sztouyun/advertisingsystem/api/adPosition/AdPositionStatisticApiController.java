package com.sztouyun.advertisingsystem.api.adPosition;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.adPosition.AdPosition;
import com.sztouyun.advertisingsystem.model.adPosition.QAdPosition;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.service.adPosition.AdPositionService;
import com.sztouyun.advertisingsystem.service.statistic.AdvertisementPositionStatisticService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.adPosition.AdPositionListViewModel;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.index.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "广告位统计接口")
@RestController
@RequestMapping("/api/adPosition/statistic")
public class AdPositionStatisticApiController extends BaseApiController {
    @Autowired
    private AdPositionService adPositionService;
    @Autowired
    private AdvertisementPositionStatisticService advertisementPositionStatisticService;
    @Autowired
    private AreaService areaService;

    @ApiOperation(value = "广告位统计列表",notes = "创建人：文丰")
    @RequestMapping(value = "/allAdsPositionStatistic",method = RequestMethod.POST)
    public InvokeResult<PageList<AdPositionStatisticItem>> allAdsPositionStatistic(@Validated  @RequestBody BasePageInfo viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<AdPositionStatisticDto> pages=advertisementPositionStatisticService.getAdPositionInfoStatisticPageInfo(viewModel);
        Long total = advertisementPositionStatisticService.getTotalAdPosition();
        PageList<AdPositionStatisticItem> page=ApiBeanUtils.convertToPageList(pages,adPositionStatisticDto->{
            AdPositionStatisticItem adPositionStatisticItem=ApiBeanUtils.copyProperties(adPositionStatisticDto,AdPositionStatisticItem.class);
            adPositionStatisticItem.setAreaName(getAreaName(adPositionStatisticDto.getAreaId()));
            adPositionStatisticItem.setAvailableAmount(adPositionStatisticDto.getTotalAmount(), adPositionStatisticDto.getUsedAmount());
            adPositionStatisticItem.setAmountRatio(NumberFormatUtil.format(adPositionStatisticItem.getTotalAmount(), total, Constant.RATIO_PATTERN));
            adPositionStatisticItem.setUtilizationRatio(NumberFormatUtil.format(adPositionStatisticItem.getUsedAmount(), adPositionStatisticItem.getTotalAmount(), Constant.RATIO_PATTERN));
            return adPositionStatisticItem;
        });
        return InvokeResult.SuccessResult(page);

    }

    @ApiOperation(value = "广告位统计列表(展开项)",notes = "创建人：文丰")
    @GetMapping(value = "/adsPositionStatisticByProvince/{areaId}")
    public InvokeResult<List<AdPositionStatisticItem>> adsPositionStatisticByProvince(@PathVariable("areaId") String areaId) {
        if (StringUtils.isEmpty(areaId))
            throw new BusinessException("区域ID不能为空");
        if(areaService.isMunicipality(areaId))
            return InvokeResult.SuccessResult();
        Long total = advertisementPositionStatisticService.getTotalAdPosition();
        List<AdPositionStatisticDto> list = advertisementPositionStatisticService.getAdPositionInfoStatisticInfoByProvince(areaId);
        List<AdPositionStatisticItem> result=getItemList(total, list);
        return InvokeResult.SuccessResult(result);

    }

    private List<AdPositionStatisticItem> getItemList( Long total, List<AdPositionStatisticDto> list) {
        List<AdPositionStatisticItem> resultList = new ArrayList<>();
        list.stream().forEach(adPositionStatisticDto -> {
            AdPositionStatisticItem adPositionStatisticItem = ApiBeanUtils.copyProperties(adPositionStatisticDto, AdPositionStatisticItem.class);
            adPositionStatisticItem.setAreaName(getAreaName(adPositionStatisticDto.getAreaId()));
            adPositionStatisticItem.setAvailableAmount(adPositionStatisticDto.getTotalAmount(), adPositionStatisticDto.getUsedAmount());
            adPositionStatisticItem.setAmountRatio(NumberFormatUtil.format(adPositionStatisticItem.getTotalAmount(), total, Constant.RATIO_PATTERN));
            adPositionStatisticItem.setUtilizationRatio(NumberFormatUtil.format(adPositionStatisticItem.getUsedAmount(), adPositionStatisticItem.getTotalAmount(), Constant.RATIO_PATTERN));
            resultList.add(adPositionStatisticItem);
        });
       return resultList;
    }

}
