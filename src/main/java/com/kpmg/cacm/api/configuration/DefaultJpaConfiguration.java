package com.kpmg.cacm.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.kpmg.cacm.api.repository.spring")
public class DefaultJpaConfiguration {}