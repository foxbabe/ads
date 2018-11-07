package com.sztouyun.advertisingsystem.common.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public interface AuthorizedQueryDslPredicateExecutor<T> extends JoinFetchCapableQueryDslJpaRepository<T> {
    default T findOneAuthorized(Predicate predicate) {
        return findOne(getBaseFilter().and(predicate));
    }

    default Iterable<T> findAllAuthorized(Predicate predicate) {
        return findAll(getBaseFilter().and(predicate));
    }

    default Iterable<T> findAllAuthorized() {
        return findAll(getBaseFilter());
    }

    default Page<T> findAllAuthorized(Predicate predicate, Pageable pageable) {
        return findAll(getBaseFilter().and(predicate), pageable);
    }

    default T findOneAuthorized(Predicate predicate, JoinDescriptor joinDescriptor) {
        return findOne(getBaseFilter().and(predicate), joinDescriptor);
    }

    default Iterable<T> findAllAuthorized(Predicate predicate, JoinDescriptor joinDescriptor) {
        return findAll(getBaseFilter().and(predicate), joinDescriptor);
    }

    default Iterable<T> findAllAuthorized(JoinDescriptor joinDescriptor) {
        return findAll(getBaseFilter(), joinDescriptor);
    }

    default Page<T> findAllAuthorized(Predicate predicate, Pageable pageable, JoinDescriptor joinDescriptor) {
        return findAll(getBaseFilter().and(predicate), pageable, joinDescriptor);
    }

    default long countAuthorized(Predicate predicate) {
        return count(getBaseFilter().and(predicate));
    }

    default boolean existsAuthorized(Predicate predicate) {
        return exists(getBaseFilter().and(predicate));
    }


    default <V> V findOneAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return findOne(q -> createQueryFactory.apply(q).where(getBaseFilter()));
    }

    default <V> List<V> findAllAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return findAll(q -> createQueryFactory.apply(q).where(getBaseFilter()));
    }

    default <V> Page<V> findAllAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, Pageable pageable) {
        return findAll(q -> createQueryFactory.apply(q).where(getBaseFilter()), pageable);
    }

    default <V> long countAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return count(q -> createQueryFactory.apply(q).where(getBaseFilter()));
    }

    default <V> boolean existsAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory) {
        return exists(q -> createQueryFactory.apply(q).where(getBaseFilter()));
    }

    //---------------------------添加状态条件过滤--------------------------------
    default T findOneAuthorized(Predicate predicate, boolean enableStatusFilter) {
        return findOne(getBaseFilter(enableStatusFilter).and(predicate));
    }

    default Iterable<T> findAllAuthorized(Predicate predicate, boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter).and(predicate));
    }

    default Iterable<T> findAllAuthorized(boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter));
    }

    default Page<T> findAllAuthorized(Predicate predicate, Pageable pageable, boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter).and(predicate), pageable);
    }

    default T findOneAuthorized(Predicate predicate, JoinDescriptor joinDescriptor, boolean enableStatusFilter) {
        return findOne(getBaseFilter(enableStatusFilter).and(predicate), joinDescriptor);
    }

    default Iterable<T> findAllAuthorized(Predicate predicate, JoinDescriptor joinDescriptor, boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter).and(predicate), joinDescriptor);
    }

    default Iterable<T> findAllAuthorized(JoinDescriptor joinDescriptor, boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter), joinDescriptor);
    }

    default Page<T> findAllAuthorized(Predicate predicate, Pageable pageable, JoinDescriptor joinDescriptor, boolean enableStatusFilter) {
        return findAll(getBaseFilter(enableStatusFilter).and(predicate), pageable, joinDescriptor);
    }

    default long countAuthorized(Predicate predicate, boolean enableStatusFilter) {
        return count(getBaseFilter(enableStatusFilter).and(predicate));
    }

    default boolean existsAuthorized(Predicate predicate, boolean enableStatusFilter) {
        return exists(getBaseFilter(enableStatusFilter).and(predicate));
    }

    default <V> V findOneAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, boolean enableStatusFilter) {
        return findOne(q -> createQueryFactory.apply(q).where(getBaseFilter(enableStatusFilter)));
    }

    default <V> List<V> findAllAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, boolean enableStatusFilter) {
        return findAll(q -> createQueryFactory.apply(q).where(getBaseFilter(enableStatusFilter)));
    }

    default <V> Page<V> findAllAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, Pageable pageable, boolean enableStatusFilter) {
        return findAll(q -> createQueryFactory.apply(q).where(getBaseFilter(enableStatusFilter)), pageable);
    }

    default <V> long countAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, boolean enableStatusFilter) {
        return count(q -> createQueryFactory.apply(q).where(getBaseFilter(enableStatusFilter)));
    }

    default <V> boolean existsAuthorized(Function<JPAQueryFactory, JPAQuery<V>> createQueryFactory, boolean enableStatusFilter) {
        return exists(q -> createQueryFactory.apply(q).where(getBaseFilter(enableStatusFilter)));
    }

    default BooleanBuilder getBaseFilter() {
        return getBaseFilter(true);
    }

    default BooleanBuilder getBaseFilter(boolean enableStatusFilter) {
        BooleanBuilder filter = getAuthorizationFilter();
        if (enableStatusFilter) {
            filter.and(getStatusFilter());
        }
        return filter;
    }

    default BooleanBuilder getAuthorizationFilter() {
        return new BooleanBuilder();
    }

    default BooleanBuilder getStatusFilter() {
        return new BooleanBuilder();
    }
}
