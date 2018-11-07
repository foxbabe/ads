package com.sztouyun.advertisingsystem.service.demo;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.Customer;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Riber on 2017/8/6 0006.
 */
@Service
public class DemoService extends BaseService {

    //实体管理者
    @Autowired
    private EntityManager entityManager;

    //查询工厂
    private  JPAQueryFactory queryFactory;

    @PostConstruct
    public void init()
    {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Autowired
    private CustomerRepository customerRepository;

    //querydsl使用Demo，参考教程：http://www.jianshu.com/p/99a5ec5c3bd5
    public void  queryDemo(){
        QCustomer qCustomer = QCustomer.customer;
        QContract qContract =QContract.contract;
        //获取单个实体对象
        Customer customer =customerRepository.findOne(
                qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")));

        //获取数量
        Long count =customerRepository.count(
                qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")));

        //判断是否存在
        Boolean isExists =customerRepository.exists(
                qCustomer.name.eq("1").and(qCustomer.id.eq("1").not()));

        //获取实体集合
        Pageable pageable =new PageRequest(0,20);
        Iterable<Customer> customers =customerRepository.findAll(qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")));

        //分页获取实体集合
        Page<Customer> pageCustomers =customerRepository.findAll(qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")),pageable);

        //子查询
        pageCustomers =customerRepository.findAll(qCustomer.id.in(
                JPAExpressions.select(qContract.customerId).from(qContract).where(qContract.id.eq("1"))),pageable);

//--------------------------------------------返回自定义对象---------------------------------
        //获取自定义viewmodel
        DemoViewModel demoViewModel =queryFactory
                .select(Projections.bean(DemoViewModel.class, qCustomer.name, qCustomer.count().as("count")))
                .from(qCustomer)
                .where(qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")))
                .groupBy(qCustomer.name)
                .fetchFirst();

        //获取自定义viewmodel集合
        List<DemoViewModel> demoViewModels =queryFactory
                .select(Projections.bean(DemoViewModel.class, qCustomer.name, qCustomer.count().as("count")))
                .from(qCustomer)
                .where(qCustomer.ownerId.eq("1").and(qCustomer.name.like("1")))
                .groupBy(qCustomer.name)
                .fetch();

        //多表连接查询(cross join)
        List<DemoViewModel> joinQueryResult =
        queryFactory.select(Projections.bean(DemoViewModel.class, qCustomer.name, qContract.count().as("count")))
                .from(qCustomer,qContract).where(qContract.customerId.eq(qCustomer.id))
                .groupBy(qCustomer.name)
                .fetch();

        //多表连接子查询
        List<Customer> subQueryResult = queryFactory.select(qCustomer).from(qCustomer)
                .where(qCustomer.id.in(
                        JPAExpressions.select(qContract.customerId).from(qContract).where(qContract.id.eq("1"))))
                .fetch();

        //多表连接查询分页(inner join, 需要有外键导航字段)
        JPAQuery<DemoViewModel> query= queryFactory.select(Projections.bean(DemoViewModel.class, qContract.contractName, qCustomer.count().as("count")))
                .from(qContract).innerJoin(qContract.customer).groupBy(qContract.contractName);

        //Page<DemoViewModel> demoViewModelPage =fetchPage(query,pageable);
    }


}
