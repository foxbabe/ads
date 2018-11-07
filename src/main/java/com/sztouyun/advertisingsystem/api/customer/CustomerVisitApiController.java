package com.sztouyun.advertisingsystem.api.customer;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.CustomerVisit;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.customer.CustomerVisitService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerAdvertisementStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerContractStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(value = "客户拜访管理接口")
@RestController
@RequestMapping("/api/customerVisit")
public class CustomerVisitApiController extends BaseApiController {
    @Autowired
    private CustomerVisitService customerVisitService;
    @Autowired
    private AdvertisementService advertisementService;

    @ApiOperation(value="新建客户拜访",notes = "创建人：文丰")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public InvokeResult createCustomerVisit(@Validated @RequestBody CreateCustomerVisitViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        CustomerVisit customerVisit= ApiBeanUtils.copyProperties(viewModel,CustomerVisit.class);
        customerVisitService.createCustomerVisit(customerVisit);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="修改客户拜访信息",notes = "创建人：文丰")
    @RequestMapping(value="/update",method = RequestMethod.POST)
    public InvokeResult updateCustomerVisit(@Validated @RequestBody UpdateCustomerVisitViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        CustomerVisit customerVisit= ApiBeanUtils.copyProperties(viewModel,CustomerVisit.class);
        customerVisitService.updateCustomerVisit(customerVisit);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value="客户拜访信息回显",notes = "创建人：文丰")
    @RequestMapping(value="/{id}/editInfo",method = RequestMethod.GET)
    public InvokeResult<UpdateCustomerVisitViewModel> getEditInfo(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("客户拜访ID不能为空");
        CustomerVisit customerVisit= customerVisitService.getCustomerVisit(id);
        UpdateCustomerVisitViewModel updateInfoCustomerVisitViewModel=ApiBeanUtils.copyProperties(customerVisit,UpdateCustomerVisitViewModel.class);
        return InvokeResult.SuccessResult(updateInfoCustomerVisitViewModel);
    }

    @ApiOperation(value="客户拜访详情",notes = "创建人：文丰")
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public InvokeResult<CustomerVisitDetailViewModel> getCustomerVisitDetail(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("客户拜访ID不能为空");
        CustomerVisit customerVisit= customerVisitService.getCustomerVisit(id);
        CustomerVisitDetailViewModel viewModel=getCustomerVisitDetailViewModel(customerVisit.getCustomer());
        BeanUtils.copyProperties(customerVisit, viewModel);
        viewModel.setCreator(getUserNickname(customerVisit.getCreatorId()));
        var loginUser=getUser();
        String customerId=customerVisit.getCustomerId();
        setArea(customerVisit.getCustomer(),viewModel);
        Long visitTimes=customerVisitService.getVisitTimes(customerId);
        Date lastestVisitTime=customerVisitService.getLastestVisitTime(customerId);
        viewModel.setVisitTimes(visitTimes);
        viewModel.setLastestVisitTime(lastestVisitTime);
        if(loginUser.getRoleTypeEnum().getValue().equals(RoleTypeEnum.SaleMan.getValue()) && !customerVisit.getCreatorId().equals(loginUser.getId())) {
            viewModel.setCanEdit(false);
        }
        return InvokeResult.SuccessResult(viewModel);
    }

    private void setArea(Customer customer, CustomerVisitDetailViewModel viewModel) {
        String cityName=getAreaName(customer.getCityId());
        String provinceName=getAreaName(customer.getProvinceId());
        String regionName=getAreaName(customer.getRegionId());
        viewModel.setCityName(cityName);
        viewModel.setProvinceName(provinceName);
        viewModel.setRegion(provinceName+"/"+cityName+"/"+regionName);
    }

    @ApiOperation(value="客户拜访列表",notes = "创建人：文丰")
    @RequestMapping(value="",method = RequestMethod.POST)
    public InvokeResult<PageList<CustomerVisitListItemViewModel>> getCustomerVisitList(@Validated @RequestBody CustomerVisitListRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<CustomerVisit> pages=customerVisitService.getCustomerVisitList(request);
        var loginUser=getUser();
        PageList<CustomerVisitListItemViewModel> list=ApiBeanUtils.convertToPageList(pages,customerVisit -> {
            CustomerVisitListItemViewModel item=ApiBeanUtils.copyProperties(customerVisit,CustomerVisitListItemViewModel.class);
            item.setCreator(getUserNickname(customerVisit.getCreatorId()));
            item.setVisitor(getUserNickname(customerVisit.getCreatorId()));
            if(loginUser.getRoleTypeEnum().getValue().equals(RoleTypeEnum.SaleMan.getValue()) && !customerVisit.getCreatorId().equals(loginUser.getId())) {
                item.setCanEdit(false);
            }
            return item;
        });
        return InvokeResult.SuccessResult(list);
    }

    private CustomerVisitDetailViewModel getCustomerVisitDetailViewModel(Customer customer){
        CustomerVisitDetailViewModel viewModel=ApiBeanUtils.copyProperties(customer,CustomerVisitDetailViewModel.class);
        viewModel.setOwnerName(getUserNickname(customer.getOwnerId()));
        viewModel.setCityName(getAreaName(customer.getCityId()));
        viewModel.setProvinceName(getAreaName(customer.getProvinceId()));
        List<CustomerAdvertisementStatistic> advertisementStatistics = customerService.getCustomerAdvertisementStatistics(Arrays.asList(customer.getId()));
        List<CustomerContractStatistic> contractStatistics = customerService.getCustomerContractStatistics(Arrays.asList(customer.getId()));
        if(advertisementStatistics != null && advertisementStatistics.size() == 1){
            CustomerAdvertisementStatistic statistic =advertisementStatistics.get(0);
            viewModel.setAdvertisementDeliveryTimes(statistic.getAdvertisementDeliveryTimes());
            viewModel.setDelivering(statistic.getAdvertisingDeliveringCount()>0);
        }
        if(contractStatistics != null && contractStatistics.size() == 1){
            CustomerContractStatistic contractStatistic =contractStatistics.get(0);
            viewModel.setAdvertisementTotalAmount(contractStatistic.getContractTotalAmount());
        }
        viewModel.setLastestDeliveryTime(advertisementService.getCustomerLastestDeliveryTime(customer.getId()));
        return viewModel;

    }
}
