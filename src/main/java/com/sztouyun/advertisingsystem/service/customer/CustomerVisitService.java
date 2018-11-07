package com.sztouyun.advertisingsystem.service.customer;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.customer.CustomerVisit;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.customer.QCustomerVisit;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerVisitRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit.CustomerVisitListRequest;
import com.sztouyun.advertisingsystem.viewmodel.customer.CustomerVisit.CustomerVisitRequest;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;


@Service
public class CustomerVisitService extends BaseService {
    @Autowired
    private CustomerVisitRepository customerVisitRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private final QCustomerVisit qCustomerVisit = QCustomerVisit.customerVisit;
    private final QCustomer qCustomer=QCustomer.customer;

    @Transactional
    public void createCustomerVisit(CustomerVisit customerVisit){
        if(!customerRepository.existsAuthorized(qCustomer.id.eq(customerVisit.getCustomerId())))
            throw new BusinessException("客户ID无效");
        customerVisitRepository.save(customerVisit);
    }

    @Transactional
    public void updateCustomerVisit(CustomerVisit customerVisit){
        CustomerVisit oldCustomerVisit=customerVisitRepository.findOneAuthorized(qCustomerVisit.id.eq(customerVisit.getId()));
        if(null==oldCustomerVisit)
            throw new BusinessException("客户拜访ID无效");
        var loginUser=getUser();
        if(loginUser.getRoleTypeEnum().getValue().equals(RoleTypeEnum.SaleMan.getValue()) && !oldCustomerVisit.getCreatorId().equals(loginUser.getId()))
            throw new BusinessException("没有编辑权限");
        if(!customerRepository.existsAuthorized(qCustomer.id.eq(customerVisit.getCustomerId())))
            throw new BusinessException("没有操作权限");
        customerVisitRepository.save(customerVisit);
    }

   public CustomerVisit  getCustomerVisit(String id){
        CustomerVisit customerVisit=customerVisitRepository.findOneAuthorized(qCustomerVisit.id.eq(id));
        if(null==customerVisit)
            throw  new BusinessException("客户拜访ID无效");
        return customerVisit;
   }

   public Page<CustomerVisit> getCustomerVisitList(CustomerVisitListRequest request){
       BooleanBuilder predicate =new BooleanBuilder();
       Pageable pageable=new PageRequest(request.getPageIndex(),request.getPageSize(),new QSort(qCustomerVisit.updatedTime.desc()));
       if(!StringUtils.isEmpty(request.getCustomerName())){
           predicate.and(qCustomerVisit.customerName.contains(request.getCustomerName()));
       }
       if(!StringUtils.isEmpty(request.getNickname())){
           predicate.and(qCustomerVisit.creator.nickname.contains(request.getNickname()));
       }
       if(!StringUtils.isEmpty(request.getVisitor())){
           predicate.and(qCustomerVisit.creator.nickname.contains(request.getVisitor()));
       }
       if(Objects.nonNull(request.getStartTime())){
           predicate.and(qCustomerVisit.visitTime.goe(request.getStartTime()));
       }
       if(Objects.nonNull(request.getEndTime())){
           predicate.and(qCustomerVisit.visitTime.loe(request.getEndTime()));
       }
       return  customerVisitRepository.findAllAuthorized(predicate,pageable);
   }

   public Long getVisitTimes(String customerId){
       return customerVisitRepository.count(qCustomerVisit.customerId.eq(customerId));
   }

   public Date getLastestVisitTime(String customerId){
       if(StringUtils.isEmpty(customerId))
           return null;
       return customerVisitRepository.findOne(q->q
               .select(qCustomerVisit.visitTime).from(qCustomerVisit)
               .where(qCustomerVisit.customerId.eq(customerId)).orderBy(qCustomerVisit.visitTime.desc()));
   }

    public Page<CustomerVisit> getCustomerVisitList(CustomerVisitRequest request){
        Pageable pageable=new PageRequest(request.getPageIndex(),request.getPageSize(),new QSort(qCustomerVisit.updatedTime.desc()));
        return  customerVisitRepository.findAll(qCustomerVisit.customerId.eq(request.getCustomerId()),pageable);
    }
}
