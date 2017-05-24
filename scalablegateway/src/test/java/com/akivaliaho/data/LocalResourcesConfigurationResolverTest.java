package com.akivaliaho.data;

import com.akivaliaho.config.ConfigEnum;
import com.akivaliaho.config.ConfigurationObject;
import com.akivaliaho.config.LocalResourcesConfigurationResolver;
import mockit.Deencapsulation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by akivv on 10.4.2017.
 */
public class LocalResourcesConfigurationResolverTest {
    private LocalResourcesConfigurationResolver localResourcesConfigurationResolver;

    @Before
    public void setUp() throws Exception {
        this.localResourcesConfigurationResolver = new LocalResourcesConfigurationResolver();
    }

    @Test
    public void findJpaConfigurationObjectByConfigurationId() throws Exception {
        //Check jpa configs are loaded
        Environment environmentMock = mock(Environment.class);
        Deencapsulation.setField(this.localResourcesConfigurationResolver, "env", environmentMock);
        initEnvMocks(environmentMock);
        ConfigurationObject configurationObjectByConfigurationId = this.localResourcesConfigurationResolver.findConfigurationObjectByConfigurationId(ConfigEnum.JPA_PROPERTIES);
        Map<String, String> propertyMap = configurationObjectByConfigurationId.getPropertyMap();
        assertEquals(propertyMap.get("hibernate.dialect"), "testDialect");
    }


    private void initEnvMocks(Environment environmentMock) {
        when(environmentMock.getProperty("hibernate.dialect")).thenReturn("testDialect");
        when(environmentMock.getProperty("hibernate.hbm2ddl.auto")).thenReturn("false");
        when(environmentMock.getProperty("hibernate.show_sql")).thenReturn("false");
        when(environmentMock.getProperty("hibernate.format_sql")).thenReturn("false");
    }

}