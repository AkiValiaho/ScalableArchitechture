package com.akivaliaho.config;

/**
 * Created by akivv on 6.4.2017.
 */
public interface ConfigurationResolver {
    ConfigurationObject findConfigurationObjectByConfigurationId(String propertyName);
}
