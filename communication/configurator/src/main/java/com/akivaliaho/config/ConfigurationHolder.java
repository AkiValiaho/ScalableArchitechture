package com.akivaliaho.config;

import com.akivaliaho.config.annotations.Interest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vagrant on 4/5/17.
 */
@Component
@Slf4j
public class ConfigurationHolder {
    @Autowired
    ConfigurationResolver configurationResolver;
    @Autowired
    ApplicationContext applicationContext;
    Map<String, Method> interestConfiguration;
    private String domainPackages;
    private Properties dataSourceProperties;
    private String serviceOutboundQueueName;
    private String masterBrokerOutboundExchange;
    @Getter
    private Map<String, Method> interestMap = new HashMap<>();

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

    public List<String> getInterests() {
        // Scan classpath to find all @Interest annotated methods
        return scanClassPathForInterests("com.akivaliaho");
    }


    public List<String> scanClassPathForInterests(String rootPackage) {
        List<URL> collect = new ArrayList<>(getURLsForPackage(rootPackage));
        Configuration configuration = new ConfigurationBuilder().addUrls(collect)
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner(),
                        new FieldAnnotationsScanner());
        Reflections reflections = new Reflections(configuration);
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(Interest.class);
        methodsAnnotatedWith.stream()
                .forEach(method -> {
                    Class<?> value = method.getAnnotation(Interest.class).receives();
                    if (value.getCanonicalName().equals("java.lang.Object")) {
                        return;
                    }
                    interestMap.put(value.getCanonicalName(), method);
                });
        List<String> collect1 = methodsAnnotatedWith.stream()
                //Get Interest annotated stuff
                .map(interest -> interest.getAnnotation(Interest.class).receives().getCanonicalName())
                .filter(interest -> !interest.equals("java.lang.Object"))
                .collect(Collectors.toList());
        return collect1;
    }

    private Collection<URL> getURLsForPackage(String string) {
        Collection<URL> forPackage = ClasspathHelper.forPackage(string, null);
        //Add the package ending to the url
        return forPackage.stream()
                .peek(msg -> log.info("Found URL: {}", msg.getPath()))
                .map(msg -> {
                    try {
                        return appendPackageName(string, msg);
                    } catch (MalformedURLException e) {
                        log.error("Couldn't create an URL", e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    private URL appendPackageName(String string, URL msg) throws MalformedURLException {
        String changedPackageName = string.replaceAll("\\.", "/");
        String urlAsString = msg.toString();
        return new URL(urlAsString + changedPackageName);
    }
}
