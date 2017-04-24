package com.akivaliaho.data;

import com.akivaliaho.config.ConfigurationHolder;
import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by akivv on 7.4.2017.
 */
public class DataConfigTest {
    private DataConfig dataConfig;

    @Before
    public void init() {
        this.dataConfig = new DataConfig();
    }

    @Test
    public void transactionManager() throws Exception {
    }

    @Test
    public void dataSource() throws Exception {
    }

    @Test
    public void entityManagerFactory() throws Exception {
        ConfigurationHolder configurationHolderMock = new MockUp<ConfigurationHolder>() {
            @Mock
            public Map<String, String> getJpaProperties() {
                return Collections.singletonMap("Hello world", "Hello world");
            }
        }.getMockInstance();
        DataSource dataSource = new MockUp<DataSource>() {

        }.getMockInstance();
        Deencapsulation.setField(this.dataConfig, "configurationHolder", configurationHolderMock);
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = this.dataConfig.entityManagerFactory(dataSource);
        Map<String, Object> jpaPropertyMap = localContainerEntityManagerFactoryBean.getJpaPropertyMap();
        assertTrue(jpaPropertyMap.containsKey("Hello world"));
    }

}