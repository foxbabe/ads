package com.sztouyun.advertisingsystem.internalapi.commodity;

import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.internalapi.BaseInternalApiController;
import com.sztouyun.advertisingsystem.model.commodity.Commodity;
import com.sztouyun.advertisingsystem.model.commodity.CommodityType;
import com.sztouyun.advertisingsystem.model.commodity.Supplier;
import com.sztouyun.advertisingsystem.model.store.StoreCommodity;
import com.sztouyun.advertisingsystem.service.commodity.CommodityService;
import com.sztouyun.advertisingsystem.service.commodity.CommodityTypeService;
import com.sztouyun.advertisingsystem.service.commodity.StoreCommodityService;
import com.sztouyun.advertisingsystem.service.commodity.SupplierService;
import com.sztouyun.advertisingsystem.service.store.StoreService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.internal.commodity.BaseCommodityRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.commodity.CommodityRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.commodity.StoreCommodityRequest;
import com.sztouyun.advertisingsystem.viewmodel.internal.commodity.SupplierRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wenfeng on 2018/3/12.
 */
@Api(value = "商品信息接口")
@RestController
@RequestMapping("/internal/api/commodity")
public class CommodityInternalController extends BaseInternalApiController {
    @Autowired
    private CommodityTypeService commodityTypeService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private StoreCommodityService storeCommodityService;
    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "更新商品类别接口")
    @PostMapping(value = "/updateType")
    public InvokeResult updateCommodityType(@Validated  @RequestBody BaseCommodityRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        if(request.getIsDeleted()!=null && request.getIsDeleted()){
            commodityTypeService.delete(request.getId());
        }else{
            CommodityType commodityType= ApiBeanUtils.copyProperties(request,CommodityType.class);
            commodityTypeService.update(commodityType);
        }
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "更新供应商接口")
    @PostMapping(value = "/updateSupplier")
    public InvokeResult updateSupplier(@Validated  @RequestBody SupplierRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        if(request.getIsDeleted()!=null && request.getIsDeleted()){
            supplierService.delete(request.getId());
        }else{
            Supplier supplier= ApiBeanUtils.copyProperties(request,Supplier.class);
            supplierService.update(supplier);
        }
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "更新商品接口")
    @PostMapping(value = "/updateCommodity")
    public InvokeResult updateCommodity(@Validated  @RequestBody CommodityRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        if(request.getIsDeleted()!=null && request.getIsDeleted()){
            commodityService.delete(request.getId());
        }else{
            Commodity commodity= ApiBeanUtils.copyProperties(request,Commodity.class);
            commodityService.update(commodity);
        }
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "更新门店商品接口")
    @PostMapping(value = "/updateStoreCommodity")
    public InvokeResult updateStoreCommodity(@Validated  @RequestBody StoreCommodityRequest request, BindingResult result){
        if (result.hasErrors())
            return ValidateFailResult(result);
        String storeId=storeService.getStoreIdByStoreNo(request.getStoreNo());
        if(StringUtils.isEmpty(storeId))
            return InvokeResult.Fail("门店不存在");
        StoreCommodity storeCommodity= new StoreCommodity(storeId,request.getCommodityId());
        if(request.getIsDeleted()!=null && request.getIsDeleted()){
            storeCommodityService.delete(storeCommodity);
        }else{
            storeCommodityService.create(storeCommodity);
        }
        return InvokeResult.SuccessResult();
    }



}
