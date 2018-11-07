package com.sztouyun.advertisingsystem.api.customer;

import com.sztouyun.advertisingsystem.api.BaseApiController;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.Advertisement;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.Contract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.CustomerExtension;
import com.sztouyun.advertisingsystem.model.customer.CustomerVisit;
import com.sztouyun.advertisingsystem.model.material.Material;
import com.sztouyun.advertisingsystem.model.system.TerminalTypeEnum;
import com.sztouyun.advertisingsystem.service.account.UserService;
import com.sztouyun.advertisingsystem.service.advertisement.AdvertisementService;
import com.sztouyun.advertisingsystem.service.customer.CustomerService;
import com.sztouyun.advertisingsystem.service.customer.CustomerVisitService;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.EnumUtils;
import com.sztouyun.advertisingsystem.utils.NumberFormatUtil;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.CustomerAdvertisementListItemViewModel;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.CustomerAdvertisementRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.PageList;
import com.sztouyun.advertisingsystem.viewmodel.customer.*;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit.CustomerVisitListItemViewModel;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit.CustomerVisitRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Api(value = "客户接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerApiController extends BaseApiController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private  AdvertisementService advertisementService;
    @Autowired
    private CustomerVisitService customerVisitService;
    @Autowired
    private UserService userService;

    @ApiOperation(value="新建客户",notes = "修改人: 王伟权")
    @RequestMapping(value="/create",method = RequestMethod.POST)
    public InvokeResult<String> createCustomer(@Validated @RequestBody CreateCustomerViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Customer customer=new Customer();
        BeanUtils.copyProperties(viewModel, customer);
        CustomerExtension customerExtension = ApiBeanUtils.copyProperties(viewModel, CustomerExtension.class);
        customerExtension.setCustomer(customer);
        customer.setCustomerExtension(customerExtension);

        return InvokeResult.SuccessResult(customerService.createCustomer(customer));
    }

    @ApiOperation(value="修改客户",notes = "创建人：张瑞兵")
    @RequestMapping(value="/update",method = RequestMethod.POST)
    public InvokeResult updateCustomer(@Validated @RequestBody UpdateCustomerViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Customer customer=customerService.getCustomer(viewModel.getId());;
        BeanUtils.copyProperties(viewModel, customer);
        BeanUtils.copyProperties(viewModel, customer.getCustomerExtension());
        customerService.updateCustomer(customer);
        return InvokeResult.SuccessResult();
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "删除客户", notes = "创建人：陈化静")
    @RequestMapping(value="{id}/delete",method = RequestMethod.POST)
    public InvokeResult deleteCustomer(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        customerService.deleteCustomer(id);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "查询客户信息",notes = "创建人：陈化静")
    @RequestMapping(value="{id}",method = RequestMethod.GET)
    public InvokeResult<CustomerDetailViewModel> queryCustomerDetail(@PathVariable String id){
        if(StringUtils.isEmpty(id))
            return InvokeResult.Fail("id不能为空");

        Customer customer = customerService.getCustomer(id);
        CustomerDetailViewModel viewModel = new CustomerDetailViewModel();
        BeanUtils.copyProperties(customer, viewModel);
        viewModel.setOwnerName(getUserNickname(customer.getOwnerId()));
        setArea(customer, viewModel);
        Long visitTimes=customerVisitService.getVisitTimes(id);
        Date lastestVisitTime=customerVisitService.getLastestVisitTime(id);
        viewModel.setCreator(getUserNickname(customer.getCreatorId()));
        viewModel.setVisitTimes(visitTimes);
        viewModel.setLastestVisitTime(lastestVisitTime);
        List<CustomerAdvertisementStatistic> advertisementStatistics = customerService.getCustomerAdvertisementStatistics(Arrays.asList(id));
        List<CustomerContractStatistic> contractStatistics = customerService.getCustomerContractStatistics(Arrays.asList(id));
        List<User> customerUser = userService.getCustomerUser(Arrays.asList(id));
        if(customerUser!=null && customerUser.size()==1){
            viewModel.setCreateAccount(Boolean.TRUE);
            viewModel.setEnable(!customerUser.get(0).isEnable());
        }
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
        viewModel.setIndustryFlag(getDataDictName(customer.getIndustryId()));
        viewModel.setSubIndustryFlag(getDataDictName(customer.getSubIndustryId()));

        CustomerExtension customerExtension = customer.getCustomerExtension();
        BeanUtils.copyProperties(customerExtension, viewModel);

        return InvokeResult.SuccessResult(viewModel);
    }

    private void setArea(Customer customer, CustomerDetailViewModel viewModel) {

        String cityName=getAreaName(customer.getCityId());
        String provinceName=getAreaName(customer.getProvinceId());
        String regionName=getAreaName(customer.getRegionId());
        viewModel.setCityName(cityName);
        viewModel.setProvinceName(provinceName);
        viewModel.setRegionName(regionName);
        viewModel.setRegion(provinceName+"/"+cityName+"/"+regionName);
    }

    @ApiOperation(value = "查询客户列表",notes = "创建人：王英峰")
    @RequestMapping(value="",method = RequestMethod.POST)
    public InvokeResult<PageList<CustomerListItem>> queryCustomers(@Validated  @RequestBody CustomerBasePageInfoViewModel viewModel, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);

        Pageable pageable= new MyPageRequest(viewModel.getPageIndex(),viewModel.getPageSize());
        Page<Customer> pages=customerService.queryCustomerList(viewModel.getCustomername(),viewModel.getNickname(),viewModel.getAreaId(),pageable);
        List<CustomerAdvertisementStatistic> customerAdvertisementStatistics = new ArrayList<>();
        List<CustomerContractStatistic> customerContractStatistics = new ArrayList<>();
        List<User> customerUser = new ArrayList<>();
        if(pages.getContent() !=null && pages.getContent().size()>0){
            List<String> customerIds = Linq4j.asEnumerable(pages.getContent()).select(c->c.getId()).toList();
            customerAdvertisementStatistics = customerService.getCustomerAdvertisementStatistics(customerIds);
            customerContractStatistics= customerService.getCustomerContractStatistics(customerIds);
            customerUser = userService.getCustomerUser(customerIds);
        }
        Enumerable<CustomerAdvertisementStatistic> customerStatistics = Linq4j.asEnumerable(customerAdvertisementStatistics);
        Enumerable<CustomerContractStatistic> contractStatistic = Linq4j.asEnumerable(customerContractStatistics);
        Map<String, User> userCustomerIdMap = Linq4j.asEnumerable(customerUser).toMap(q -> q.getCustomerId(), b -> b);
        PageList<CustomerListItem> pageList= ApiBeanUtils.convertToPageList(pages, customer->{
            CustomerListItem customerListItem=new CustomerListItem();
            BeanUtils.copyProperties(customer,customerListItem);
            customerListItem.setOwnerName(getUserNickname(customer.getOwnerId()));
            customerListItem.setCityName(getAreaName(customer.getCityId()));
            User user = userCustomerIdMap.get(customer.getId());
            if(user!=null){
                customerListItem.setCreateAccount(Boolean.TRUE);
                customerListItem.setEnable(!user.isEnable());
            }
            CustomerAdvertisementStatistic advertisementStatistic = customerStatistics.firstOrDefault(s->s.getCustomerId().equals(customer.getId()));
            if(advertisementStatistic != null){
                customerListItem.setAdvertisementDeliveryTimes(advertisementStatistic.getAdvertisementDeliveryTimes());
                customerListItem.setDelivering(advertisementStatistic.getAdvertisingDeliveringCount()>0);
            }
            CustomerContractStatistic customerContractStatistic = contractStatistic.firstOrDefault(s->s.getCustomerId().equals(customer.getId()));
            if(customerContractStatistic != null){
                customerListItem.setAdvertisementTotalAmount(NumberFormatUtil.format(customerContractStatistic.getContractTotalAmount()));
            }
            return customerListItem;
        });
        return InvokeResult.SuccessResult(pageList);
    }

    @PreAuthorize("hasAnyRole('Admin', 'ManagerialStaff')")
    @ApiOperation(value = "为客户分配业务员",notes = "修改人：王伟权")
    @RequestMapping(value="distribute/{customerId}/{userId}",method = RequestMethod.POST)
    public InvokeResult distributeCustomer(@PathVariable String customerId, @PathVariable String userId){
        if(StringUtils.isEmpty(customerId))
            return InvokeResult.Fail("客户id不能为空");
        if(StringUtils.isEmpty(userId))
            return InvokeResult.Fail("业务员id不能为空");
        customerService.distributeCustomer(customerId,userId);
        return InvokeResult.SuccessResult();
    }

    @ApiOperation(value = "获取有待执行,执行中合同的客户列表",notes = "创建人：李川")
    @PostMapping(value="customerContractStatistic")
    public InvokeResult<PageList<CustomerPendingExecutionContractViewModel>> getCustomerContractStatistic(@RequestBody CustomerQueryRequest request){
        Pageable pageable= new MyPageRequest(request.getPageIndex(),request.getPageSize());
        Page<CustomerPendingExecutionContractStatistic>  list = customerService.getCustomerPendingExecutionContractStatistic(request.getCustomerName(),pageable);
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(list,s->{
            CustomerPendingExecutionContractViewModel viewModel = ApiBeanUtils.copyProperties(s,CustomerPendingExecutionContractViewModel.class);
            viewModel.setCustomerName(getCustomerName(s.getCustomerId()));
            return viewModel;
        }));
    }

    @ApiOperation(value = "获取客户详情下面的广告列表",notes = "创建人：文丰")
    @PostMapping(value = "advertisementList")
    public InvokeResult<PageList<CustomerAdvertisementListItemViewModel>> getAdvertisementListByCustomer(@Validated @RequestBody CustomerAdvertisementRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<Advertisement>  pages=advertisementService.getAdvertisementListByCustomer(request);
        Map<String, List<String>> advertisementDeliveryPlatforms = advertisementService.getAdvertisementDeliveryPlatforms(pages.map(d->d.getContractId()).getContent());
        return InvokeResult.SuccessResult(ApiBeanUtils.convertToPageList(pages,advertisement->{
            CustomerAdvertisementListItemViewModel viewModel = ApiBeanUtils.copyProperties(advertisement,CustomerAdvertisementListItemViewModel.class);
            Contract contract=advertisement.getContract();
            viewModel.setContractName(contract.getContractName());
            viewModel.setTotalCost(NumberFormatUtil.format(contract.getTotalCost()));
            viewModel.setAdvertisementStatusName(EnumUtils.getDisplayName(advertisement.getAdvertisementStatus(),AdvertisementStatusEnum.class));
            viewModel.setAdvertisementType(EnumUtils.getDisplayName(advertisement.getAdvertisementType(),MaterialTypeEnum.class));
            viewModel.setOwner(getUserNickname(advertisement.getContract().getOwnerId()));
            viewModel.setCanView(canView(Arrays.asList(advertisement.getCreatorId(), contract.getOwnerId())));
            List<String> platforms = advertisementDeliveryPlatforms.get(advertisement.getContractId());
            if(EnumUtils.toValueMap(TerminalTypeEnum.class).size()==platforms.size()){
                viewModel.setTerminalNames( Constant.ALL_PLATFORM);
            }else{
                viewModel.setTerminalNames( StringUtils.join(platforms,Constant.DELIVERYPLATFORMSEPARATOR));
            }
            return viewModel;
        }));
    }


    @ApiOperation(value = "获取客户详情素材列表", notes = "创建人: 王伟权")
    @PostMapping(value = "advertisementMaterialList")
    public InvokeResult<PageList<CustomerAdvertisementMaterialViewModel>> getAdvertisementMaterialsByCustomer(@Validated @RequestBody CustomerAdvertisementRequest request, BindingResult result) {
        if(result.hasErrors())
            return ValidateFailResult(result);

        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize());
        Page<Material> pages = advertisementService.getAdvertisementMaterialsByCustomer(pageable, request.getCustomerId());

        PageList<CustomerAdvertisementMaterialViewModel> resultList = ApiBeanUtils.convertToPageList(pages, advertisementMaterial -> {

            CustomerAdvertisementMaterialViewModel customerAdvertisementMaterialViewModel = ApiBeanUtils.copyProperties(advertisementMaterial, CustomerAdvertisementMaterialViewModel.class);
            customerAdvertisementMaterialViewModel.setMaterialTypeName(EnumUtils.toEnum(advertisementMaterial.getMaterialType(), MaterialTypeEnum.class).getDisplayName());
            customerAdvertisementMaterialViewModel.setCreatorName(getUserNickname(advertisementMaterial.getCreatorId()));

            if(MaterialTypeEnum.Text.getValue().equals(advertisementMaterial.getMaterialType())) {
                customerAdvertisementMaterialViewModel.setMaterialName(advertisementMaterial.getData());
            } else {
                customerAdvertisementMaterialViewModel.setMaterialName(advertisementMaterial.getFileName());
                customerAdvertisementMaterialViewModel.setUrl(advertisementMaterial.getData());
            }
            return customerAdvertisementMaterialViewModel;
        });
        return InvokeResult.SuccessResult(resultList);
    }

    @ApiOperation(value = "获取指定客户的拜访记录", notes = "创建人:文丰")
    @PostMapping(value = "visitList")
    public InvokeResult<PageList<CustomerVisitListItemViewModel>> getCustomerVisitList(@Validated @RequestBody CustomerVisitRequest request, BindingResult result){
        if(result.hasErrors())
            return ValidateFailResult(result);
        Page<CustomerVisit> pages=customerVisitService.getCustomerVisitList(request);
        PageList<CustomerVisitListItemViewModel> list=ApiBeanUtils.convertToPageList(pages,customerVisit -> {
            CustomerVisitListItemViewModel item=ApiBeanUtils.copyProperties(customerVisit,CustomerVisitListItemViewModel.class);
            item.setCreator(getUserNickname(customerVisit.getCreatorId()));
            item.setVisitor(getUserNickname(customerVisit.getCreatorId()));
            item.setCanEdit(false);
            return item;
        });
        return InvokeResult.SuccessResult(list);
    }
}
