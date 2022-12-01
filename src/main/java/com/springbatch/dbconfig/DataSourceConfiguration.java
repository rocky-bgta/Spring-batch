package com.springbatch.dbconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource datasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("cashedev")
    @ConfigurationProperties(prefix = "spring.cashedev")
    public DataSource cashedev(){
        return DataSourceBuilder.create().build();
    }

}
