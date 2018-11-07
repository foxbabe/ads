package com.sztouyun.advertisingsystem.api.system;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.system.Area;
import com.sztouyun.advertisingsystem.service.contract.ContractService;
import com.sztouyun.advertisingsystem.service.customerStore.CustomerStorePlanService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.service.system.AreaService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.DistanceUtil;
import com.sztouyun.advertisingsystem.viewmodel.store.ContractStoreCheckTreeViewModel;
import com.sztouyun.advertisingsystem.viewmodel.store.CustomerStoreInfoAreaSelectedRequest;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreInfoAreaSelectedViewModel;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaStoreInfo;
import com.sztouyun.advertisingsystem.viewmodel.system.AreaViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by szty on 2017/7/25.
 */
@Api(value = "地区管理接口")
@RestController
@CacheConfig(cacheNames = "areas")
@RequestMapping("/api/area")
public class AreaController extends BaseApiController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CustomerStorePlanService customerStorePlanService;

    @ApiOperation(value = "查询下级地区", notes = "创建人：张瑞兵")
    @GetMapping("/{areaId}/sub")
    public InvokeResult<List<AreaViewModel>> getSubAreas(@PathVariable("areaId") String areaId) {
        Iterable<Area> areas = areaService.getSubAreas(areaId);
        List<AreaViewModel> list = Linq4j.asEnumerable(areas)
                .select(a -> ApiBeanUtils.copyProperties(a, AreaViewModel.class)).toList();
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询有客户的地区", notes = "创建人：张瑞兵")
    @PostMapping("/existsCustomer")
    public InvokeResult<List<AreaViewModel>> getAreasIfExistsCustomer() {
        List<Area> areas = areaService.getExistsCustomerAreas();
        List<AreaViewModel> list = ApiBeanUtils.convertToAreaTreeList(areas, area -> ApiBeanUtils.copyProperties(area, AreaViewModel.class), Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value = "查询包含门店信息的地区", notes = "修改人: 王伟权")
    @PostMapping("/containStoreInfo")
    public InvokeResult<List<AreaViewModel>> getAreaByContainStoreInfo(@Validated @RequestBody StoreInfoAreaSelectedViewModel viewModel, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        fillDataInAreaByContainStoreInfo(viewModel);

        List<AreaStoreInfo> areas = areaService.getAreaByContainStoreInfo(viewModel);
        addSpecialNode(areas, viewModel.getStoreType(), viewModel.getContractId());

        List<ContractStoreCheckTreeViewModel> contractStoreCheckList = contractService.getContractStoreAreaIds(viewModel);


        Boolean existsTestContractStore = contractService.existsTestContractStore(viewModel);
        existsTestContractStore = existsTestContractStore == null ? Boolean.FALSE : existsTestContractStore;
        Boolean finalExistsTestContractStore = existsTestContractStore;

        boolean hasTestInfo = Linq4j.asEnumerable(contractStoreCheckList).any(region -> region.getIsTest().equals(Boolean.TRUE));
        boolean hasAbnormalInfo = Linq4j.asEnumerable(contractStoreCheckList).any(region -> region.getRegionId().equals("") && region.getIsTest().equals(Boolean.FALSE));
        List<String> regionIds = Linq4j.asEnumerable(contractStoreCheckList).where(a -> a.getIsTest().equals(false) && !a.getRegionId().equals("")).select(a -> a.getRegionId()).toList();

        List<AreaViewModel> list = ApiBeanUtils.convertToAreaTreeListWithRootNode(areas, area -> {
           AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            if (regionIds.contains(area.getId())) {
                areaViewModel.setChecked(true);
            }
           if(areaViewModel.getName().equals(Constant.AREA_ABNORMAL_NODE_NAME) && hasAbnormalInfo) {
               areaViewModel.setChecked(true);
           }
           if(areaViewModel.getName().equals(Constant.AREA_TEST_NODE_NAME) && (finalExistsTestContractStore || hasTestInfo)) {
               areaViewModel.setChecked(true);
           }
           return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }


    @ApiOperation(value = "查询客户选点门店所在的地区", notes = "创建人：文丰")
    @PostMapping("/customerContainStoreInfo")
    public InvokeResult<List<AreaViewModel>> getAreaByContainStoreInfo(@Validated @RequestBody CustomerStoreInfoAreaSelectedRequest viewModel, BindingResult result) {
        if (result.hasErrors()) {
            return ValidateFailResult(result);
        }
        fillDataInAreaByContainStoreInfo(viewModel);

        List<AreaStoreInfo> areas = areaService.getCustomerStoreArea(viewModel);
        addSpecialWithoutTestNode(areas,viewModel.getCustomerStorePlanId());

        List<String> customerStoreCheckIdList = customerStorePlanService.getCustomerCheckedStoreAreaIds(viewModel);
        boolean hasAbnormalInfo = customerStoreCheckIdList.stream().anyMatch(a->StringUtils.isEmpty(a));
        List<String> regionIds = customerStoreCheckIdList.stream().filter(a->!StringUtils.isEmpty(a)).collect(Collectors.toList());

        List<AreaViewModel> list = ApiBeanUtils.convertToAreaTreeListWithRootNode(areas, area -> {
            AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            if (regionIds.contains(area.getId())) {
                areaViewModel.setChecked(true);
            }
            if(areaViewModel.getName().equals(Constant.AREA_ABNORMAL_NODE_NAME) && hasAbnormalInfo) {
                areaViewModel.setChecked(true);
            }
            return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    private  void fillDataInAreaByContainStoreInfo(StoreInfoAreaSelectedViewModel viewModel) {
        if (viewModel.getLongitude() != null && viewModel.getLatitude() != null && viewModel.getDistance() != null) {
            viewModel.setStoreDataMapInfo(DistanceUtil.returnSquarePoint(viewModel.getLongitude(), viewModel.getLatitude(), viewModel.getDistance()));
        }

        if (viewModel.getIsPaveGoods() != null) {
            viewModel.getPaveGoodsConditionInfo().setIsPaveGoods(viewModel.getIsPaveGoods());
        }
        if (!CollectionUtils.isEmpty(viewModel.getPaveGoodsList())) {
            viewModel.getPaveGoodsConditionInfo().setPaveGoodsList(viewModel.getPaveGoodsList());
        }

        List<String> areaIds = Arrays.asList(viewModel.getAreaIds().split(","));
        viewModel.setRegionIds(areaService.filterAreaIdsByLevel(areaIds, 3));
        viewModel.setHasAbnormalNode(areaIds.contains(Constant.AREA_ABNORMAL_NODE_ID));//选中无省市区节点
        viewModel.setHasTestNode(areaIds.contains(Constant.AREA_TEST_NODE_ID));
    }

    private  void fillDataInAreaByContainStoreInfo(CustomerStoreInfoAreaSelectedRequest viewModel) {
        if (viewModel.getLongitude() != null && viewModel.getLatitude() != null && viewModel.getDistance() != null) {
            viewModel.setStoreDataMapInfo(DistanceUtil.returnSquarePoint(viewModel.getLongitude(), viewModel.getLatitude(), viewModel.getDistance()));
        }
        List<String> areaIds = Arrays.asList(viewModel.getAreaIds().split(","));
        viewModel.setRegionIds(areaService.filterAreaIdsByLevel(areaIds, 3));
        viewModel.setHasAbnormalNode(areaIds.contains(Constant.AREA_ABNORMAL_NODE_ID));//选中无省市区节点
    }

    @ApiOperation(value = "查询合同选中门店中省市区, 组装成树", notes = "修改人: 王伟权")
    @GetMapping("/getAreaInfoByContractSelected/{contractId}")
    public InvokeResult<List<AreaViewModel>> getAreaInfo(@PathVariable String contractId) {

        if (StringUtils.isEmpty(contractId)) {
            return InvokeResult.Fail("合同ID不能为空");
        }
        StoreInfoAreaSelectedViewModel viewModel = new StoreInfoAreaSelectedViewModel();
        viewModel.setContractId(contractId);
        return getAreaList(viewModel);
    }

    @ApiOperation(value="获得广告下门店列表的省市区",notes = "创建人：毛向军")
    @GetMapping("/{contractId}/areaInfo")
    public InvokeResult<List<AreaViewModel>> getAreaInfoByAdvertisement(@PathVariable String contractId) {
        if (StringUtils.isEmpty(contractId)) {
            return InvokeResult.Fail("合同ID不能为空");
        }
        return getAreaList(new StoreInfoAreaSelectedViewModel(contractId));
    }

    @ApiOperation(value="获得存在门店的省市区",notes = "修改人：毛向军")
    @GetMapping("/getAreaInfo")
    @Cacheable(key = "'AreaInfoExistStore'")
    public InvokeResult<List<AreaViewModel>> getAreaInfoExistStore() {
        return getAreaList(null);
    }

    private InvokeResult<List<AreaViewModel>> getAreaList(StoreInfoAreaSelectedViewModel viewModel) {
        List<Area> areas = areaService.getAreaInfo(viewModel);
        List<AreaViewModel> list = ApiBeanUtils.convertToTreeList(areas, area -> {
            AreaViewModel areaViewModel = ApiBeanUtils.copyProperties(area, AreaViewModel.class);
            return areaViewModel;
        }, Constant.TREE_ROOT_ID);
        return InvokeResult.SuccessResult(list);
    }

    @ApiOperation(value="获得存在门店画像的省市区",notes = "修改人：毛向军")
    @GetMapping("/getStorePortraitAreaInfo")
    public InvokeResult<List<AreaViewModel>> getStorePortraitAreaInfo() {
        return getAreaList(new StoreInfoAreaSelectedViewModel(true));
    }

    public void addSpecialNode(List<AreaStoreInfo> areas, Integer storeType, String contractId) {
        areas.add(new AreaStoreInfo(storeService.getAllStoreCountByStoreType(storeType, contractId).intValue(), Constant.AREA_CONTAIN_ALL_NODE_NAME
                , Constant.TREE_ROOT_ID, null));
        int abnormalNodeCount = storeService.getStoreCountInAbnormal(storeType).intValue();
        if(abnormalNodeCount > 0) {
            areas.add(0, new AreaStoreInfo(abnormalNodeCount, Constant.AREA_ABNORMAL_NODE_NAME, Constant.AREA_ABNORMAL_NODE_ID, Constant.TREE_ROOT_ID));
        }

        Long testStoreCount = storeService.getTestStoreCount(storeType);
        if(testStoreCount > 0) {
            areas.add(1, new AreaStoreInfo(testStoreCount.intValue(), Constant.AREA_TEST_NODE_NAME, Constant.AREA_TEST_NODE_ID, Constant.TREE_ROOT_ID));
        }
    }

    public void addSpecialWithoutTestNode(List<AreaStoreInfo> areas,String customerStorePlanID) {
        areas.add(new AreaStoreInfo(storeService.getAllAvailableStoreCount(customerStorePlanID).intValue(), Constant.AREA_CONTAIN_ALL_NODE_NAME
                , Constant.TREE_ROOT_ID, null));
        int abnormalNodeCount = storeService.getStoreCountInAbnormal(customerStorePlanID).intValue();
        if(abnormalNodeCount > 0) {
            areas.add(0, new AreaStoreInfo(abnormalNodeCount, Constant.AREA_ABNORMAL_NODE_NAME, Constant.AREA_ABNORMAL_NODE_ID, Constant.TREE_ROOT_ID));
        }
    }

}
