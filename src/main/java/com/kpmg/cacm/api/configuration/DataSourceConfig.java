package com.kpmg.cacm.api.configuration;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource appDatasource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate appJdbcTemplate(@Qualifier("appDatasource") DataSource appDatasource) {
        return new JdbcTemplate(appDatasource);
    }

    @Bean
    @ConfigurationProperties(prefix = "cacm.config.client.datasource")
    public DataSource clientDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate clientJdbcTemplate(@Qualifier("clientDataSource") DataSource clientDataSource) {
        return new JdbcTemplate(clientDataSource);
    }
}
