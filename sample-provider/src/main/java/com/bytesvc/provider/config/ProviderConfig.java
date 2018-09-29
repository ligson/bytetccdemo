package com.bytesvc.provider.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.bytesoft.bytejta.supports.jdbc.LocalXADataSource;
import org.bytesoft.bytetcc.supports.springcloud.config.SpringCloudConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Import(SpringCloudConfiguration.class)
@Configuration
public class ProviderConfig {

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        LocalXADataSource localXADataSource = new LocalXADataSource();
        localXADataSource.setDataSource(invokeGetDataSource());
        return localXADataSource;
    }

    public BasicDataSource invokeGetDataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/dt?useSSL=false");
        bds.setUsername("root");
        bds.setPassword("password");
        bds.setMaxTotal(50);
        bds.setInitialSize(20);
        bds.setMaxWaitMillis(60000);
        bds.setMinIdle(6);
        bds.setLogAbandoned(true);
        bds.setRemoveAbandonedOnBorrow(true);
        bds.setRemoveAbandonedOnMaintenance(true);
        bds.setRemoveAbandonedTimeout(1800);
        bds.setTestWhileIdle(true);
        bds.setTestOnBorrow(false);
        bds.setTestOnReturn(false);
        bds.setValidationQuery("select 'x' ");
        bds.setValidationQueryTimeout(1);
        bds.setTimeBetweenEvictionRunsMillis(30000);
        bds.setNumTestsPerEvictionRun(20);
        return bds;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(@Autowired DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

}
