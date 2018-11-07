package com.sztouyun.advertisingsystem.repository;

import com.sztouyun.advertisingsystem.common.jpa.JpaRepositoryBase;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T>  extends JpaRepositoryBase<T, String> {
}
