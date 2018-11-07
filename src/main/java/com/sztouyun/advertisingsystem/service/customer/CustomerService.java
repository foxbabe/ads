package com.sztouyun.advertisingsystem.service.customer;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementStatusEnum;
import com.sztouyun.advertisingsystem.model.advertisement.QAdvertisement;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.*;
import com.sztouyun.advertisingsystem.model.customerStore.QCustomerStorePlan;
import com.sztouyun.advertisingsystem.model.material.QMaterial;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerOperationLogRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerVisitRepository;
import com.sztouyun.advertisingsystem.repository.customerStore.CustomerStorePlanRepository;
import com.sztouyun.advertisingsystem.repository.material.MaterialRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import com.sztouyun.advertisingsystem.service.message.event.CustomerOperationEvent;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerAdvertisementStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerContractStatistic;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerPendingExecutionContractStatistic;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


@Service
@CacheConfig(cacheNames = "customers")
public class CustomerService extends BaseService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MaterialRepository advertisementMaterialRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodeGenerationService codeGenerationService;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CustomerVisitRepository customerVisitRepository;
    @Autowired
    private CustomerOperationLogRepository customerOperationLogRepository;
    @Autowired
    private CustomerStorePlanRepository customerStorePlanRepository;

    private final QCustomer qCustomer = QCustomer.customer;
    private final QContract qContract = QContract.contract;
    private final QMaterial qMaterial = QMaterial.material;
    private final QAdvertisement qAdvertisement = QAdvertisement.advertisement;
    private final QCustomerVisit qCustomerVisit=QCustomerVisit.customerVisit;
    private final QCustomerStorePlan qCustomerStorePlan=QCustomerStorePlan.customerStorePlan;

    @Transactional
    public String createCustomer(Customer customer){
        int count = customerRepository.countByName(customer.getName());
        if(count>0)
            throw new BusinessException("客户名称不能重复，无法保存");
        String customerNumber = codeGenerationService.generateCode(CodeTypeEnum.BTE);
        customer.setCustomerNumber(customerNumber);
        var creator = getUser();
        customer.setOwnerId(creator.getId());
        customer.setCreatorId(creator.getId());
        customerRepository.save(customer);
        return customer.getId();
    }

    @Transactional
    public void updateCustomer(Customer customer) {
        boolean isExist = customerRepository.exists(customer.getId());
        if(!isExist)
            throw new BusinessException("客户不存在");
        String isNameExist = customerRepository.findByNameAndId(customer.getId(),customer.getName());
        if(!StringUtils.isEmpty(isNameExist))
            throw new BusinessException("客户名称不能重复，无法保存");
        customer.setUpdatedTime(new Date());
        customerRepository.save(customer);
        CustomerOperationLog customerOperationLog=customerOperationLogRepository.save(new CustomerOperationLog(CustomerOperationTypeEnum.Edit.getValue(),customer.getId(),null,customer.getOwnerId()));
        customerOperationLog.setCreatorId(getUser().getId());
        publishEvent(new CustomerOperationEvent(customerOperationLog));
    }

    @Transactional
    public void deleteCustomer(String customerId ) {
        if(!customerRepository.exists(qCustomer.id.eq(customerId)))
            throw new BusinessException("该客户信息不存在");
        if (contractRepository.exists(qContract.customerId.eq(customerId)) ||
                advertisementRepository.exists(qAdvertisement.customerId.eq(customerId))||
                advertisementMaterialRepository.exists(qMaterial.customerId.eq(customerId)) ||
                customerStorePlanRepository.exists(qCustomerStorePlan.customerId.eq(customerId)) ||
                customerVisitRepository.exists(qCustomerVisit.customerId.eq(customerId)))
            throw new BusinessException("当前客户存在相关业务关联，不能直接删除");
        String ownerId=customerRepository.findOwnerIdById(customerId);
        customerRepository.delete(customerId);
        userRepository.deleteByCustomerId(customerId);
        CustomerOperationLog customerOperationLog=new CustomerOperationLog(CustomerOperationTypeEnum.Delete.getValue(),customerId,null,ownerId);
        customerOperationLogRepository.save(customerOperationLog);
        customerOperationLog.setCreatorId(getUser().getId());
        publishEvent(new CustomerOperationEvent(customerOperationLog));
    }

    public Customer getCustomer(String id){
        if(StringUtils.isEmpty(id))
            throw new BusinessException("客户ID不能为空！");
        Customer customer= customerRepository.findOneAuthorized(qCustomer.id.eq(id));
        if(null == customer)
            throw new BusinessException("客户不存在或者权限不足！");
        return customer;
    }

    public Page<Customer> queryCustomerList(String customerName,String nickname,String areaId,Pageable pageable){
        BooleanBuilder predicate =new BooleanBuilder();
        if(!StringUtils.isEmpty(customerName)){
            predicate.and(qCustomer.name.contains(customerName));
        }
        if(!StringUtils.isEmpty(nickname)){
            predicate.and(qCustomer.owner.nickname.contains(nickname));
        }
        if(!StringUtils.isEmpty(areaId)){
            predicate.and(qCustomer.provinceId.eq(areaId).or(qCustomer.cityId.eq(areaId).or(qCustomer.regionId.eq(areaId))));
        }
        return  customerRepository.findAllAuthorized(predicate,pageable);
    }

    @Transactional
    public void distributeCustomer(String customerId, String userId) {
        Customer customer = customerRepository.findOne(customerId);
        if(null == customer)
            throw new BusinessException("客户不存在");
        User user = userRepository.findById(userId);
        if(null == user)
            throw new BusinessException("业务员不存在");
        String oldCustomerOwnerId = customer.getOwnerId();
        customer.setOwnerId(userId);
        customer.setUpdatedTime(new Date());
        contractRepository.updateUnsignedContractOwner(userId, customerId, oldCustomerOwnerId);//客户合同分配新的负责人
        customerRepository.save(customer);
        CustomerOperationLog customerOperationLog=new CustomerOperationLog(CustomerOperationTypeEnum.Distribute.getValue(),customerId,oldCustomerOwnerId,userId);
        customerOperationLogRepository.save(customerOperationLog);
        customerOperationLog.setCreatorId(getUser().getId());
        publishEvent(new CustomerOperationEvent(customerOperationLog));
    }

    public Page<CustomerPendingExecutionContractStatistic> getCustomerPendingExecutionContractStatistic(String customerName, Pageable pageable){
        BooleanBuilder filter = new BooleanBuilder();
        if(!StringUtils.isEmpty(customerName)){
            filter.and(qContract.customer.name.contains(customerName));
        }
        Page<CustomerPendingExecutionContractStatistic> list = contractRepository.findAllAuthorized(q->q
                .select(Projections.bean(CustomerPendingExecutionContractStatistic.class, qContract.customerId, qContract.id.count().as("pendingExecutionContractCount")))
                .from(qContract).innerJoin(qContract.customer)
                .on(qContract.contractStatus.in(ContractStatusEnum.PendingExecution.getValue(),ContractStatusEnum.Executing.getValue()))
                .where(filter)
                .groupBy(qContract.customerId),pageable);

        return  list;
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public String getCustomerNameFromCache(String customerId) {
        if(StringUtils.isEmpty(customerId))
            return "";
        return customerRepository.findOne(q->q.select(qCustomer.name).from(qCustomer).where(qCustomer.id.eq(customerId)));
    }

    public List<CustomerAdvertisementStatistic> getCustomerAdvertisementStatistics(List<String> customerIds){
        NumberExpression<Integer> deliveringAdvertising = qAdvertisement.advertisementStatus.when(AdvertisementStatusEnum.Delivering.getValue()).then(1).otherwise(0);
        return customerRepository.findAll(q ->q
                .select(Projections.bean(CustomerAdvertisementStatistic.class, qCustomer.id.as("customerId"),
                        qAdvertisement.id.countDistinct().as("advertisementDeliveryTimes"),
                        deliveringAdvertising.sum().as("advertisingDeliveringCount")))
                .from(qCustomer)
                .innerJoin(qCustomer.advertisements,qAdvertisement)
                .where(qCustomer.id.in(customerIds))
                .groupBy(qCustomer.id));
    }

    public List<CustomerContractStatistic> getCustomerContractStatistics(List<String> customerIds){
        return customerRepository.findAll(q ->q
                .select(Projections.bean(CustomerContractStatistic.class, qCustomer.id.as("customerId"),
                        qContract.totalCost.sum().as("contractTotalAmount")))
                .from(qCustomer)
                .innerJoin(qCustomer.contracts,qContract).on(qContract.contractStatus.eq(ContractStatusEnum.Finished.getValue()))
                .where(qCustomer.id.in(customerIds))
                .groupBy(qCustomer.id));
    }

    public Long getAllCustomersByProvince(String provinceId) {
        return customerRepository.countAuthorized(qCustomer.provinceId.eq(provinceId));
    }
}
