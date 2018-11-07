package com.sztouyun.advertisingsystem.common.jpa.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.common.jpa.JoinFetchCapableQueryDslJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JoinFetchCapableQueryDslJpaRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements JoinFetchCapableQueryDslJpaRepository<T> {
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public JoinFetchCapableQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public JoinFetchCapableQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
        super(entityInformation, entityManager, resolver);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder(this.path.getType(), this.path.getMetadata());
        this.querydsl = new Querydsl(entityManager, this.builder);
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable, JoinDescriptor joinDescriptor) {
        JPQLQuery<T> query = this.querydsl.applyPagination(pageable, this.createQuery(joinDescriptor, new Predicate[]{predicate}).select(this.path));
        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> count(predicate,joinDescriptor));
    }

    @Override
    public long count(Predicate predicate, JoinDescriptor joinDescriptor) {
        return this.createQuery(joinDescriptor, new Predicate[]{predicate}).fetchCount();
    }

    @Override
    public boolean exists(Predicate predicate, JoinDescriptor joinDescriptor) {
        return this.createQuery(joinDescriptor, new Predicate[]{predicate}).fetchCount() > 0L;
    }

    @Override
    public <V> V findOne(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return this.createQuery(createQueryFactory).fetchFirst();
    }

    @Override
    public <V> List<V> findAll(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return this.createQuery(createQueryFactory).fetch();
    }

    @Override
    public <V> Page<V> findAll(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, Pageable pageable) {
        QueryResults<V> queryResults = this.createQuery(createQueryFactory).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public <V> long count(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return this.createQuery(createQueryFactory).fetchCount();
    }

    @Override
    public <V> boolean exists(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return this.createQuery(createQueryFactory).fetchCount() > 0L;
    }

    @Override
    public List<T> findAll(Predicate predicate, JoinDescriptor joinDescriptor) {
        return this.createQuery(joinDescriptor, new Predicate[]{predicate}).select(this.path).fetch();
    }

    @Override
    public T findOne(Predicate predicate, JoinDescriptor joinDescriptor) {
        return this.createQuery(joinDescriptor, new Predicate[]{predicate}).select(this.path).fetchFirst();
    }

    @Override
    public T findOne(Predicate predicate) {
        return createQuery(predicate).select(path).fetchFirst();
    }

    protected <V> JPQLQuery<V> createQuery(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        AbstractJPAQuery jpaQuery = createQueryFactory.apply(queryFactory);
        JPQLQuery<V> query = (JPQLQuery<V>) createQuery(jpaQuery);
        return query;
    }

    protected JPQLQuery<?> createQuery(JoinDescriptor joinDescriptor, Predicate... predicate) {
        JPAQueryBase jpaQuery = this.querydsl.createQuery(new EntityPath[]{this.path});
        if (joinDescriptor != null) {
            jpaQuery = joinDescriptor.includeJoin(jpaQuery);
        }
        AbstractJPAQuery<?, ?> query = (AbstractJPAQuery) jpaQuery.where(predicate);
        return createQuery(query);
    }

    protected JPQLQuery<?> createQuery(AbstractJPAQuery query) {
        CrudMethodMetadata metadata = this.getRepositoryMethodMetadata();
        if (metadata == null) {
            return query;
        } else {
            LockModeType type = metadata.getLockModeType();
            query = type == null ? query : query.setLockMode(type);
            Iterator entry = this.getQueryHints().entrySet().iterator();

            while (entry.hasNext()) {
                Map.Entry<String, Object> hint = (Map.Entry) entry.next();
                query.setHint((String) hint.getKey(), hint.getValue());
            }
            return query;
        }
    }

}
