package com.sztouyun.advertisingsystem.api.advertisement;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.common.DisplayTimeUnitEnum;
import com.sztouyun.advertisingsystem.model.common.PackagePeroidEnum;
import com.sztouyun.advertisingsystem.model.contract.StoreTypeEnum;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementActiveDegreeProfitConfigCellType;
import com.sztouyun.advertisingsystem.model.mongodb.profit.AdvertisementProfitConfig;
import com.sztouyun.advertisingsystem.model.system.*;
import com.sztouyun.advertisingsystem.service.advertisement.*;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.*;
import com.sztouyun.advertisingsystem.viewmodel.common.BasePageInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.EnumItemInfo;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;


/**
 * Created by wenfeng on 2017/8/4.
 */

@Api("广告配置管理接口")
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementConfigController extends BaseApiController {
    @Autowired
    private AdvertisementPriceConfigService advertisementPriceConfigService;
    @Autowired
    private AdvertisementPackageConfigService advertisementPackageConfigService;
    @Autowired
    private AdvertisementSizeConfigService advertisementSizeConfigService;
    @Autowired
    private AdvertisementDurationConfigService advertisementDurationConfigService;
    @Autowired
    private AdvertisementDisplayTimesConfigService advertisementDisplayTimesConfigService;
    @Autowired
    private AdvertisementService advertisementService;

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新增广告价格配置", notes = "创建人：文丰")
    @PostMapping(value = "/priceConfig/create")
    public InvokeResult createAdvertisementPriceConfig(@Validated @RequestBody BaseAdvertisementPriceConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementPriceConfig advertisementPriceConfig = new AdvertisementPriceConfig();
        BeanUtils.copyProperties(viewModel, advertisementPriceConfig);
        advertisementPriceConfigService.createAdvertisementPriceConfig(advertisementPriceConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除广告价格配置", notes = "创建人：文丰")
    @PostMapping(value = "/priceConfig/delete/{id}")
    public InvokeResult deleteAdvertisementPriceConfig(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        advertisementPriceConfigService.deleteAdvertisementPriceConfig(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改广告价格配置", notes = "创建人：文丰")
    @PostMapping(value = "/priceConfig/update")
    public InvokeResult updateAdvertisementPriceConfig(@Validated @RequestBody UpdateAdvertisementPriceConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementPriceConfig advertisementPriceConfig = new AdvertisementPriceConfig();
        BeanUtils.copyProperties(viewModel, advertisementPriceConfig);
        advertisementPriceConfigService.updateAdvertisementPriceConfig(advertisementPriceConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询广告价格配置详情", notes = "创建人：文丰")
    @RequestMapping(value = "/priceConfig/{id}", method = RequestMethod.GET)
    public InvokeResult<DetailAdvertisementPriceConfigViewModel> getAdvertisementPriceConfig(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        AdvertisementPriceConfig advertisementPriceConfig = advertisementPriceConfigService.getAdvertisementPriceConfig(id);
        DetailAdvertisementPriceConfigViewModel detailViewModel = new DetailAdvertisementPriceConfigViewModel();
        BeanUtils.copyProperties(advertisementPriceConfig, detailViewModel);
        detailViewModel.setStoreTypeName(EnumUtils.getDisplayName(advertisementPriceConfig.getStoreType(),StoreTypeEnum.class));
        detailViewModel.setCreatedTime(advertisementPriceConfig.getStartTime());
        detailViewModel.setCreator(getUserNickname(advertisementPriceConfig.getCreatorId()));
        detailViewModel.setPrice(NumberFormatUtil.format(advertisementPriceConfig.getPrice()));
        if (advertisementPriceConfig.getActived()) {
            detailViewModel.setUpdatedTime(advertisementPriceConfig.getCreatedTime());
        }
        return InvokeResult.SuccessResult(detailViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询广告价格配置列表", notes = "创建人：文丰")
    @PostMapping(value = "/priceConfig")
    public InvokeResult<PageList<DetailAdvertisementPriceConfigViewModel>> getAdvertisementPriceConfigList(@Validated @RequestBody AdvertisementPricePricePageInfoViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(QAdvertisementPriceConfig.advertisementPriceConfig.createdTime.desc()));
        Page<AdvertisementPriceConfig> pages = advertisementPriceConfigService.queryAdvertisementPriceConfigList(true, viewModel.getStoreType(), pageable);
        PageList<DetailAdvertisementPriceConfigViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementPriceConfig ->
        {
            DetailAdvertisementPriceConfigViewModel detailAdvertisementPriceConfigView=new DetailAdvertisementPriceConfigViewModel();
            BeanUtils.copyProperties(advertisementPriceConfig,detailAdvertisementPriceConfigView);
            detailAdvertisementPriceConfigView.setStoreTypeName(EnumUtils.getDisplayName(advertisementPriceConfig.getStoreType(),StoreTypeEnum.class));
            detailAdvertisementPriceConfigView.setCreator(getUserNickname(advertisementPriceConfig.getCreatorId()));
            detailAdvertisementPriceConfigView.setCreatedTime(advertisementPriceConfig.getStartTime());
            detailAdvertisementPriceConfigView.setPrice(NumberFormatUtil.format(advertisementPriceConfig.getPrice()));
            if(advertisementPriceConfig.getActived()){
                detailAdvertisementPriceConfigView.setUpdatedTime(advertisementPriceConfig.getCreatedTime());
            }
            return detailAdvertisementPriceConfigView;
        });
        return InvokeResult.SuccessResult(resultList);
    }


    @ApiOperation(value = "判断门店价格配置是否完整", notes = "创建人: 王伟权  返回true表示配置完整, 否则返回false")
    @GetMapping(value = "/hasCompletedPriceConfig")
    public InvokeResult<Boolean> hasCompletedPriceConfig() {
        return InvokeResult.SuccessResult(advertisementPriceConfigService.hasCompletedPriceConfig());
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新增广告尺寸配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/sizeConfig/create")
    public InvokeResult createSizeConfig(@Validated @RequestBody BaseAdvertisementSizeConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementSizeConfig advertisementSizeConfig = ApiBeanUtils.copyProperties(viewModel, AdvertisementSizeConfig.class);
        advertisementSizeConfigService.createSizeConfig(advertisementSizeConfig);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改广告尺寸配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/sizeConfig/update")
    public InvokeResult updateSizeConfig(@Validated @RequestBody UpdateAdvertisementSizeConfigViewModel viewModel, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);

        AdvertisementSizeConfig oldSizeConfig = advertisementSizeConfigService.getSizeConfig(viewModel.getId());
        AdvertisementSizeConfig newSizeConfig = ApiBeanUtils.copyProperties(oldSizeConfig, AdvertisementSizeConfig.class);
        BeanUtils.copyProperties(viewModel, newSizeConfig);
        advertisementSizeConfigService.updateSizeConfig(newSizeConfig);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "获取广告尺寸配置详情", notes = "创建人: 王伟权")
    @GetMapping(value = "/sizeConfig/{id}")
    public InvokeResult<DetailAdvertisementSizeConfigViewModel> getSizeConfig(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("广告尺寸id不能为空");
        AdvertisementSizeConfig sizeConfig = advertisementSizeConfigService.getSizeConfig(id);
        DetailAdvertisementSizeConfigViewModel detailAdvertisementSizeConfigViewModel = ApiBeanUtils.copyProperties(sizeConfig, DetailAdvertisementSizeConfigViewModel.class);
        detailAdvertisementSizeConfigViewModel.setCreator(getUserNickname(sizeConfig.getCreatorId()));
        return InvokeResult.SuccessResult(detailAdvertisementSizeConfigViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除广告尺寸配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/sizeConfig/{id}/delete")
    public InvokeResult deleteSizeConfig(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("广告尺寸id不能为空");
        advertisementSizeConfigService.deleteSizeConfig(id);
        return InvokeResult.SuccessResult();
    }



    @ApiOperation(value = "分页获取广告尺寸配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/sizeConfig")
    public InvokeResult<PageList<DetailAdvertisementSizeConfigViewModel>> getAllSizeConfigList(@Validated @RequestBody BasePageInfo viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(QAdvertisementSizeConfig.advertisementSizeConfig.createdTime.desc()));
        Page<AdvertisementSizeConfig> pages = advertisementSizeConfigService.getAllSizeConfitList(pageable);
        PageList<DetailAdvertisementSizeConfigViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementSizeConfig -> {

            DetailAdvertisementSizeConfigViewModel detailAdvertisementSizeConfigViewModel = ApiBeanUtils.copyProperties(advertisementSizeConfig, DetailAdvertisementSizeConfigViewModel.class);
            detailAdvertisementSizeConfigViewModel.setCreator(getUserNickname(advertisementSizeConfig.getCreatorId()));
            return detailAdvertisementSizeConfigViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新增广告时长配置", notes = "创建人：王伟权")
    @PostMapping(value = "/durationConfig/create")
    public InvokeResult createDurationConfig(@Validated @RequestBody BaseAdvertisementDurationConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        DurationUnitEnum durationUnitEnum = EnumUtils.toEnum(viewModel.getDurationUnit(), DurationUnitEnum.class);
        if(durationUnitEnum == null)
            return InvokeResult.Fail("请选择正确的单位类型");

        AdvertisementDurationConfig advertisementDurationConfig = ApiBeanUtils.copyProperties(viewModel, AdvertisementDurationConfig.class);
        advertisementDurationConfigService.createDurationConfig(advertisementDurationConfig);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "更新广告时长配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/durationConfig/update")
    public InvokeResult updateDurationConfig(@Validated @RequestBody UpdateAdvertisementDurationConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        DurationUnitEnum durationUnitEnum = EnumUtils.toEnum(viewModel.getDurationUnit(), DurationUnitEnum.class);
        if(durationUnitEnum == null)
            return InvokeResult.Fail("请选择正确的单位类型");

        AdvertisementDurationConfig oldDurationConfig = advertisementDurationConfigService.getDurationConfig(viewModel.getId());
        AdvertisementDurationConfig newDurationConfig = ApiBeanUtils.copyProperties(oldDurationConfig, AdvertisementDurationConfig.class);
        BeanUtils.copyProperties(viewModel, newDurationConfig);

        advertisementDurationConfigService.updateDurationConfig(newDurationConfig);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "广告时长配置详情", notes = "创建人: 王伟权")
    @GetMapping(value = "/durationConfig/{id}")
    public InvokeResult<DetailAdvertisementDurationConfigViewModel> getDurationConfig(@PathVariable String id) {

        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("广告时长配置不存在！");

        AdvertisementDurationConfig durationConfig = advertisementDurationConfigService.getDurationConfig(id);
        DetailAdvertisementDurationConfigViewModel detailAdvertisementDurationConfigViewModel = ApiBeanUtils.copyProperties(durationConfig, DetailAdvertisementDurationConfigViewModel.class);

        detailAdvertisementDurationConfigViewModel.setCreator(getUserNickname(durationConfig.getCreatorId()));
        detailAdvertisementDurationConfigViewModel.setUnitName(EnumUtils.toEnum(durationConfig.getDurationUnit(), DurationUnitEnum.class).getDisplayName());

        return InvokeResult.SuccessResult(detailAdvertisementDurationConfigViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除广告时长配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/durationConfig/{id}/delete")
    public InvokeResult deleteDurationConfig(@PathVariable String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("广告时长配置不存在！");

        advertisementDurationConfigService.deleteDurationConfig(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "分页获取广告时长配置", notes = "创建人: 王伟权")
    @PostMapping(value = "/durationConfig")
    public InvokeResult<PageList<DetailAdvertisementDurationConfigViewModel>> getAllDurationConfigList(@Validated @RequestBody BasePageInfo viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(QAdvertisementDurationConfig.advertisementDurationConfig.createdTime.desc()));
        Page<AdvertisementDurationConfig> pages = advertisementDurationConfigService.getAllDurationConfigList(pageable);
        PageList<DetailAdvertisementDurationConfigViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementDurationConfig -> {
            DetailAdvertisementDurationConfigViewModel durationConfigViewModel = ApiBeanUtils.copyProperties(advertisementDurationConfig, DetailAdvertisementDurationConfigViewModel.class);
            durationConfigViewModel.setUnitName(EnumUtils.toEnum(advertisementDurationConfig.getDurationUnit(), DurationUnitEnum.class).getDisplayName());
            durationConfigViewModel.setCreator(getUserNickname(advertisementDurationConfig.getCreatorId()));
            return durationConfigViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新增套餐配置", notes = "创建人：文丰")
    @PostMapping(value = "/packageConfig/create")
    public InvokeResult createAdvertisementPackageConfig(@Validated @RequestBody BaseAdvertisementPackageConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementPackageConfig advertisementPackageConfig=new AdvertisementPackageConfig();
        BeanUtils.copyProperties(viewModel,advertisementPackageConfig);
        advertisementPackageConfigService.createAdvertisementPackageConfig(advertisementPackageConfig);
        return InvokeResult.SuccessResult();
    }


    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除套餐配置", notes = "创建人：文丰")
    @PostMapping(value = "/packageConfig/delete/{id}")
    public InvokeResult deleteAdvertisementPackageConfig(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        advertisementPackageConfigService.deleteAdvertisementPackageConfig(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改套餐配置", notes = "创建人：文丰")
    @PostMapping(value = "/packageConfig/update")
    public InvokeResult updateAdvertisementPriceConfig(@Validated @RequestBody UpdateAdvertisementPackageConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementPackageConfig advertisementPackageConfig=new AdvertisementPackageConfig();
        BeanUtils.copyProperties(viewModel,advertisementPackageConfig);
        advertisementPackageConfigService.updateAdvertisementPackageConfig(advertisementPackageConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询套餐配置详情", notes = "创建人：文丰")
    @RequestMapping(value = "/packageConfig/{id}", method = RequestMethod.GET)
    public InvokeResult<DetailAdvertisementPackageConfigViewModel> getAdvertisementPackageConfig(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        AdvertisementPackageConfig advertisementPackageConfig= advertisementPackageConfigService.getAdvertisementPackageConfig(id);
        DetailAdvertisementPackageConfigViewModel detailViewModel=new DetailAdvertisementPackageConfigViewModel();
        BeanUtils.copyProperties(advertisementPackageConfig,detailViewModel);
        detailViewModel.setTotalCost(NumberFormatUtil.format(advertisementPackageConfig.getTotalCost()));
        detailViewModel.setPeriodName(EnumUtils.getDisplayName(advertisementPackageConfig.getPeriod(),PackagePeroidEnum.class));
        detailViewModel.setTotalAmount(advertisementPackageConfig.getTotalAmount());
        detailViewModel.setCreator(getUserNickname(advertisementPackageConfig.getCreatorId()));
        detailViewModel.setUpdater(getUserNickname(advertisementPackageConfig.getUpdaterId()));
        return InvokeResult.SuccessResult(detailViewModel);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "查询套餐配置列表", notes = "创建人：文丰")
    @PostMapping(value = "/packageConfig")
    public InvokeResult<PageList<DetailAdvertisementPackageConfigViewModel>> getAdvertisementPackageConfigList(@Validated @RequestBody AdvertisementPackagePageInfoViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(QAdvertisementPackageConfig.advertisementPackageConfig.createdTime.desc()));
        Page<AdvertisementPackageConfig> pages = advertisementPackageConfigService.queryAdvertisementPackageConfigList(true,viewModel.getPackageName(), pageable);
        PageList<DetailAdvertisementPackageConfigViewModel> resultList= ApiBeanUtils.convertToPageList(pages, advertisementPackageConfig ->
        {
            DetailAdvertisementPackageConfigViewModel detailAdvertisementPackageConfigViewModel=new DetailAdvertisementPackageConfigViewModel();
            BeanUtils.copyProperties(advertisementPackageConfig,detailAdvertisementPackageConfigViewModel);
            detailAdvertisementPackageConfigViewModel.setPeriodName(EnumUtils.getDisplayName(advertisementPackageConfig.getPeriod(),PackagePeroidEnum.class));
            detailAdvertisementPackageConfigViewModel.setCreator(getUserNickname(advertisementPackageConfig.getCreatorId()));
            detailAdvertisementPackageConfigViewModel.setUpdater(getUserNickname(advertisementPackageConfig.getUpdaterId()));
            detailAdvertisementPackageConfigViewModel.setTotalAmount(advertisementPackageConfig.getTotalAmount());
            detailAdvertisementPackageConfigViewModel.setTotalCost(NumberFormatUtil.format(advertisementPackageConfig.getTotalCost()));
            return detailAdvertisementPackageConfigViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "新增广告展示次数配置", notes = "创建人：文丰")
    @PostMapping(value = "/displayTimesConfig/create")
    public InvokeResult createAdvertisementDisplayTimesConfig(@Validated @RequestBody BaseAdvertisementDisplayTimesConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig=new AdvertisementDisplayTimesConfig();
        BeanUtils.copyProperties(viewModel,advertisementDisplayTimesConfig);
        advertisementDisplayTimesConfigService.createAdvertisementDisplayTimesConfig(advertisementDisplayTimesConfig);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "删除广告展示次数配置", notes = "创建人：文丰")
    @PostMapping(value = "/displayTimesConfig/delete/{id}")
    public InvokeResult deleteAdvertisementDisplayTimesConfig(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        advertisementDisplayTimesConfigService.deleteAdvertisementDisplayTimesConfig(id);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @ApiOperation(value = "修改广告展示次数配置", notes = "创建人：文丰")
    @PostMapping(value = "/displayTimesConfig/update")
    public InvokeResult updateAdvertisementDisplayTimesConfig(@Validated @RequestBody UpdateAdvertisementDisplayTimesConfigViewModel viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig=new AdvertisementDisplayTimesConfig();
        BeanUtils.copyProperties(viewModel,advertisementDisplayTimesConfig);
        advertisementDisplayTimesConfigService.updateAdvertisementDisplayTimesConfig(advertisementDisplayTimesConfig);
        return InvokeResult.SuccessResult();
    }
    
    @ApiOperation(value = "查询广告展示次数配置详情", notes = "创建人：文丰")
    @RequestMapping(value = "/displayTimesConfig/{id}", method = RequestMethod.GET)
    public InvokeResult<DetailAdvertisementDisplayTimesConfigViewModel> getAdvertisementDisplayTimesConfig(@PathVariable("id") String id) {
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");
        AdvertisementDisplayTimesConfig advertisementDisplayTimesConfig= advertisementDisplayTimesConfigService.getAdvertisementDisplayTimesConfig(id);
        DetailAdvertisementDisplayTimesConfigViewModel detailViewModel=new DetailAdvertisementDisplayTimesConfigViewModel();
        BeanUtils.copyProperties(advertisementDisplayTimesConfig,detailViewModel);
        detailViewModel.setTimeUnitName(EnumUtils.getDisplayName(advertisementDisplayTimesConfig.getTimeUnit(),DisplayTimeUnitEnum.class));
        detailViewModel.setCreator(getUserNickname(advertisementDisplayTimesConfig.getCreatorId()));
        return InvokeResult.SuccessResult(detailViewModel);
    }

    @ApiOperation(value = "查询广告展示次数配置列表", notes = "创建人：文丰")
    @PostMapping(value = "/displayTimesConfig")
    public InvokeResult<PageList<DetailAdvertisementDisplayTimesConfigViewModel>> getAdvertisementDisplayTimesConfigList(@Validated @RequestBody BasePageInfo viewModel, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);
        Pageable pageable = new MyPageRequest(viewModel.getPageIndex(), viewModel.getPageSize(), new QSort(QAdvertisementDisplayTimesConfig.advertisementDisplayTimesConfig.createdTime.desc()));
        Page<AdvertisementDisplayTimesConfig> pages = advertisementDisplayTimesConfigService.queryAdvertisementDisplayTimesConfigList(pageable);
        PageList<DetailAdvertisementDisplayTimesConfigViewModel> resultList= ApiBeanUtils.convertToPageList(pages, advertisementDisplayTimesConfig ->
        {
            DetailAdvertisementDisplayTimesConfigViewModel detailAdvertisementDisplayTimesConfigView=new DetailAdvertisementDisplayTimesConfigViewModel();
            BeanUtils.copyProperties(advertisementDisplayTimesConfig,detailAdvertisementDisplayTimesConfigView);
            detailAdvertisementDisplayTimesConfigView.setCreator(getUserNickname(advertisementDisplayTimesConfig.getCreatorId()));
            detailAdvertisementDisplayTimesConfigView.setTimeUnitName(EnumUtils.getDisplayName(advertisementDisplayTimesConfig.getTimeUnit(),DisplayTimeUnitEnum.class));
            return detailAdvertisementDisplayTimesConfigView;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "获取合同广告相关配置", notes = "创建人: 王伟权")
    @GetMapping(value = "/contractAdvertisementConfig/{contractId}")
    public InvokeResult<List<TerminalAdvertisementConfigInfo>> getContractAdvertisementConfig(@PathVariable String contractId) {
        if(StringUtils.isEmpty(contractId))
            return InvokeResult.Fail("请输入有效的合同id");
        return InvokeResult.SuccessResult(advertisementService.getContractAdvertisementConfig(contractId));
    }

    @ApiOperation(value = "广告活跃度计费配置枚举选项", notes = "创建人: 李川")
    @GetMapping(value = "/getAdvertisementActiveDegreeProfitConfigItems")
    public InvokeResult<Iterable<EnumItemInfo>> getAdvertisementActiveDegreeProfitConfigItems() {
        return InvokeResult.SuccessResult(
                Linq4j.asEnumerable(EnumUtils.getAllItems(AdvertisementActiveDegreeProfitConfigCellType.class))
                .select(item->new EnumItemInfo(item.getValue(),item.getDisplayName(), item.getDemoData()))
        );
    }

    @ApiOperation(value = "广告分成配置Demo", notes = "创建人: 李川")
    @PostMapping(value = "/getAdvertisementProfitConfig")
    public InvokeResult getAdvertisementProfitConfig(@Validated @RequestBody AdvertisementProfitConfig advertisementProfitConfig, BindingResult result) {
        return InvokeResult.SuccessResult();
    }
}