package com.akivaliaho.data;

import com.zaxxer.hikari.HikariDataSource;
import com.akivaliaho.config.ConfigurationHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.util.Properties;

/**
 * Created by vagrant on 4/5/17.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.akivaliaho.amqp")
@EnableTransactionManagement
@DependsOn(value = "configurationHolder")
public class DataConfig {
    @Autowired
    ConfigurationHolder configurationHolder;

    @Bean
    TransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return (TransactionManager) jpaTransactionManager;
    }

    @Bean
    DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        Properties dataSourceProperties = configurationHolder.getDataSourceProperties();
        hikariDataSource.setPassword(dataSourceProperties.getProperty("db.driver"));
        hikariDataSource.setUsername(dataSourceProperties.getProperty("db.username"));
        hikariDataSource.setJdbcUrl(dataSourceProperties.getProperty("db.url"));
        hikariDataSource.setPassword(dataSourceProperties.getProperty("db.password"));
        return hikariDataSource;
    }

    @Bean
    protected LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.akivaliaho.domain");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.putAll(configurationHolder.getJpaProperties());
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        return localContainerEntityManagerFactoryBean;
    }
}
