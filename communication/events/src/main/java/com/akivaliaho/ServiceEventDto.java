package com.akivaliaho;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by akivv on 23.7.2017.
 */
public class ServiceEventDto implements Serializable {
    @Getter
    @Setter
    private String originalEventName;
    @Getter
    @Setter
    private Object[] originalParameters;
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String eventName;
    @Getter
    @Setter
    private Object[] parameters;

    public ServiceEventDto(String originalEventName, Object[] originalParameters, Long id, Object[] parameters, String event) {
        this.originalEventName = originalEventName;
        this.originalParameters = originalParameters;
        this.id = id;
        this.eventName = event;
        this.parameters = parameters;

    }
}
