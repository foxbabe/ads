package com.sztouyun.advertisingsystem.repository.customer;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;

@CacheConfig(cacheNames = "customers")
public interface CustomerRepository extends BaseRepository<Customer> {
    Integer countByName(String name);

    @Query(value = "select 1 from  Customer c where c.id<>?1 and c.name=?2")
    String  findByNameAndId(String id, String name);

    @CacheEvict(key = "#p0.getId()")
    @Override
    Customer save(Customer s);

    @CacheEvict(key = "#p0")
    @Override
    void delete(String id);

    @Override
    default BooleanBuilder getAuthorizationFilter(){
          return AuthenticationService.getUserAuthenticationFilter(QCustomer.customer.ownerId);
    }

    boolean existsByOwnerId(String ownerId);

    boolean existsByCreatorId(String creatorId);

    @Query(value = "select c.ownerId from  Customer c where c.id=?1 ")
    String findOwnerIdById(String id);
}
