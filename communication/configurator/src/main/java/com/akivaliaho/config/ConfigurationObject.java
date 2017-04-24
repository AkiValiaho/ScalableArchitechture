package com.akivaliaho.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by akivv on 6.4.2017.
 */
@Entity
@RequiredArgsConstructor
@NamedEntityGraph(name = "com.akivaliaho.amqp.com.akivaliaho.config.ConfigurationObject.propertyMap",
        attributeNodes = @NamedAttributeNode("propertyMap"))
public class ConfigurationObject {
    @Getter
    @Setter
    @NonNull
    String configurationId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ElementCollection
    @Getter
    @Setter
    @NonNull
    private Map<String, String> propertyMap;

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }
}
