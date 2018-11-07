package com.sztouyun.advertisingsystem.repository;

import com.sztouyun.advertisingsystem.common.jpa.JpaRepositoryBase;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseAutoKeyRepository<T>  extends JpaRepositoryBase<T, Long> {
}
