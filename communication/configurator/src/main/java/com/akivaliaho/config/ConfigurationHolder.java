package com.akivaliaho.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

/**
 * Created by vagrant on 4/5/17.
 */
@Component
public class ConfigurationHolder {
    @Autowired
    ConfigurationResolver configurationResolver;
    private String domainPackages;
    private Properties dataSourceProperties;
    private String serviceOutboundQueueName;
    private String masterBrokerOutboundExchange;

    public Map<String, String> getJpaProperties() {
        ConfigurationObject jpaProperties = configurationResolver.findConfigurationObjectByConfigurationId("jpaProperties");
        return jpaProperties.getPropertyMap();
    }

    public Properties getDataSourceProperties() {
        ConfigurationObject configurationObjectByConfigurationId = configurationResolver.findConfigurationObjectByConfigurationId(ConfigEnum.DATASOURCE_CONFIG);
        Properties properties = new Properties();
        properties.putAll(configurationObjectByConfigurationId.getPropertyMap());
        return properties;
    }

    public Map<String, String> getMessagingConfiguration() {
        ConfigurationObject messagingConfig = configurationResolver.findConfigurationObjectByConfigurationId(ConfigEnum.MESSAGING_CONFIG);
        return messagingConfig.getPropertyMap();
    }
}
