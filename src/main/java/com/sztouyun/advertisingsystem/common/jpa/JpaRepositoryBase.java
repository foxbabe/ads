package com.sztouyun.advertisingsystem.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface JpaRepositoryBase<T, ID extends Serializable> extends AuthorizedQueryDslPredicateExecutor<T>,JpaRepository<T, ID> {
}
