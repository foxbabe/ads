package com.sztouyun.advertisingsystem.common.jpa;


import com.sztouyun.advertisingsystem.common.jpa.impl.JoinFetchCapableQueryDslJpaRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class JoinFetchRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    public JoinFetchRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {

        return new JoinFetchFactory(entityManager);
    }

    private static class JoinFetchFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public JoinFetchFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @Override
        protected  SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information) {
            JpaEntityInformation entityInformation = getEntityInformation(information.getDomainType());
            return new JoinFetchCapableQueryDslJpaRepositoryImpl<T,I>(entityInformation,entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return JpaRepositoryBase.class;
        }
    }
}
