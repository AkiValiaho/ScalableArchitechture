package com.akivaliaho.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by akivv on 6.4.2017.
 */
@Component
@Primary
public class LocalResourcesConfigurationResolver implements ConfigurationResolver {

    @Autowired
    Environment env;

    @Override
    public ConfigurationObject findConfigurationObjectByConfigurationId(String propertyName) {
        if (propertyName.equals(ConfigEnum.JPA_PROPERTIES)) {
            return fetchJpaProperties();
        } else if ((propertyName.equals(ConfigEnum.DATASOURCE_CONFIG))) {
            return fetchDataSourceConfig();
        } else if (propertyName.equals(ConfigEnum.MESSAGING_CONFIG)) {
            return fetchMessagingConfig();
        }
        return null;
    }

    private ConfigurationObject fetchMessagingConfig() {
        String[] messagingConfigArray = {
                "mq.gatewayqueue",
                "mq.to_esb_exchange",
                "mq.from_esb_exchange",
                "mq.from_esb_queue",
                "mq.to_esb_queue",
                "mq.port",
                "mq.username",
                "mq.password",
                "mq.address",
                "mq.servicebasename",
                "mq.servicequeue"
        };
        Map<String, String> dataSourceConfig = configArrayToMap(messagingConfigArray);
        return new ConfigurationObject(ConfigEnum.DATASOURCE_CONFIG, dataSourceConfig);
    }

    private ConfigurationObject fetchDataSourceConfig() {
        String[] dataSourceConfigArray = {
                "db.driver",
                "db.url",
                "db.username",
                "db.password"
        };
        Map<String, String> collect = configArrayToMap(dataSourceConfigArray);
        return new ConfigurationObject(ConfigEnum.DATASOURCE_CONFIG, collect);
    }

    private Map<String, String> configArrayToMap(String[] dataSourceConfigArray) {
        return Arrays.stream(dataSourceConfigArray)
                .map(this::propertyObject)
                .collect(Collectors.toMap(prop -> prop.getPropertyName(), prop -> prop.getPropertyValue()));
    }

    private PropertyObject propertyObject(String s) {
        return new PropertyObject(s, env.getProperty(s));
    }

    private ConfigurationObject fetchJpaProperties() {
        String[] jpaPropertyNameArray = {
                "hibernate.dialect",
                "hibernate.hbm2ddl.auto",
                "hibernate.show_sql",
                "hibernate.format_sql"
        };
        return new ConfigurationObject(ConfigEnum.JPA_PROPERTIES, configArrayToMap(jpaPropertyNameArray));
    }


    @AllArgsConstructor
    private class PropertyObject {
        @Getter
        @Setter
        String propertyName;
        @Getter
        @Setter
        String propertyValue;
    }
}
