package com.akivaliaho.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by vagrant on 4/5/17.
 */
@Entity
@NoArgsConstructor
public class ServiceEvent implements Serializable {
    @Id
    @Getter
    private Long id;
    @Getter
    private String eventName;
    @Getter
    private Object[] parameters;

    public ServiceEvent(ServiceEvent event) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.parameters = event.getParameters();
    }

    public void saveEvent(String eventName, Object... params) {
        this.eventName = eventName;
        this.parameters = params;
    }

    public EventParams getEventParams() {
        return new EventParams(eventName, parameters);
    }

    @AllArgsConstructor
    class EventParams {
        @Getter
        String eventName;
        @Getter
        Object[] params;
    }
}
