package com.kpmg.cacm.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class,
    basePackages = "com.kpmg.cacm.api.repository.datatables"
)
public class DataTablesJpaConfiguration {}