package com.sztouyun.advertisingsystem.api.commodity;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.commodity.Supplier;
import com.sztouyun.advertisingsystem.service.commodity.CommodityService;
import com.sztouyun.advertisingsystem.service.commodity.CommodityTypeService;
import com.sztouyun.advertisingsystem.service.commodity.SupplierService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.commodity.*;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品信息接口")
@RestController
@RequestMapping("/api/commodity")
public class CommodityApiController extends BaseApiController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CommodityTypeService commodityTypeService;

    @ApiOperation(value = "查询商品筛选项信息", notes = "创建人：毛向军")
    @RequestMapping(value = "/queryCommodityOptionInfo", method = RequestMethod.POST)
    public InvokeResult<PageList<CommodityOptionViewModel>> queryCommodityOptionInfo(@Validated @RequestBody CommodityOptionRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (request.getCommodityOptionInfos()==null||request.getCommodityOptionInfos().size()==0)
            return InvokeResult.SuccessResult(new PageList<>());
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(commodityService.queryCommodityOptionInfo(request),i->i));

    }

    @ApiOperation(value = "查询商品分类筛选项信息", notes = "创建人：毛向军")
    @RequestMapping(value = "/queryCommodityTypeOptionInfo", method = RequestMethod.POST)
    public InvokeResult<PageList<CommodityTypeOptionViewModel>> queryCommodityTypeOptionInfo(@Validated @RequestBody CommodityTypeOptionRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        if (StringUtils.isEmpty(request.getSupplierId()))
            return InvokeResult.SuccessResult(new PageList<>());
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(commodityService.queryCommodityTypeOptionInfo(request),i->i));
    }

    @ApiOperation(value = "查询供应商筛选项信息", notes = "创建人：毛向军")
    @RequestMapping(value = "/querySupplierOptionInfo", method = RequestMethod.POST)
    public InvokeResult<PageList<SupplierOptionViewModel>> querySupplierOptionInfo(@Validated @RequestBody SupplierOptionRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        Page<Supplier> page = supplierService.querySupplierOptionInfo(request);
        PageList<SupplierOptionViewModel> pageList = ApiBeanUtils.convertToPageList(page, i -> {
            SupplierOptionViewModel viewModel = new SupplierOptionViewModel();
            viewModel.setSupplierId(i.getId());
            viewModel.setSupplierName(i.getName());
            return viewModel;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @ApiOperation(value = "查询门店详情内的供应商筛选项",notes = "创建人：毛向军")
    @RequestMapping(value="/{storeId}/queryStoreSupplierOptionInfo",method = RequestMethod.GET)
    public InvokeResult<List<SupplierOptionViewModel>> queryStoreSupplierOptionInfo(@PathVariable String storeId) {
        if (StringUtils.isEmpty(storeId))
            return InvokeResult.Fail("门店id不能为空");
        return InvokeResult.SuccessResult(commodityService.queryStoreSupplierOptionInfo(storeId));
    }

    @ApiOperation(value = "查询门店详情内的商品分类筛选项",notes = "创建人：毛向军")
    @RequestMapping(value="/queryStoreCommodityTypeOptionInfo",method = RequestMethod.POST)
    public InvokeResult<List<StoreCommodityTypeOptionViewModel>> queryStoreCommodityTypeOptionInfo(@Validated @RequestBody StoreCommodityTypeInfoRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(commodityTypeService.queryStoreCommodityTypeOptionInfo(request));
    }

    @ApiOperation(value = "查询门店详情内的商品", notes = "创建人：毛向军")
    @RequestMapping(value = "/queryStoreCommodityInfo", method = RequestMethod.POST)
    public InvokeResult<PageList<CommodityOptionViewModel>> queryStoreCommodityInfo(@Validated @RequestBody StoreCommodityInfoRequest request, BindingResult result) {
        if (result.hasErrors())
            return ValidateFailResult(result);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(commodityService.queryStoreCommodityInfo(request),i->i));

    }


}